package com.scalecap.domain;

import java.util.*;
import java.util.stream.Collectors;

public class WebCrawlerService {

    private final SearchService searchService;
    private final HttpService httpService;

    public WebCrawlerService(SearchService searchService, HttpService httpService) {
        this.searchService = Objects.requireNonNull(searchService);
        this.httpService = Objects.requireNonNull(httpService);
    }

    public Map<String, Long> findMostUsedJsLibraries(String searchTerm, int top) {

        if (Objects.isNull(searchTerm) || searchTerm.isBlank())
            throw new IllegalArgumentException("Search term must be a non-null and non-blank value");

        if (top <= 0)
            throw new IllegalArgumentException(String.format("Number of top libraries should be a positive value. Provided input value = %s", top));

        WebPage resultPage = searchService.search(searchTerm);

        List<WebPage> pages = httpService.getWebPagesFromURIs(resultPage.links());

        List<String> allJsLibs = getJsLibrariesForPages(pages);

        return getTopJsLibraries(allJsLibs, top);
    }

    private List<String> getJsLibrariesForPages(List<WebPage> pages) {

        return pages.stream()
                .map(WebPage::jsLibraries)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Map<String, Long> getTopJsLibraries(List<String> jsLibraries, int top) {

        return jsLibraries.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(
                        Collections.reverseOrder(Map.Entry.<String, Long>comparingByValue())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

}
