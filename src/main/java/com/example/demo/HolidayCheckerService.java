package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class HolidayCheckerService {
    public static boolean checkForIndependenceDay(LocalDate date) {
        int july = 7;
        int independenceDay = 4;
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        //easy check (note: If July 4th on Saturday or Sunday, it is not the observed holiday)
        if(date.getMonthValue() == july && date.getDayOfMonth() == independenceDay && dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            return true;
        }

        //is holiday on weekend check
        if (date.getMonthValue() == july && (date.getDayOfMonth() == 3 || date.getDayOfMonth() == 5)) {
            if (date.getDayOfMonth() == 3 && dayOfWeek == DayOfWeek.FRIDAY) {
                return true;
            } else if (date.getDayOfMonth() == 5 && dayOfWeek == DayOfWeek.MONDAY) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkForLaborDay(LocalDate date) {
        int september = 9;

        if(date.getMonthValue() == september) {
            if(date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7) {
                return true;
            }
        }

        return false;
    }

}
