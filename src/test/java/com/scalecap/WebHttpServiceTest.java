package com.scalecap;

import com.scalecap.domain.HttpService;
import com.scalecap.domain.WebCrawlerException;
import com.scalecap.domain.WebPage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebHttpServiceTest {

    private HttpClient mockHttpClient = mock(HttpClient.class);
    private HttpResponse<Object> mockHttpResponse = mock(HttpResponse.class);

    private class TestableWebHttpService extends WebHttpService {

        TestableWebHttpService(HttpClient client) {
            this.client = client;
        }
    }

    @Test
    void getWebPagesFromURIs() {
    }

    @Test
    void shouldGetCorrectNoOfWebPages_given_validResponseForAllUris_when_gettingWebPageFromURI() throws IOException, InterruptedException,
            URISyntaxException {

        when(mockHttpClient.send(any(HttpRequest.class), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-page.html").toURI()));
        when(mockHttpResponse.body()).thenReturn(resultPageBody);

        HttpService httpService = new TestableWebHttpService(mockHttpClient);
        List<String> uris = List.of("http://google.com", "http://infoq.com");
        List<WebPage> webPages = httpService.getWebPagesFromURIs(uris);

        assertThat(webPages).hasSize(2);
    }

    @Test
    void shouldGetEmptyList_given_allUriRequestsFail_when_gettingWebPageFromURI() throws IOException, InterruptedException {

        when(mockHttpClient.send(any(HttpRequest.class), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(400);

        HttpService httpService = new TestableWebHttpService(mockHttpClient);
        List<String> uris = List.of("http://google.com", "http://infoq.com");
        List<WebPage> webPages = httpService.getWebPagesFromURIs(uris);

        assertThat(webPages).hasSize(0);
    }

    @Test
    void shouldGetWebPageWithCorrectData_given_validResponse_when_gettingWebPageFromURI() throws IOException, InterruptedException,
            URISyntaxException {

        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-page.html").toURI()));
        when(mockHttpResponse.body()).thenReturn(resultPageBody);

        HttpService httpService = new TestableWebHttpService(mockHttpClient);

        WebPage webPage = httpService.getWebPageFromURI("http://google.com");

        assertThat(webPage.links()).hasSize(10);
    }

    @Test
    void shouldThrowException_given_responseWithNonSuccesCode_when_gettingWebPageFromURI() throws IOException, InterruptedException {

        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(400);

        HttpService httpService = new TestableWebHttpService(mockHttpClient);

        Throwable thrown = catchThrowable(() -> httpService.getWebPageFromURI("http://google.com"));

        assertThat(thrown).isInstanceOf(WebCrawlerException.class)
                .hasNoCause()
                .hasMessage("Web page request failed with response code 400. Web page URI: http://google.com.");
    }

    @Test
    void shouldMapToUncheckedException_given_checkedExceptionIsThrown_when_gettingWebPageFromURI() throws IOException, InterruptedException {

        when(mockHttpClient.send(any(), any())).thenThrow(new IOException());

        HttpService httpService = new TestableWebHttpService(mockHttpClient);

        Throwable thrown = catchThrowable(() -> httpService.getWebPageFromURI("http://google.com"));

        assertThat(thrown).isInstanceOf(WebCrawlerException.class)
                .hasCauseInstanceOf(IOException.class)
                .hasMessage("Exception while getting web page");
    }
}