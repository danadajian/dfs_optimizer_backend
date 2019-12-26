package com.optimizer.util;

import util.DateOperations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateOperationsTest {

    private DateOperations dateOperations = spy(DateOperations.class);
    private Calendar testCal = Calendar.getInstance();

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        testCal.setTime(sdf.parse("Wed Nov 13 16:20:00 CST 2019"));
    }

    @AfterEach
    void verifyDates() {
        verify(dateOperations).getTodaysDate();
    }

    @Test
    void shouldReturnTodaysDateString() {
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTodaysDateString();
        assertEquals("2019-11-13", result);
    }

    @Test
    void shouldGetEasternTime() {
        dateOperations.getTodaysDate();
        String result = dateOperations.getEasternTime("2019-11-17T18:00:00", "utc",
                "EEE h:mma z");
        assertEquals("Sun 1:00PM EST", result);
    }
}