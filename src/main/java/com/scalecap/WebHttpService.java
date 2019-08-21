package com.scalecap;

import com.scalecap.domain.HttpService;
import com.scalecap.domain.WebCrawlerException;
import com.scalecap.domain.WebPage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

class WebHttpService implements HttpService {

    HttpClient client = HttpClient.newHttpClient();

    @Override
    public List<WebPage> getWebPagesFromURIs(List<String> pageURIs) {

        if (Objects.isNull(pageURIs))
            return Collections.emptyList();

        List<CompletableFuture<WebPage>> webPageFutures = pageURIs.stream()
                .map(pageURI ->
                        CompletableFuture.supplyAsync(() -> getWebPageFromURI(pageURI))
                                .exceptionally(exception -> null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(webPageFutures.toArray(new CompletableFuture[webPageFutures.size()]));

        try {
            combinedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new WebCrawlerException("Exception while getting web pages", e);
        }

        return webPageFutures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @Override
    public WebPage getWebPageFromURI(String pageURI) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36")
                    .uri(URI.create(pageURI))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new WebCrawlerException(
                        String.format(
                                "Web page request failed with response code %d. Web page URI: %s.", response.statusCode(), pageURI)
                );
            }

            return new WebPage(response.body());

        } catch (IOException | InterruptedException e) {
            throw new WebCrawlerException("Exception while getting web page", e);
        }
    }
}
