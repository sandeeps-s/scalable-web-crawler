package com.scalecap.domain;

import java.util.List;

public interface HttpService {

    List<WebPage> getWebPagesFromURIs(List<String> links);

    WebPage getWebPageFromURI(String link);

}
