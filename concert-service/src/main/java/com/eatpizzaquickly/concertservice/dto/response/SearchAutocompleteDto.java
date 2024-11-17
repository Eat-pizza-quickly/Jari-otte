package com.eatpizzaquickly.concertservice.dto.response;

import com.eatpizzaquickly.concertservice.dto.SearchAutoTitleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SearchAutocompleteDto {
    private List<SearchAutoTitleDto> autoList;

    public static SearchAutocompleteDto of(List<SearchAutoTitleDto> titles) {
        return new SearchAutocompleteDto(titles);
    }
}
