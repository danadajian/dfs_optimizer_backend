package api;

import api.HttpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    private HttpRequest httpRequest = new HttpRequest();

    @Test
    void shouldReturnErrorCodeMessage() {
        String errorMessageExample = "java.io.IOException: Server returned HTTP response code: 500 for URL: https://httpstat.us/500";
        assertEquals(500, httpRequest.extractErrorCodeFromString(errorMessageExample));
    }
}