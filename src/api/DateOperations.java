package api;

import java.text.*;
import java.util.Calendar;

class DateOperations {

    String getFanduelDateString(int dayOfWeekInt) {
        Calendar today = getTodaysDate();
        int dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK);
        int dayOfWeekDiff = adjustToTuesdayStart(dayOfWeekInt) - adjustToTuesdayStart(dayOfWeekToday);
        today.add(Calendar.DATE, dayOfWeekDiff);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(today.getTime());
    }

    Calendar getTodaysDate() {
        return Calendar.getInstance();
    }

    private int adjustToTuesdayStart(int dayOfWeek) {
        return dayOfWeek > 2 ? dayOfWeek - 2 : dayOfWeek + 5;
    }
}
