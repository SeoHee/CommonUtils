package kyowon.co.kr.lib.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String FORMAT_YMD = "yyyyMMdd";

    public static String getToday(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date d = new Date(System.currentTimeMillis());

        return formatter.format(d);
    }

    public static boolean isAfterTime(int hourOfDay) {
        Calendar currentC = Calendar.getInstance();
        int hour = currentC.get(Calendar.HOUR_OF_DAY);

        if (hour >= hourOfDay) {
            return true;
        } else {
            return false;
        }
    }

    public static Calendar getBeforeNdateFromToday(int date) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, date * -1);
        return c;
    }

    public static String convertMillisToFormat(String format, long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date d = new Date(milliSeconds);
        return formatter.format(d);
    }

    /**
     * 데이터 포맷 변형 후 반환
     *
     * @param value               변형 전 데이터 (1988.01.05)
     * @param orgFormatPattern    변형 전 데이터 포맷 (yyyy.MM.dd)
     * @param resultFormatPattern 변형 후 데이터 포맷 (MM.dd)
     * @return 변경 후 데이터 반환 (01.05)
     */
    public static String convertDateFormat(String value, String orgFormatPattern, String resultFormatPattern) {
        SimpleDateFormat orgDateFormat = new SimpleDateFormat(orgFormatPattern);
        SimpleDateFormat convertFormat = new SimpleDateFormat(resultFormatPattern, Locale.KOREAN);

        String resultDate = "";
        try {
            Date orgDate = orgDateFormat.parse(value);

            String convertStartString = convertFormat.format(orgDate);
            resultDate = convertStartString;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static long convertTimeToMillis(String formatDate, String format) {
        long ret = 0;

        try {
            if (TextUtils.isEmpty(formatDate)) {
                return ret;
            }

            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
            Date date = (Date) formatter.parse(formatDate);
            ret = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
