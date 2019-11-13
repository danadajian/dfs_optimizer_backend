package api;

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
    void shouldReturnFileNotFoundIf404() {
        String url = "https://httpstat.us/404";
        String result = "";
        try {
            result = httpRequest.HttpRequest(url);
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(404, e.getErrorCode());
        }
        assertEquals("", result);
    }

    @Test
    void shouldReturnGenericExceptionIfNot200Or404() {
        String url403 = "https://httpstat.us/403";
        String result403 = "";
        try {
            result403 = httpRequest.HttpRequest(url403);
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(403, e.getErrorCode());
        }
        assertEquals("", result403);
        String url500 = "https://httpstat.us/500";
        String result500 = "";
        try {
            result500 = httpRequest.HttpRequest(url500);
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(500, e.getErrorCode());
        }
        assertEquals("", result500);
        String url596 = "https://httpstat.us/596";
        String result596 = "";
        try {
            result596 = httpRequest.HttpRequest(url596);
        } catch (MakeHttpRequest.ApiException e) {
            assertEquals(596, e.getErrorCode());
        }
        assertEquals("", result596);
    }

    @Test
    void shouldReturnErrorCodeMessage() {
        String errorMessageExample = "java.io.IOException: Server returned HTTP response code: 500 for URL: https://httpstat.us/500";
        assertEquals(500, httpRequest.extractErrorCodeFromString(errorMessageExample));
    }
}