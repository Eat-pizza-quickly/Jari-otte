package com.eatpizzaquickly.concertservice.dto;

import com.eatpizzaquickly.concertservice.entity.ConcertSearch;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "auto")
public class SearchAutoTitleDto {
    private String auto; // 개별 검색어 항목 (콘서트 이름 또는 아티스트 이름)

    public static List<SearchAutoTitleDto> from(ConcertSearch concertSearch) {
        List<SearchAutoTitleDto> autoList = new ArrayList<>();

        // 콘서트 제목 추가
        autoList.add(new SearchAutoTitleDto(concertSearch.getTitle()));

        // 아티스트 목록이 있을 경우, 각 아티스트를 개별 항목으로 추가
        if (concertSearch.getArtists() != null && !concertSearch.getArtists().isEmpty()) {
            concertSearch.getArtists().forEach(artist ->
                    autoList.add(new SearchAutoTitleDto(artist)) // 각 아티스트를 개별적으로 추가
            );
        }

        return autoList;
    }
}
