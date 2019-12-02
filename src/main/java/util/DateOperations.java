package util;

import java.text.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateOperations {

    public String getTodaysDateString() {
        Calendar today = getTodaysDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(today.getTime());
    }

    public String getTuesdayAdjustedDateStringForDay(int dayOfWeekInt) {
        Calendar today = getTodaysDate();
        int dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK);
        int dayOfWeekDiff = adjustToTuesdayStart(dayOfWeekInt) - adjustToTuesdayStart(dayOfWeekToday);
        today.add(Calendar.DATE, dayOfWeekDiff);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(today.getTime());
    }

    public Calendar getTodaysDate() {
        return Calendar.getInstance();
    }

    private int adjustToTuesdayStart(int dayOfWeek) {
        return dayOfWeek > 2 ? dayOfWeek - 2 : dayOfWeek + 5;
    }

    public String getEasternTime(String dateString, String timeZone) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("EEE MM/dd h:mma z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("EST"));
        return format.format(cal.getTime());
    }
}
