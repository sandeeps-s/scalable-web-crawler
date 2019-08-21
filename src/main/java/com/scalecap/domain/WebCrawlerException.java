package com.scalecap.domain;

public class WebCrawlerException extends RuntimeException {

    public WebCrawlerException(String message) {
        super(message);
    }

    public WebCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
