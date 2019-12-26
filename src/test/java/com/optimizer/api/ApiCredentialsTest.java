package com.optimizer.api;

import api.ApiCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class ApiCredentialsTest {

    @Spy
    ApiCredentials credentials = new ApiCredentials();

    @Test
    void shouldImportCredentialsFromPropertiesFile() {
        String[] result = credentials.getPropValues("src/main/resources/config.properties");
        assertTrue(result[0].length() > 0 & result[1].length() > 0);
    }

    @Test
    void shouldTryToImportCredentialsFromEnvironmentVariables() {
        String[] result = credentials.getPropValues("invalidPath");
        assertTrue(result[0].length() == 0 & result[1].length() == 0);
    }

    @Test
    void shouldReturnSignature() {
        String key = "";
        String secret = "";
        String result = credentials.getSignature(key, secret);
        assertTrue(result.length() > 0);
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }
}