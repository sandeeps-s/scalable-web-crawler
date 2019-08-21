package com.scalecap.domain;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class WebPageTest {

    @Test
    public void shouldGetCorrectNoOfResultLinks_given_validBody_when_creatingWebPage() throws URISyntaxException, IOException {

        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-page.html").toURI()));
        WebPage webPage = new WebPage(resultPageBody);

        assertThat(webPage.links()).hasSize(10);
    }

    @Test
    public void shouldGetEmptyList_given_bodyWithoutResultLinks_when_gettingLinks() throws URISyntaxException, IOException {

        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-page-without-links.html").toURI()));
        WebPage webPage = new WebPage(resultPageBody);

        assertThat(webPage.links()).isNotNull();
        assertThat(webPage.links()).hasSize(0);
    }

    @Test
    public void shouldGetCorrectNoOfJsLibs_given_validBody_when_gettingJsLibraries() throws URISyntaxException, IOException {

        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-link.html").toURI()));
        WebPage webPage = new WebPage(resultPageBody);

        assertThat(webPage.jsLibraries()).hasSize(6);
    }

    @Test
    public void shouldGetEmptyList_given_bodyWithoutScripts_when_gettingJsLibraries() throws URISyntaxException, IOException {

        String resultPageBody = Files.readString(Paths.get(ClassLoader.getSystemResource("result-link-without-js-scripts.html").toURI()));
        WebPage webPage = new WebPage(resultPageBody);

        assertThat(webPage.jsLibraries()).isNotNull();
        assertThat(webPage.jsLibraries()).hasSize(0);
    }


    @Test
    public void shouldThrowException_given_blankBody_when_creatingWebPage() {

        Throwable thrown = catchThrowable(() -> new WebPage(""));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Page body must not be null or empty");
    }

    @Test
    public void shouldThrowException_given_nullBody_when_creatingWebPage() {

        Throwable thrown = catchThrowable(() -> new WebPage(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Page body must not be null or empty");
    }

}