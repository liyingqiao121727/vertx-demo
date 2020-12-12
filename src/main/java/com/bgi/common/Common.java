package com.bgi.common;

import java.time.LocalDate;

public class Common {

    /**
     * 如果考勤日期小于本月配置日期，就判断考勤日期是否在上个月初到本月配置日期之间
     * 如果考勤日期不小于本月配置日期，就判断考勤日期是否在本月初到当前日期之间
     *
     * @param configDays 本月配置日期
     * @param checkDate 考勤日期
     * @return
     */
    public static boolean checkWorkAttendance(int configDays, LocalDate checkDate) {
        LocalDate now = LocalDate.now();
        //LocalDate thisMonth = now.withDayOfMonth(1).minusDays(1L);
        //LocalDate lastMonth = now.minusMonths(1L).withDayOfMonth(1).minusDays(1L);
        //LocalDate currentMonth = now.withDayOfMonth(configDays);
        LocalDate lastMonth = now.withDayOfMonth(1).minusDays(1L);
        LocalDate currentMonth = now.plusMonths(1L).withDayOfMonth(configDays);

        /*return checkDate.isBefore(currentMonth) ?
                checkDate.isBefore(currentMonth) && checkDate.isAfter(lastMonth) :
                checkDate.isBefore(now.plusDays(1L)) && checkDate.isAfter(thisMonth);*/
        return checkDate.isBefore(currentMonth) && checkDate.isAfter(lastMonth);
    }

}
