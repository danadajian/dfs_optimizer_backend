package handler;

import collect.dfs.Fanduel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.S3Upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FanduelHandlerTest {

    @Mock
    Fanduel fanduel;

    @Mock
    S3Upload s3Upload;

    @InjectMocks
    FanduelHandler fanduelHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(fanduel.getAllContestData("2019-12-16")).thenReturn(new ArrayList<>());
    }

    @Test
    void shouldHandleRequestForFanduelData() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("date", "2019-12-16");
        fanduelHandler.handleRequest(testMap);
        verify(fanduel).getAllContestData("2019-12-16");
        verify(s3Upload, never()).uploadToS3(anyString(), any());
    }

    @Test
    void shouldHandlePipelineRequestForFanduelData() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("date", "2019-12-16");
        testMap.put("invocationType", "pipeline");
        fanduelHandler.handleRequest(testMap);
        verify(fanduel).getAllContestData("2019-12-16");
        verify(s3Upload, times(1)).uploadToS3(anyString(), any());
    }
}