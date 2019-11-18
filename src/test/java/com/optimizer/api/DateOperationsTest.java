package com.optimizer.api;

import api.DateOperations;
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
    void shouldReturnTodaysDateStringTuesdayAdjusted() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeekToday = cal.get(Calendar.DAY_OF_WEEK);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(dayOfWeekToday);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(dateFormat.format(cal.getTime()), result);
    }

    @Test
    void shouldReturnThursdayDateStringOnSunday() {
        testCal.add(Calendar.DATE, -3);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.THURSDAY);
        assertEquals("2019-11-07", result);
    }

    @Test
    void shouldReturnThursdayDateStringOnMonday() {
        testCal.add(Calendar.DATE, -2);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.THURSDAY);
        assertEquals("2019-11-07", result);
    }

    @Test
    void shouldReturnThursdayDateStringOnTuesday() {
        testCal.add(Calendar.DATE, -1);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.THURSDAY);
        assertEquals("2019-11-14", result);
    }

    @Test
    void shouldReturnSundayDateStringOnTuesday() {
        testCal.add(Calendar.DATE, -1);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.SUNDAY);
        assertEquals("2019-11-17", result);
    }

    @Test
    void shouldReturnSundayDateStringOnSaturday() {
        testCal.add(Calendar.DATE, -4);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.SUNDAY);
        assertEquals("2019-11-10", result);
    }

    @Test
    void shouldReturnSundayDateStringOnSunday() {
        testCal.add(Calendar.DATE, -3);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.SUNDAY);
        assertEquals("2019-11-10", result);
    }

    @Test
    void shouldReturnSundayDateStringOnMonday() {
        testCal.add(Calendar.DATE, -2);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.SUNDAY);
        assertEquals("2019-11-10", result);
    }

    @Test
    void shouldReturnThursdayDateStringOnFriday() {
        testCal.add(Calendar.DATE, 2);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.THURSDAY);
        assertEquals("2019-11-14", result);
    }

    @Test
    void shouldReturnMondayDateStringOnTuesday() {
        testCal.add(Calendar.DATE, -1);
        when(dateOperations.getTodaysDate()).thenReturn(testCal);
        String result = dateOperations.getTuesdayAdjustedDateStringForDay(Calendar.MONDAY);
        assertEquals("2019-11-18", result);
    }
}