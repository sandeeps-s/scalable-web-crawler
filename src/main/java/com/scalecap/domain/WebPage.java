package com.scalecap.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import static java.util.stream.Collectors.toList;

public class WebPage {

    private final List<String> jsLibraries;

    private final List<String> links;

    public WebPage(String body) {
        if (Objects.isNull(body) || body.isBlank())
            throw new IllegalArgumentException("Page body must not be null or empty");

        this.jsLibraries = extractJsLibrariesFromBody(body);
        this.links = extractLinksFromBody(body);
    }

    List<String> jsLibraries() {
        return List.copyOf(jsLibraries);
    }

    public List<String> links() {
        return List.copyOf(links);
    }

    private List<String> extractJsLibrariesFromBody(String body) {
        Document doc = Jsoup.parse(body);
        return doc.select("script[src]").stream()
                .map(element -> element.attr("src"))
                .filter(Objects::nonNull)
                .map(s -> (s.lastIndexOf('?') != -1) ? s.substring(0, s.lastIndexOf('?')): s)
                .filter(s -> s.endsWith(".js"))
                .map(this::getFileName)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private String getFileName(String scriptUrl) {
        return Collections.list(new StringTokenizer(scriptUrl, "/")).stream()
                .map(token -> (String) token)
                .filter(token -> token.contains(".js"))
                .findFirst()
                .orElse(null);
    }

    private List<String> extractLinksFromBody(String body) {
        Document doc = Jsoup.parse(body);
        return doc.select("div.r a[href]").stream()
                .map(element -> element.attr("href"))
                .filter(Objects::nonNull)
                .filter(s -> s.startsWith("http"))
                .filter(s -> !s.contains("webcache.googleusercontent.com"))
                .filter(s -> !s.contains("www.google.com"))
                .filter(s -> !s.contains("translate.google.com"))
                .collect(toList());
    }


}
