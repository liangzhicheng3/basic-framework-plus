package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.exception.TransactionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * @author liangzhicheng
 */
public class TimeUtil {

    //日期转换格式数组
    public static String[][] regularExp = new String[][]{
            //默认格式
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))\\s+([0,1]\\d|[2][0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)",
                    Constants.DATE_TIME_PATTERN},
            //日期格式（年月日）
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))",
                    Constants.DATE_PATTERN},
            //毫秒格式
            {"\\d{4}((([0][1,3-9]|[1][0-2]|[1-9])([0-2]\\d|[3][0,1]|[1-9]))|((02|2)(([1-9])|[0-2]\\d)))([0,1]\\d|[2][0-3])([0-5]\\d|\\d)([0-5]\\d|\\d)\\d{1,3}",
                    Constants.TIMESTAMP_PATTERN}};

    /**
     * Date日期格式转化成String字符串，使用默认格式
     * @param date
     * @return String
     */
    public static String format(Date date) {
        return format(date, Constants.DATE_TIME_PATTERN);
    }

    /**
     * Date日期格式化成String字符串，使用自定义格式
     * @param date
     * @param pattern
     * @return String
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 时间戳格式转化成String字符串，使用自定义格式
     * @param timestamp
     * @param pattern
     * @return String
     */
    public static String format(Long timestamp, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if(timestamp.toString().length() < 13){
            timestamp = timestamp * 1000L;
        }
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * String字符串格式转化成Date日期，使用（年月日）格式
     * @param date
     * @return Date
     */
    public static Date parse(String date) {
        return parse(date, Constants.DATE_PATTERN);
    }

    /**
     * String字符串格式转化成Date日期，使用自定义格式
     * @param date
     * @param pattern
     * @return Date
     */
    public static Date parse(String date, String pattern) {
        Date dateAfter = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try{
            dateAfter = sdf.parse(date);
        }catch(ParseException e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return dateAfter;
    }

    /**
     * String字符串格式转化成时间戳格式
     * @param date
     * @return Long
     */
    public static Long parseTime(String date) {
        return parseTimeByExp(date).getTime() / 1000;
    }

    /**
     * 根据时间格式将String字符串格式转化成时间戳格式
     * @param date
     * @return Date
     */
    public static Date parseTimeByExp(String date) {
        try{
            String format = getFormat(date);
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return new Date((sf.parse(date).getTime()));
        }catch(Exception e){
            throw new TransactionException("日期格式化发生异常！");
        }
    }

    /**
     * 获取时间格式
     * @param date
     * @return String
     */
    public static String getFormat(String date) {
        String format = null;
        if(ToolUtil.isBlank(date)){
            return null;
        }
        boolean flag = false;
        for(int i = 0; i < regularExp.length; i++){
            flag = date.matches(regularExp[i][0]);
            if(flag){
                format = regularExp[i][1];
            }
        }
        if(ToolUtil.isBlank(format)){
            PrintUtil.info("[时间格式化] date：{}", date);
            throw new TransactionException("未识别的日期格式！");
        }
        return format;
    }

//    public static void main(String[] args) {
//        long time = new Date().getTime();
//        System.out.println(getFormat(time + ""));
//        System.out.println(parseTime("2021-12-29 09:30:00"));
//    }

    /**
     * Date日期格式转化成LocalDateTime格式
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        LocalDateTime localDateTime = null;
        if(date != null){
            try{
                Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                localDateTime = instant.atZone(zoneId).toLocalDateTime();
            }catch(Exception e){
                throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
            }
        }
        return localDateTime;
    }

    /**
     * LocalDateTime格式转化成Date日期格式
     * @param localDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        Date date = null;
        try{
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = localDateTime.atZone(zoneId);
            date = Date.from(zdt.toInstant());
        }catch(Exception e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return date;
    }

    /**
     * String字符串格式转化成LocalDateTime格式
     * @param date
     * @param format
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String date, String format) {
        if(ToolUtil.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        LocalDateTime localDateTime = null;
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            localDateTime = LocalDateTime.parse(date, dtf);
        }catch(Exception e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return localDateTime;
    }

    /**
     * LocalDateTime格式转化成String字符串格式
     * @param localDateTime
     * @param format
     * @return String
     */
    public static String toString(LocalDateTime localDateTime, String format) {
        if(ToolUtil.isBlank(format)){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        String dateStr = "";
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            dateStr = localDateTime.format(dtf);
        }catch(Exception e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return dateStr;
    }

    /**
     * String字符串格式转化成LocalDate格式
     * @param date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(String date){
        DateTimeFormatter dtf = null;
        LocalDate localDate = null;
        try{
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(date, dtf);
        }catch(Exception e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return localDate;
    }

    /**
     * String字符串格式转化成LocalTime格式
     * @param date
     * @param format
     * @return LocalTime
     */
    public static LocalTime toLocalTime(String date, String format) {
        if (ToolUtil.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter dtf = null;
        LocalTime localTime = null;
        try{
            dtf = DateTimeFormatter.ofPattern(format);
            localTime = LocalTime.parse(date, dtf);
        }catch(Exception e){
            throw new TransactionException(ApiConstant.PARAM_DATE_ERROR);
        }
        return localTime;
    }

    /**
     * 返回日期参数加value天后的Date日期
     * @param date
     * @param value
     * @return Date
     */
    public static Date dateAdd(Date date, int value){
        if(date == null || value < 1){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, value);
        return calendar.getTime();
    }

    /**
     * 返回日期参数减value天后的Date日期
     * @param date
     * @param value
     * @return Date
     */
    public static Date dateSub(Date date, int value){
        if(date == null || value < 1){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 0 - value);
        return calendar.getTime();
    }

    /**
     * 返回日期参数加value天后的LocalDateTime日期
     * @param localDateTime
     * @param value
     * @param type
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTimeAdd(LocalDateTime localDateTime, int value, String type){
        if (localDateTime == null || value < 1) {
            return null;
        }
        LocalDateTime time = null;
        if(ToolUtil.in(type, "days", "months")){
            if("days".equals(type)){
//                LocalDateTime time = localDateTime.plus(value, ChronoUnit.DAYS);
                time = localDateTime.plusDays(value);
            }
            if("months".equals(type)){
//                LocalDateTime time = localDateTime.plus(value, ChronoUnit.MONTHS);
                time = localDateTime.plusMonths(value);
            }
        }
        return time;
    }

    /**
     * 返回日期参数减value天后的LocalDateTime日期
     * @param localDateTime
     * @param value
     * @param type
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTimeSub(LocalDateTime localDateTime, int value, String type){
        if (localDateTime == null || value < 1) {
            return null;
        }
        LocalDateTime time = null;
        if(ToolUtil.in(type, "days", "months")){
            if("days".equals(type)){
                time = localDateTime.minusDays(value);
            }
            if("months".equals(type)){
                time = localDateTime.minusMonths(value);
            }
        }
        return time;
    }

    /**
     * 计算两个Date日期参数之间相差多少天
     * @param min
     * @param max
     * @return int
     */
    public static int daySubBoth(Date min, Date max){
        if(min == null || max == null || min.getTime() > max.getTime()){
            return 0;
        }
        return (int)((max.getTime() - min.getTime()) / 1000 / 3600 / 24);
    }

    /**
     * 返回年份参数加value后的Date日期
     * @param date
     * @param value
     * @return Date
     */
    public static Date yearAdd(Date date, int value){
        if(date == null || value < 1){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + value);
        return calendar.getTime();
    }

    /**
     * 返回年份参数减value后的Date日期
     * @param date
     * @param value
     * @return Date
     */
    public static Date yearSub(Date date, int value){
        if(date == null || value < 1){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - value);
        return calendar.getTime();
    }

    /**
     * 返回两个日期相减，date1 - date2的年份差
     * @param date1
     * @param date2
     * @return int
     */
    public static int yearSubBoth(Date date1, Date date2){
        if(date1 != null && date2 != null){
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            return (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));
        }
        return 0;
    }

    /**
     * 返回日期参数加value天后的Date日期(不要星期天和星期天一)
     * @param date
     * @param value
     * @return Date
     */
    public static Date dateAddNot7And1(Date date, int value){
        if(date == null || value < 1){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i = value; i > 0; i--){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            while(calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1){
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return calendar.getTime();
    }

    /**
     * 获取Date日期参数对应星期几参数
     * @param date
     * @return Date
     */
    public static Date getWeekByDate(Date date){
        if(date == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 1){//星期天
            return dateSub(date, 6);
        }
        if(day == 2){//星期一
            return date;
        }
        if(day == 3){//星期二
            return dateSub(date, 1);
        }
        if(day == 4){//星期三
            return dateSub(date, 2);
        }
        if(day == 5){//星期四
            return dateSub(date, 3);
        }
        if(day == 6){//星期五
            return dateSub(date, 4);
        }
        if(day == 7){//星期六
            return dateSub(date, 5);
        }
        return date;
    }

    /**
     * 获取Date日期格式当周星期一至星期天的日期
     * @param date
     * @return List
     */
    public static List<Date> getWeekList(Date date){
        if(date == null){
            return null;
        }
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 1){//星期天
            Date start = dateSub(date, 6);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 2){//星期一
            Date start = date;
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 3){//星期二
            Date start = dateSub(date, 1);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 4){//星期三
            Date start = dateSub(date, 2);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 5){//星期四
            Date start = dateSub(date, 3);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 6){//星期五
            Date start = dateSub(date, 4);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        if(day == 7){//星期六
            Date start = dateSub(date, 5);
            list.add(start);
            for(int i = 0; i < 6; i++){
                start = dateAdd(start, 1);
                list.add(start);
            }
        }
        return list;
    }

    /**
     * 获取Date日期格式当前月份的第一天
     * @param date
     * @return Date
     */
    public static Date getMonthFirst(Date date) {
        if(date == null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取Date日期格式当前月份的最后一天
     * @param date
     * @return Date
     */
    public static Date getMonthLast(Date date) {
        if(date == null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取LocalDateTime当前时间毫秒数
     * @return long
     */
    public static long getEpochMilliByCurrentTime(){
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 判断两个Date日期参数是否是同一天
     * @param date1
     * @param date2
     * @return boolean
     */
    public static boolean isSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if(cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)){
            return false;
        }
        if(cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)){
            return false;
        }
        if(cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)){
            return false;
        }
        return true;
    }

    /**
     * 两个时间相比较，返回布尔值
     * @param time1
     * @param time2
     * @return boolean
     */
    public static boolean localDateTimeGT(LocalDateTime time1, LocalDateTime time2){
        if(time1 == null || time2 == null){
            return false;
        }
        long currentTime = time1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endTime = time2.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if(currentTime > endTime){
            return true;
        }
        return false;
    }

}
