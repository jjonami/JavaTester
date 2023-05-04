package kr.co.sampler.crawling.common.util;

import kr.co.sampler.crawling.common.ResultCode;
import kr.co.sampler.crawling.common.model.exception.CustomException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <PRE>
 * 날짜나 시간관련 유틸 클래스
 * <p>
 * 클래스 : DateUtil
 * </PRE>
 */
public class DateUtil {
    protected static Log log = LogFactory.getLog(DateUtil.class);


    /**
     * <PRE>
     * A 기간이 B날짜에 포함되는지 true, false를 리턴한다.
     * <p>
     * 메소드 : isDuplication
     *
     * @param _ASTDATE A 기간(시작날짜)
     * @param _AEDDATE A 기간(끝날짜)
     * @param _BDATE   B 날짜
     * @return </PRE>
     */
    public static boolean isDuplication(String _ASTDATE, String _AEDDATE, String _BDATE) {
        boolean ret = false;
        String[] result = getDiffDays(_ASTDATE, _AEDDATE);

        for (int i = 0; i < result.length; i++) {
            if (result[i].equals(_BDATE)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * <PRE>
     * 두 날짜 사이의 일수를 계산
     * <p>
     * 메소드 : getDiffDayCount
     *
     * @param fromDate
     * @param toDate
     * @return </PRE>
     */
    public static int getDiffDayCount(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try {
            return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getDiffDayCount(String pattern, String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * <PRE>
     * 두 날짜 사이의 분을 계산
     * <p>
     * 메소드 : getDiffMinuteCount
     *
     * @param fromDate
     * @param toDate
     * @return </PRE>
     */
    public static int getDiffMinuteCount(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * <PRE>
     * 두 날짜 사이의 초를 계산
     * <p>
     * 메소드 : getDiffMinuteCount
     *
     * @param fromDate
     * @param toDate
     * @return </PRE>
     */
    public static int getDiffSecondsCount(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * <PRE>
     * 시작일부터 종료일까지 사이의 날짜를 배열에 담아 리턴 ( 시작일과 종료일을 모두 포함한다 )
     * <p>
     * 메소드 : getDiffDays
     *
     * @param fromDate
     * @param toDate
     * @return </PRE>
     */
    public static String[] getDiffDays(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(fromDate));
        } catch (Exception e) {
        }

        int count = getDiffDayCount(fromDate, toDate);

        // 시작일부터
        cal.add(Calendar.DATE, -1);

        // 데이터 저장
        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i <= count; i++) {
            cal.add(Calendar.DATE, 1);

            list.add(sdf.format(cal.getTime()));
        }

        String[] result = new String[list.size()];

        list.toArray(result);

        return result;
    }

    /**
     * <PRE>
     * 오늘날짜를 format 의 형식대로 리턴한다.
     * <p>
     * 메소드 : getToday
     *
     * @param format YYYYMMDD
     * @return </PRE>
     */
    public static String getToday(String format) {
        StringBuffer dateResult = new StringBuffer();
        try {
            Date date = new Date();
            SimpleDateFormat simpleDate = new SimpleDateFormat(format);

            simpleDate.format(date, dateResult, new FieldPosition(1));
        } catch (Exception e) {
            log.error(e);
        }
        return dateResult.toString();
    }

    public static String getUTCToday(String format) {
        StringBuffer dateResult = new StringBuffer();
        try {
            Date date = new Date();
            SimpleDateFormat simpleDate = new SimpleDateFormat(format);
            simpleDate.setTimeZone(TimeZone.getTimeZone("UTC"));
            simpleDate.format(date, dateResult, new FieldPosition(1));
        } catch (Exception e) {
            log.error(e);
        }
        return dateResult.toString();
    }

    /**
     * <PRE>
     * 날짜를 입력하면 구분자(-) 을 붙여서 리턴한다.
     * <p>
     * 메소드 : getDateformat
     *
     * @param date
     * @return </PRE>
     */
    public static String getDateFormat(String date) {
        return getDateFormat(date, "-");
    }

    /**
     * <PRE>
     * 8자리의 날짜값을 token 값에 따라 날짜포맷으로 리턴한다.
     * <p>
     * 메소드 : getDateformat
     *
     * @param date
     * @return </PRE>
     */
    public static String getDateFormat(String date, String token) {
        StringBuffer ret = new StringBuffer();
        try {
            date = date.trim();

            if (date != null && date.length() == 8) {
                ret.append(date.substring(0, 4)).append(token).append(
                        date.substring(4, 6)).append(token).append(
                        date.substring(6));
            } else {
                ret.append("-");
            }
        } catch (Exception e) {
            log.error(e);
        }
        return ret.toString();
    }



    /**
     * <PRE>
     * 입력받은 String 날짜값을  날짜포맷으로 리턴한다.
     * data: SimpleDateFormat 이용 여러형태로 변환 가능
     * <p>
     * 메소드 : getStringDateFormat
     *
     * @param date
     * @return </PRE>
     */
    public static String getStringDateFormat(String date, String format) {
        String dateResult = new String();
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format, Locale.KOREA);

            Date getDate = dt.parse(date);
            dateResult = dt.format(getDate);
        } catch (Exception e) {
            log.error(e);
        }
        return dateResult.toString();
    }

    public static String getStringDateNonLocaleUTCFormat(String date, String format) {
        String dateResult = new String();
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format);
            dt.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date getDate = dt.parse(date);
            dateResult = dt.format(getDate);
        } catch (Exception e) {
            log.error(e);
        }
        return dateResult.toString();
    }

    public static String getStringDateNonLocaleFormat(String date, String format) {
        String dateResult = new String();
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format);
            Date getDate = dt.parse(date);
            dateResult = dt.format(getDate);
        } catch (Exception e) {
            log.error(e);
        }
        return dateResult.toString();
    }

    public static Date getStringToDate(String date, String format) throws CustomException {
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format);
            Date getDate = dt.parse(date);
            return getDate;
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(ResultCode.CODE_500);
        }
    }

    /**
     * <PRE>
     * 6자리의 날짜값을 token 값에 따라 날짜포맷으로 리턴한다.
     * <p>
     * 메소드 : getMonthformat
     *
     * @param date
     * @return </PRE>
     */
    public static String getMonthFormat(String date, String token) {
        StringBuffer ret = new StringBuffer();
        try {
            date = date.trim();

            if (date != null && date.length() == 6) {
                ret.append(date.substring(0, 4)).append(token).append(
                        date.substring(4, 6));
            } else {
                ret.append("-");
            }
        } catch (Exception e) {
            log.error(e);
        }
        return ret.toString();
    }


    /**
     * <PRE>
     * 6자리의 시간값(150801)을 시간 포맷(15:08:01)으로 리턴한다.
     * 시간이 6자리보다 짧으면 입력한 시간값(strTime)을 그대로 리턴한다.
     * <p>
     * 메소드 : getTimeFormat
     *
     * @param strTime
     * @return </PRE>
     */
    public static String getTimeFormat(String strTime, String token) {
        StringBuilder strResult = new StringBuilder();
        try {
            if (strTime != null && strTime.trim().length() >= 6) {
                strTime = strTime.trim();
                strResult.append(strTime.substring(0, 2));
                strResult.append(token);
                strResult.append(strTime.substring(2, 4));
                strResult.append(token);
                strResult.append(strTime.substring(4, 6));
            } else if (strTime != null && strTime.trim().length() == 4) {
                strTime = strTime.trim();
                strResult.append(strTime.substring(0, 2));
                strResult.append(token);
                strResult.append(strTime.substring(2, 4));
            } else {

                strResult.append(strTime);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return strResult.toString();
    }

    /**
     * <PRE>
     * 입력한 날짜에 달수을 더한 날짜를 리턴한다.
     * <p>
     * 메소드 : addDateByMonth
     *
     * @param date
     * @param month
     * @return </PRE>
     */
    public static Date addDateByMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.add(Calendar.MONTH, month);
        } catch (Exception e) {
            log.error(e);
        }
        return cal.getTime();
    }


    /**
     * <PRE>
     * 입력한 날짜에 일수를 더한 날짜를 리턴한다.
     * <p>
     * 메소드 : addDateByDay
     *
     * @param date
     * @param day
     * @return </PRE>
     */
    public static Date addDateByDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, day);
        } catch (Exception e) {
            log.error(e);
        }
        return cal.getTime();
    }

    /**
     * <PRE>
     * 입력한 시간에 입력한 시간을 더한 시간을 리턴한다.
     * <p>
     * 메소드 : addTimeByHour
     *
     * @param date
     * @param hour
     * @return </PRE>
     */
    public static Date addTimeByHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, hour);
        } catch (Exception e) {
            log.error(e);
        }
        return cal.getTime();
    }

    /**
     * <PRE>
     * 입력한 시간에 입력한 분을 더한 시간을 리턴한다.
     * <p>
     * 메소드 : addTimeByMinute
     *
     * @param date
     * @param minute
     * @return </PRE>
     */
    public static Date addTimeByMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minute);
        } catch (Exception e) {
            log.error(e);
        }
        return cal.getTime();
    }

    /**
     * <PRE>
     * 입력한 월에 달수을 더한 월을 리턴한다.
     * <p>
     * 메소드 : addMonthByMonth
     *
     * @param yearMonth
     * @param minVal
     * @return </PRE>
     */
    public static String addMonthByMonth(String yearMonth, int minVal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        String retStr = "";
        try {
            int year = Integer.parseInt(yearMonth.substring(0, 4));
            int month = Integer.parseInt(yearMonth.substring(4, 6));

            cal.add(month, minVal);
            cal.set(year, month - 1, 0);

            String beforeYear = dateFormat.format(cal.getTime()).substring(0, 4);
            String beforeMonth = dateFormat.format(cal.getTime()).substring(4, 6);
            retStr = beforeYear + beforeMonth;

        } catch (Exception e) {
            log.error(e);
        }

        return retStr;
    }

    /**
     * <PRE>
     * 해당년월의 마지막 날짜를 구한다.
     * <p>
     * 메소드 : getLastDayOfMonth
     *
     * @param yearMonth
     * @return </PRE>
     */
    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, 6));

        Calendar cal = Calendar.getInstance();
        String lastDay = "";
        try {

            cal.set(year, (month - 1), 1);

            lastDay = String.valueOf(cal.getActualMaximum(Calendar.DATE));

        } catch (Exception e) {
            log.error(e);
        }

        return lastDay;
    }

    /**
     * <PRE>
     * Date 타입의 date 를 기본포맷인 yyyy-MM-dd로
     * 포맷팅하여 리턴한다.
     * <p>
     * 메소드 : getStringFromDate
     *
     * @param date
     * @return </PRE>
     */
    public static String getStringFromDate(Date date) {
        return getStringFromDate(date, "yyyy-MM-dd");
    }

    /**
     * <PRE>
     * Date  타입의 date 를 포맷문자열(format) 형식의 문자열로 리턴한다.
     * <p>
     * 메소드 : getStringFromDate
     *
     * @param date
     * @param format
     * @return </PRE>
     */
    public static String getStringFromDate(Date date, String format) {
        StringBuffer dateResult = new StringBuffer();
        SimpleDateFormat simpleDate = new SimpleDateFormat(format);
        simpleDate.format(date, dateResult, new FieldPosition(1));
        return dateResult.toString();
    }

    /**
     * <PRE>
     * 요일알기
     * <p>
     * 메소드 : getWeekDay
     *
     * @param strDate
     * @param
     * @return </PRE>
     */
    public int getWeekDay(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(strDate));
        } catch (Exception e) {
        }

        int dayweek = cal.get(Calendar.DAY_OF_WEEK);

        return dayweek;
    }

    public int getWeekDay(Date strDate) {

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(strDate);
        } catch (Exception e) {
        }

        int dayweek = cal.get(Calendar.DAY_OF_WEEK);

        return dayweek;
    }

    /**
     * System의 현재 Date 정보를 yyyy-MM-dd HH:mm String으로 Return 한다.
     *
     * @return
     */
    public static String getStringNowDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(date));

        return today;
    }

    /**
     * System의 현재 Date 정보를 변환할 Format Parameter 형태를 받아 String으로 Return 한다.
     *
     * @return
     */
    public static String getStringNowDateTime(String str) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = (new SimpleDateFormat(str).format(date));

        return today;
    }

    /**
     * <PRE>
     * 테스트용 메소드
     * <p>
     * 메소드 : testCase
     *
     * </PRE>
     */
    public static void testCase() {
        // 오늘날짜 출력
        Date date = new Date();
        System.out.println(date);
        System.out.println(getStringFromDate(new Date(), "hh:mm"));

        // 일주일전 날짜
        date = addDateByDay(date, -7);
        System.out.println(getStringFromDate(date, "yyyyMMdd"));

        // 한달전 날짜
        date = addDateByMonth(date, -1);
        System.out.println(getStringFromDate(date, "yyyyMMdd"));

        System.out.println(getTimeFormat("1234", ":"));

        System.out.println(DateUtil.getToday("HH:mm:ss"));

        DateUtil dtut = new DateUtil();

    }

    public static String getStringNowTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = (new SimpleDateFormat("HHmmss").format(date));

        return today;
    }

    public static String getStringBeforeTime(int beforeTime) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, beforeTime);
        String time = (new SimpleDateFormat("HHmmss").format(calendar.getTime()));
        return time;
    }

    public static String getStringBeforeDate(int beforeDate) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, beforeDate);
        String today = (new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()));
        return today;
    }

    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String start = "2106-12-31 23:59:59";
        String dateString = "Tue, 16 Mar 2021 08:53:46 UTC+0900 (KST)";
