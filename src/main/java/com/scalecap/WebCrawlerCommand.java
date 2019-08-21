package com.scalecap;

import com.scalecap.domain.WebCrawlerService;

import java.util.Map;

class WebCrawlerCommand {

    private final WebCrawlerService webCrawlerService;

    WebCrawlerCommand(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }

    Map<String, Long> findMostUsedJsLibraries(String searchTerm, int top) {
        return webCrawlerService.findMostUsedJsLibraries(searchTerm, top);
    }

}
