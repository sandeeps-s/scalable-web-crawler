package com.scalecap;

import com.scalecap.domain.HttpService;
import com.scalecap.domain.SearchService;
import com.scalecap.domain.WebPage;

class WebSearchService implements SearchService {

    private final HttpService httpService;
    private final Config config;

    WebSearchService(HttpService httpService, Config config) {
        this.httpService = httpService;
        this.config = config;
    }

    @Override
    public WebPage search(String searchTerm) {
        String searchURI = String.format(config.searchURITemplate(), searchTerm);
        return httpService.getWebPageFromURI(searchURI);
    }

}
