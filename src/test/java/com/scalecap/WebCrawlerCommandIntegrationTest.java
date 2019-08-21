package com.scalecap;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class WebCrawlerCommandIntegrationTest {

    private WireMockServer wireMockServer;

    private final WebCrawlerCommand webCrawlerCommand = App.bootStrap();

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        setupStub();
    }

    @Test
    void shouldFindMostUsedJsLibraries() {

        Map<String, Long> mostUsedJsLibraries = webCrawlerCommand.findMostUsedJsLibraries("Kotlin", 5);
        assertThat(mostUsedJsLibraries.keySet()).extracting(s -> s)
                .containsOnlyOnce("analytics.js", "gtm.js", "fbevents.js", "gpt.js", "inferredEvents.js");
    }

    private void setupStub() {
        wireMockServer.stubFor(
                get(urlEqualTo("/search?q=Kotlin"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("google-result-page.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/wiki/Kotlin_(Programmiersprache)"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("kotlin-programmiersprache-wikipedia.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/kotlin-lang"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("kotlin-lang.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/news/kotlin-programmiersprache/"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("kotlin-programmiersprache.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/entwicklung/programmiersprachen/ist-kotlin-das-bessere-java-eine-einfuehrung.html"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("ist-kotlin-das-bessere-java-eine-einfuehrung.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/de/6-wichtige-gruende-fuer-kotlin-als-java-nachfolger-im-enterprise-bereich"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("6-wichtige-gruende-fuer-kotlin-als-java-nachfolger-im-enterprise-bereich.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/digitalguide/websites/web-entwicklung/kotlin-tutorial/"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("web-entwicklung-kotlin-tutorial.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/kotlin-tutorial"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("kotlin-tutorial.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/kotlin/learn"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("kotlin-learn.html")
                        )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/developer/meldung/I-O-2019-Googles-Bekenntnis-zu-Kotlin-4417060.html"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withBodyFile("googles-bekenntnis-zu-kotlin.html")
                        )
        );

    }

}