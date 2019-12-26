package com.optimizer.api;

import api.HttpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class httpRequestTest {

    private HttpRequest httpRequest = new HttpRequest();

    @Test
    void shouldReturnErrorCodeMessage() {
        String errorMessageExample = "java.io.IOException: Server returned HTTP response code: 500 for URL: https://httpstat.us/500";
        assertEquals(500, httpRequest.extractErrorCodeFromString(errorMessageExample));
    }
}