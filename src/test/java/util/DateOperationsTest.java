package util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateOperationsTest {

    DateOperations dateOperations = spy(DateOperations.class);
    Calendar testCal = Calendar.getInstance();

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        testCal.setTime(sdf.parse("Wed Nov 13 16:20:00 CST 2019"));
    }

    @AfterEach
    void verifyDates() {
        verify(dateOperations).getDateToday();
    }

    @Test
    void shouldReturnTodaysDateString() {
        when(dateOperations.getDateToday()).thenReturn(testCal);
        String result = dateOperations.getDateTodayString();
        assertEquals("2019-11-13", result);
    }

    @Test
    void shouldGetEasternTime() {
        dateOperations.getDateToday();
        String result = dateOperations.getEasternTime("2019-11-17T18:00:00", "utc",
                "EEE h:mma z");
        assertEquals("Sun 1:00PM EST", result);
    }
}