//        String end = "2022-02-03  01:00:00";

//        int a = DateUtil.getDiffDayCount(pattern, start, end);
        String getExdate = getExdate();
//        System.out.println("getExdate : " + getExdate);
        String getExdate1 = getExdate(start, pattern);
//        System.out.println("getExdate1 :: " + getExdate1);
//        System.out.println("same ? :: " + (getExdate.equals(getExdate1)));
    }

    /**
     * 매개변수로 일자를 받아 EEE, d MMM yyyy HH:mm:ss Z 형태의 String 날짜로 변환하여 리턴한다.
     *
     * @return
     */
    public static String getDayGMTStringReturn(int day) {
        Date date = new Date();
        String rtnStrDate = "";

        Date newDate = new Date(date.getTime() + ((long) 1000 * 60 * 60 * 24 * (day)));

        String ISO_FORMAT = "EEE, d MMM yyyy HH:mm:ss zzz";
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        rtnStrDate = sdf.format(newDate);

        return rtnStrDate;
    }

    public static String getExdate() {
        try {
            String dateString = "Mon, 31 Dec 2100 12:59:59 UTC+0900 (KST)";
            String format = "EEE, d MMM yyyy HH:mm:ss zZ (z)";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,Locale.US);
            Date date = dateFormat.parse(dateString);
//            System.out.println("date :: " + date);
            long unixTime = (long)date.getTime()/1000;
//            System.out.println(unixTime);
            String hex = Long.toHexString(unixTime);
//            System.out.println("hex :: " + hex);
//            System.out.println("hex :: " + hex.length());
            return hex.toUpperCase();
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public static String getExdate(String dateStr, String format) {
        try {
//            System.out.println("dateStr :: " + dateStr);
//            System.out.println("format :: " + format);
//            String dateString = "Tue, 16 Mar 2021 08:53:46 UTC+0900 (KST)";
//            String format = "EEE, d MMM yyyy HH:mm:ss zZ (z)";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,Locale.US);
            Date date = dateFormat.parse(dateStr);
            System.out.println("date :: " + date);
            long unixTime = (long)date.getTime()/1000;
//            System.out.println(unixTime);
            String hex = Long.toHexString(unixTime);
            return hex.toUpperCase();
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public static String[] getDiffMonths(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> monthList = new ArrayList<>();
        try {
            Date start = sdf.parse(fromDate);
            Date end = sdf.parse(toDate);

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(start);
            int startMon = calendar.get(Calendar.MONTH);

            calendar.setTime(end);
            int endMon = calendar.get(Calendar.MONTH);

            int monthDiff = endMon - startMon;

            calendar.setTime(start);
            for (int i = 0; i <= monthDiff; i++) {
                monthList.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
        }
        String[] result = new String[monthList.size()];
        monthList.toArray(result);

        return result;
    }

    public static Date getTodayToDate(String formatStr) throws CustomException {
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            Date today = format.parse(format.format(new Date()));
            return today;
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(ResultCode.CODE_500);
        }
    }
}
