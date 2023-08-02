package com.example.demo;

import com.example.demo.model.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HolidayCheckerServiceTest {

    @Autowired
    HolidayCheckerService holidayServiceChecker;

    @Test
    public void checkForIndependenceDay_JulyFourth_IsHoliday() {
        //given
        LocalDate julyFourth = LocalDate.of(2023, 7, 4);

        //when
        boolean actualResult = holidayServiceChecker.checkForIndependenceDay(julyFourth);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void checkForIndependenceDay_JulyFifth_IsHoliday() {
        //given
        LocalDate julyDate = LocalDate.of(2021, 7, 5);

        //when
        boolean actualResult = holidayServiceChecker.checkForIndependenceDay(julyDate);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void checkForIndependenceDay_JulyThird_IsHoliday() {
        //given
        LocalDate julyDate = LocalDate.of(2020, 7, 3);

        //when
        boolean actualResult = holidayServiceChecker.checkForIndependenceDay(julyDate);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void checkForIndependenceDay_MondayJulyThird_IsNotHoliday() {
        //given
        LocalDate julyDate = LocalDate.of(2023, 7, 3);

        //when
        boolean actualResult = holidayServiceChecker.checkForIndependenceDay(julyDate);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void checkForIndependenceDay_WednesdayJulyFifth_IsNotHoliday() {
        //given
        LocalDate julyDate = LocalDate.of(2023, 7, 5);

        //when
        boolean actualResult = holidayServiceChecker.checkForIndependenceDay(julyDate);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void checkForLaborDay_GivenLaborDay_IsHoliday() {
        //given
        LocalDate laborDay2023 = LocalDate.of(2023, 9, 4);
        LocalDate laborDay2022 = LocalDate.of(2022, 9, 5);
        LocalDate laborDay2021 = LocalDate.of(2021, 9, 6);
        LocalDate laborDay2020 = LocalDate.of(2020, 9, 7);
        LocalDate laborDay2019 = LocalDate.of(2019, 9, 2);
        LocalDate laborDay2018 = LocalDate.of(2018, 9, 3);
        LocalDate laborDay2017 = LocalDate.of(2017, 9, 4);
        LocalDate laborDay2016 = LocalDate.of(2016, 9, 5);
        LocalDate laborDay2015 = LocalDate.of(2015, 9, 7);

        //when
        boolean actual2023 = holidayServiceChecker.checkForLaborDay(laborDay2023);
        boolean actual2022 = holidayServiceChecker.checkForLaborDay(laborDay2022);
        boolean actual2021 = holidayServiceChecker.checkForLaborDay(laborDay2021);
        boolean actual2020 = holidayServiceChecker.checkForLaborDay(laborDay2020);
        boolean actual2019 = holidayServiceChecker.checkForLaborDay(laborDay2019);
        boolean actual2018 = holidayServiceChecker.checkForLaborDay(laborDay2018);
        boolean actual2017 = holidayServiceChecker.checkForLaborDay(laborDay2017);
        boolean actual2016 = holidayServiceChecker.checkForLaborDay(laborDay2016);
        boolean actual2015 = holidayServiceChecker.checkForLaborDay(laborDay2015);

        //then
        assertTrue(actual2023);
        assertTrue(actual2022);
        assertTrue(actual2021);
        assertTrue(actual2020);
        assertTrue(actual2019);
        assertTrue(actual2018);
        assertTrue(actual2017);
        assertTrue(actual2016);
        assertTrue(actual2015);
    }

    @Test
    public void checkForLaborDay_GivenRandomSeptDate_IsNotHoliday() {
        //given
        LocalDate dayAfterLaborDay2023 = LocalDate.of(2023, 9, 5);
        LocalDate dayAfterLaborDay2022 = LocalDate.of(2022, 9, 6);
        LocalDate dayBeforeLaborDay2023 = LocalDate.of(2023, 9, 3);
        LocalDate dayBeforeLaborDay2022 = LocalDate.of(2022, 9, 3);

        //when
        boolean actualAfter2023 = holidayServiceChecker.checkForLaborDay(dayAfterLaborDay2023);
        boolean actualAfter2022 = holidayServiceChecker.checkForLaborDay(dayAfterLaborDay2022);
        boolean actualBefore2023 = holidayServiceChecker.checkForLaborDay(dayBeforeLaborDay2023);
        boolean actualBefore2022 = holidayServiceChecker.checkForLaborDay(dayBeforeLaborDay2022);

        //then
        assertFalse(actualAfter2023);
        assertFalse(actualAfter2022);
        assertFalse(actualBefore2023);
        assertFalse(actualBefore2022);

    }

}
