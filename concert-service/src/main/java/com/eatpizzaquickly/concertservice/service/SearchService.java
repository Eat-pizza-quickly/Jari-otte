package com.eatpizzaquickly.concertservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.eatpizzaquickly.concertservice.dto.SearchAutoTitleDto;
import com.eatpizzaquickly.concertservice.dto.SearchConcertResponseDto;
import com.eatpizzaquickly.concertservice.dto.response.SearchAutocompleteDto;
import com.eatpizzaquickly.concertservice.dto.response.SearchConcertListDto;
import com.eatpizzaquickly.concertservice.entity.Concert;
import com.eatpizzaquickly.concertservice.entity.ConcertSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;

    /* 자동 완성 기능 구현 (오타 허용 및 부분 일치) */
    public SearchAutocompleteDto autocomplete(String query) {
        try {
            // MultiMatchQuery 구성: 오타 허용과 접두사 일치
            MultiMatchQuery matchQueryWithFuzziness = MultiMatchQuery.of(m -> m
                    .query(query)                                         // 사용자가 입력한 검색어
                    .fields("title^2", "artists")          // title에 가중치 2배 부여, artists 필드 포함
                    .fuzziness("AUTO")                              // 오타 허용
                    .operator(Operator.Or)                                // OR 연산자로 매칭
            );

            // 접두사 일치를 위한 MultiMatchQuery 구성
            MultiMatchQuery matchQueryWithPhrasePrefix = MultiMatchQuery.of(m -> m
                    .query(query)
                    .fields("title^2", "artists")           // title과 artists 필드 포함
                    .type(TextQueryType.PhrasePrefix)                      // 접두사 일치
            );

            // BoolQuery 구성
            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .should(Query.of(q -> q.multiMatch(matchQueryWithFuzziness)))      // 오타 허용 쿼리
                    .should(Query.of(q -> q.multiMatch(matchQueryWithPhrasePrefix)))   // 접두사 일치 쿼리
            );

            // SearchRequest 생성
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("concerts")                        // 인덱스 이름
                    .query(Query.of(q -> q.bool(boolQuery)))        // BoolQuery 사용
                    .size(10)                                 // 최대 10개 결과만 반환
            );

            // 검색 실행
            SearchResponse<ConcertSearch> searchResponse = elasticsearchClient.search(searchRequest, ConcertSearch.class);

            // 검색 결과를 DTO로 변환 (title과 artists의 각 항목을 개별적으로 추가하고 일치율 기준으로 정렬)
            List<SearchAutoTitleDto> autoList = searchResponse.hits().hits().stream()
                    .flatMap(hit -> {
                        assert hit.source() != null;
                        return SearchAutoTitleDto.from(hit.source()).stream();
                    })
                    // query로 시작하는 항목을 먼저 필터링
                    .sorted((dto1, dto2) -> {
                        boolean dto1StartsWithQuery = dto1.getAuto().startsWith(query);
                        boolean dto2StartsWithQuery = dto2.getAuto().startsWith(query);

                        // query로 시작하는 항목이 먼저 오도록 정렬
                        if (dto1StartsWithQuery && !dto2StartsWithQuery) return -1;
                        if (!dto1StartsWithQuery && dto2StartsWithQuery) return 1;

                        // 시작하지 않는 경우는 score 기준 정렬 유지
                        return 0;
                    })
                    // 중복 제거
                    .distinct()
                    // 최대 5개만 선택
                    .limit(5)
                    .collect(Collectors.toList());

            // 결과 반환
            return new SearchAutocompleteDto(autoList);

        } catch (IOException e) {
            throw new RuntimeException("자동완성 검색 실패", e);
        }
    }

    /* 멀티필드검색 및 오타 허용 검색 (공연이름, 아티스트 이름) */
    public SearchConcertListDto searchConcerts(String query, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        try {
            // 다중 필드 검색, 오타 허용
            // title과 artists 필드에서 검색어가 포함된 문서를 찾고, 오타도 허용합니다.
            MultiMatchQuery matchQueryWithFuzziness = MultiMatchQuery.of(m -> m
                    .query(query)                                  // 사용자가 입력한 검색어
                    .fields("title^2", "artists")   // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                    .fuzziness("AUTO")                      // 오타를 허용하여 유사한 검색어도 매칭
                    .operator(Operator.Or)                        // 모든 검색어를 포함할 필요 없이 하나만 포함해도 매칭
            );

            // phrase_prefix
            // 검색어가 입력된 단어의 앞부분만 맞아도 매칭되도록 설정
            // 검색어의 접두사에 맞는 문서도 검색
            MultiMatchQuery matchQueryWithPhrasePrefix = MultiMatchQuery.of(m -> m
                    .query(query)                                   // 사용자가 입력한 검색어
                    .fields("title^2", "artists")    // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                    .type(TextQueryType.PhrasePrefix)               // phrase_prefix 타입으로 설정하여 접두사 일치 허용
            );

            // Wildcard Query 추가: artists 필드에 대해 중간에 포함된 텍스트도 매칭
            WildcardQuery wildcardQuery = WildcardQuery.of(w -> w
                    .field("artists")
                    .value("*" + query + "*")  // 검색어가 포함된 부분 일치 허용
            );

            // BoolQuery에 추가할 필터 리스트 생성
            List<Query> filters = new ArrayList<>();

            // 날짜 필터 추가 (startDate와 endDate가 모두 존재할 경우에만 필터 적용)
            if (startDate != null && endDate != null) {
                // 시작일과 종료일을 UTC 시간의 ISO-8601 문자열로 변환
                String startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toString();
                String endDateTime = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toString();

                // DateRangeQuery 생성
                DateRangeQuery dateRangeQuery = new DateRangeQuery.Builder()
                        .field("startDate")        // 필드 지정
                        .gte(startDateTime)              // 시작 시간
                        .lte(endDateTime)                // 종료 시간
                        .build();

                // RangeQuery 생성 및 DateRangeQuery 추가
                RangeQuery rangeQuery = new RangeQuery.Builder()
                        .date(dateRangeQuery)      // DateRangeQuery를 RangeQuery에 추가
                        .build();

                // Query.Builder를 사용하여 RangeQuery 추가
                Query rangeQueryWrapper = new Query.Builder()
                        .range(rangeQuery)
                        .build();

                // 필터 리스트에 추가
                filters.add(rangeQueryWrapper);
            }
            // 삭제 여부 필터 추가 (deleted가 false인 문서만 반환)
            TermQuery deletedFilter = TermQuery.of(t -> t
                    .field("deleted")
                    .value(false) // deleted가 false인 문서만 포함
            );
            filters.add(new Query.Builder().term(deletedFilter).build());

            // Bool 쿼리 구성
            // BoolQuery의 should 조건을 사용하여 두 쿼리 중 하나라도 매칭되면 결과에 포함시킵니다.
            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .should(Query.of(q -> q.multiMatch(matchQueryWithFuzziness)))        // 첫 번째 쿼리: 오타 허용
                    .should(Query.of(q -> q.multiMatch(matchQueryWithPhrasePrefix)))    // 두 번째 쿼리: 접두사 일치
                    .should(Query.of(q -> q.wildcard(wildcardQuery)))                   // Wildcard 쿼리 추가
                    .filter(filters)                                                    // 필터 조건 추가 (필터가 있을 경우에만 적용)
            );

            // SearchRequest 생성
            // 검색할 인덱스와 쿼리를 설정하고, 페이지네이션 정보(from과 size)를 설정합니다.
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("concerts")                      // 검색할 인덱스 이름
                    .query(Query.of(q -> q.bool(boolQuery)))      // 위에서 구성한 BoolQuery를 사용
                    .from((int) pageable.getOffset())             // 검색 시작 위치 설정 (페이지네이션의 offset)
                    .size(pageable.getPageSize())                 // 한 페이지에 포함될 문서 수 설정
            );

            // 검색 실행
            // elasticsearchClient의 search 메서드를 통해 Elasticsearch에 쿼리를 실행, 검색 결과를 가져온다.
            SearchResponse<ConcertSearch> searchResponse = elasticsearchClient.search(searchRequest, ConcertSearch.class);

            // 결과를 SearchConcertResponseDto 로 변환
            List<SearchConcertResponseDto> concertSimpleDtoList = searchResponse.hits().hits().stream()
                    .map(Hit::source)                           // Hit 객체에서 ConcertSearch 객체 추출
                    .filter(Objects::nonNull)                   // null 값 필터링 (null 값이 있는 경우 리스트에 포함되지 않음)
                    .map(SearchConcertResponseDto::from)        // 각 ConcertSearch 객체를 SearchConcertResponseDto로 변환
                    .collect(Collectors.toList());              // 결과를 리스트로 수집

            // SearchConcertListDto 로 반환
            return SearchConcertListDto.of(concertSimpleDtoList);

        } catch (IOException e) {
            throw new RuntimeException("검색 쿼리 실패", e);
        }
    }

    /* 콘서트 생성 될때 인덱스에 저장 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveIndex(Concert concert) {
        try {
            // Concert 객체를 ConcertSearch로 변환
            ConcertSearch concertSearch = new ConcertSearch(
                    concert.getId(),
                    concert.getTitle(),
                    concert.getArtists(),
                    concert.getCategory(),
                    concert.getDeleted(),
                    concert.getStartDate(),
                    concert.getEndDate());

            // IndexRequest 생성
            IndexRequest<ConcertSearch> request = IndexRequest.of(i -> i
                    .index("concerts")                         // 인덱스 이름 설정
                    .id(concertSearch.getConcertId().toString())     // 문서 ID 설정
                    .document(concertSearch)                         // 문서 데이터 설정
            );

            // Elasticsearch에 데이터 인덱싱 요청
            IndexResponse response = elasticsearchClient.index(request);

            System.out.println("문서가 인덱싱되었습니다. 문서 ID: " + response.id());

        } catch (Exception e) {
            throw new RuntimeException("콘서트 인덱싱 실패", e);
        }
    }

    /* 콘서트 삭제 시 인덱스에서 해당 문서 삭제 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delIndex(Long concertId) {
        try {
            // DeleteRequest 생성
            DeleteRequest request = DeleteRequest.of(d -> d
                    .index("concerts")          // 인덱스 이름 지정
                    .id(concertId.toString())   // 삭제할 문서의 ID
            );

            // Elasticsearch에 삭제 요청
            DeleteResponse response = elasticsearchClient.delete(request);

            // 삭제 확인 로그
            if (response.result().name().equals("Deleted")) {
                System.out.println("문서가 성공적으로 삭제되었습니다. 문서 ID: " + concertId);
            } else {
                System.out.println("문서가 존재하지 않거나 이미 삭제되었습니다. 문서 ID: " + concertId);
            }

        } catch (Exception e) {
            throw new RuntimeException("콘서트 인덱스 삭제 실패", e);
        }
    }
}
