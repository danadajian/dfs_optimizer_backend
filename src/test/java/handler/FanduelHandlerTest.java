package handler;

import collect.dfs.Fanduel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

class FanduelHandlerTest {

    @Mock
    Fanduel fanduel;

    @InjectMocks
    FanduelHandler fanduelHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldHandleRequestForDraftKingsData() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("date", "2019-12-16");
        fanduelHandler.handleRequest(testMap);
        verify(fanduel).getAllContestData("2019-12-16");
    }
}