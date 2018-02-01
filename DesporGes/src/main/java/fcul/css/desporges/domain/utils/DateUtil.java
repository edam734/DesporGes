package fcul.css.desporges.domain.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fcul.css.desporges.domain.Periodicity;

/**
 * A utilities class for dates. 
 * 
 * It is based on java.util.Calendar which was found to have some of the required
 * functionalities over dates.
 * 
 * There is a new date and time library in java 8 that could be used instead
 * but since eclipselink was raising problems with java8 new features the decision
 * was to keep usng Calendar
 * 
 *   http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html
 * 
 *
 * @author Thibault Langlois
 */
public class DateUtil {

    private static Calendar referenceDate = Calendar.getInstance();

    public static Calendar makeDate(int day, int month, int year) {
        Calendar c = (Calendar) referenceDate.clone();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        return c;
    }

    /**
     * A utility for printing dates.
     *
     * @param date
     * @return
     */
    public static String myFormat(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date.getTime());
    }

    /**
     * Creates a date equal to d + periodicity.
     *
     * @param d
     * @param periodicity
     * @return
     */
    public static Calendar add(Calendar d, Periodicity periodicity) {
        Calendar date = (Calendar) d.clone();
        switch (periodicity) {
            case WEEKLY:
                date.add(Calendar.HOUR, 24 * 7);
                break;
            case MONTHLY:
                date.add(Calendar.MONTH, 1);
                break;
            case SEMESTERLY:
                date.add(Calendar.MONTH, 6);
                break;
        }
        return date;
    }

    /**
     * Creates a date equal to d + periodicity * n.
     *
     * @param d
     * @param periodicity
     * @return
     *
     * @requires n > 0
     */
    public static Calendar add(Calendar d, Periodicity periodicity, int n) {
        Calendar date = (Calendar) d.clone();
        switch (periodicity) {
            case WEEKLY:
                date.add(Calendar.HOUR, 24 * 7 * n);
                break;
            case MONTHLY:
                date.add(Calendar.MONTH, 1 * n);
                break;
            case SEMESTERLY:
                date.add(Calendar.MONTH, 6 * n);
                break;
        }
        return date;
    }

    public static boolean isSaturdayOrSunday(Calendar c) {
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY;
    }

}
