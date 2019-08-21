package com.scalecap.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebCrawlerServiceTest {

    private final SearchService mockSearchService = mock(SearchService.class);
    private final HttpService mockHttpService = mock(HttpService.class);
    private final WebPage mockSearchResultPage = mock(WebPage.class);

    private final WebPage mockPage1 = mock(WebPage.class);
    private final WebPage mockPage2 = mock(WebPage.class);

    private final WebCrawlerService webCrawlerService = new WebCrawlerService(mockSearchService, mockHttpService);

    @Test
    void shouldFindCorrectUsedJsLibs_given_validSearchTerm_when_findingMostUsedJsLibraries() {

        when(mockSearchService.search(anyString())).thenReturn(mockSearchResultPage);
        when(mockHttpService.getWebPagesFromURIs(anyList())).thenReturn(List.of(mockPage1, mockPage2));
        when(mockSearchResultPage.links()).thenReturn(List.of("http://openjdk.net", "https://gradle.org/"));
        when(mockPage1.jsLibraries()).thenReturn(List.of("jQuery.js", "angular.js", "react.js"));
        when(mockPage1.jsLibraries()).thenReturn(List.of("react.js", "angular.js"));

        Map<String, Long> mostUsedJsLibraries = webCrawlerService.findMostUsedJsLibraries("microservices", 2);

        assertThat(mostUsedJsLibraries.keySet()).extracting(s -> s).contains("angular.js", "react.js");
        assertThat(mostUsedJsLibraries.keySet()).extracting(s -> s).doesNotContain("jQuery.js");
    }

    @Test
    void shouldThrowExceptions_given_invalidSearchTerm_when_findingMostUsedJsLibraries() {

        Throwable thrown = catchThrowable(() -> webCrawlerService.findMostUsedJsLibraries("", 2));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Search term must be a non-null and non-blank value");
    }

    @Test
    void shouldThrowExceptions_given_invalidTopValue_when_findingMostUsedJsLibraries() {

        Throwable thrown = catchThrowable(() -> webCrawlerService.findMostUsedJsLibraries("microservices", 0));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Number of top libraries should be a positive value. Provided input value = 0");
    }

}