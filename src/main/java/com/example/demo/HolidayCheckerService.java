package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class HolidayCheckerService {
    public static boolean isIndependenceDay(LocalDate date) {
        boolean isJuly = date.getMonthValue() == 7;
        boolean is3rd = date.getDayOfMonth() == 3;
        boolean is4th = date.getDayOfMonth() == 4;
        boolean is5th = date.getDayOfMonth() == 5;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isFriday = dayOfWeek == DayOfWeek.FRIDAY;
        boolean isNotWeekend = dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
        boolean isMonday = dayOfWeek == DayOfWeek.MONDAY;

        return isJuly && ((is3rd && isFriday) || (is4th && isNotWeekend) || (is5th && isMonday));
    }

    public static boolean isLaborDay(LocalDate date) {
        boolean isSeptember = date.getMonthValue() == 9;
        const isMonday = date.getDayOfWeek() == DayOfWeek.MONDAY;
        const isFirstMonday = isMonday && date.getDayOfMonth() <= 7;

        return isSeptember && isFirstMonday;
    }

}
