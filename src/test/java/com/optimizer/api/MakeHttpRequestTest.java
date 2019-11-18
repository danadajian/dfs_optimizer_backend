package com.optimizer.api;

import api.MakeHttpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MakeHttpRequestTest {

    private MakeHttpRequest httpRequest = new MakeHttpRequest();

    @Test
    void shouldReturnDataIf200() {
        String result = httpRequest.HttpRequest("https://httpstat.us/200");
        assertEquals("200 OK", result);
    }

    @Test
    void shouldReturnGenericExceptionIfNot200() {
        httpRequest.HttpRequest("https://httpstat.us/403");
        assertEquals(403, httpRequest.getStatusCode());
        httpRequest.HttpRequest("https://httpstat.us/404");
        assertEquals(404, httpRequest.getStatusCode());
        httpRequest.HttpRequest("https://httpstat.us/500");
        assertEquals(500, httpRequest.getStatusCode());
        String url596 = "https://httpstat.us/596";
        httpRequest.HttpRequest(url596);
        assertEquals(596, httpRequest.getStatusCode());
    }

    @Test
    void shouldReturnErrorCodeMessage() {
        String errorMessageExample = "java.io.IOException: Server returned HTTP response code: 500 for URL: https://httpstat.us/500";
        assertEquals(500, httpRequest.extractErrorCodeFromString(errorMessageExample));
    }
}