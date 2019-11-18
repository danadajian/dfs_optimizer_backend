package com.optimizer.api;

import api.GetCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class GetCredentialsTest {

    @Spy
    GetCredentials credentials = new GetCredentials();

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
}