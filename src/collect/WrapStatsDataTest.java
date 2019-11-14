package collect;

import api.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WrapStatsDataTest implements MockResponses {

    private ApiClient mockApi = mock(ApiClient.class);
    private WrapStatsData wrapStatsData = new WrapStatsData(mockApi);

    @BeforeEach
    void setUp() {
        when(mockApi.getEventsFromThisWeek()).thenReturn(fakeEventsResponse);
        when(mockApi.getProjectionsFromThisWeek()).thenReturn(fakeProjectionsResponse);
    }

    @Test
    void shouldGetEventIdsFromThisWeek() {
        List<Integer> result = wrapStatsData.getEventIdsFromThisWeek();
        List<Integer> eventList = Arrays.asList(2142081, 2142120, 2142062, 2142094, 2142098, 2142105, 2142114, 2142127,
                2142131, 2142140, 2142145, 2142163, 2142174, 2142182);
        verify(mockApi).getEventsFromThisWeek();
        assertEquals(eventList, result);
    }

    @Test
    void shouldGetProjectionsFromThisWeek() {
        Map<Integer, Map<String, Object>> result = wrapStatsData.getFantasyProjections();
        assertEquals(26.0332570149543679641473708950911299307, result.get(591816).get("dkProjection"));
        assertEquals(23.8622262095728403380510578974536722008, result.get(591816).get("fdProjection"));
        assertEquals(11.07, result.get(347).get("dkProjection"));
        assertEquals(11.07, result.get(347).get("fdProjection"));
    }

}