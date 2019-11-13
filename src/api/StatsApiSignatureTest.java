package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class StatsApiSignatureTest {

    @Test
    void shouldReturnSignature() {
        String key = "";
        String secret = "";
        String result = new StatsApiSignature(key, secret).getSignature();
        assertTrue(result.length() > 0);
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }
}