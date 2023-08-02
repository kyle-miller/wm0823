package com.example.demo;

import com.example.demo.model.RentalAgreement;
import com.example.demo.model.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RentalAgreementServiceTest {

    @Autowired
    RentalAgreementService rentalAgreementService;

    private static Tool LADW;
    private static Tool CHNS;
    private static Tool JAKD;
    private static Tool JAKR;

    @BeforeEach
    private void setup() {
        LADW = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
        CHNS = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
        JAKD = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
        JAKR = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
    }

    //Test 1
    @Test
    public void checkout_WithInvalidDiscount_ThrowsException() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2015, 9, 03);
        int rentalDays = 5;
        int invalidDiscount = 101;

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalAgreementService.checkout(JAKD, rentalDays, invalidDiscount, checkoutDate);
        });

        //then
        String expectedErrorMessage = "Please enter a valid discount percentage. The discount percentage must be between 0% and 100%.";
        String actualErrorMessage = exception.getMessage();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    //Test 2
    @Test
    public void checkout_LADW_IndependenceDay_NoHolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 3;
        int discountPercent = 10;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 2;
        LocalDate expectedDueDate = LocalDate.of(2020, 7, 5);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(LADW.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(LADW, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Test 3
    @Test
    public void checkout_CHNS_IndependenceDayWeekend_HolidayChargeButNoWeekendCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
        int rentalDays = 5;
        int discountPercent = 25;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 3;
        LocalDate expectedDueDate = LocalDate.of(2015, 7, 7);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(CHNS.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(CHNS, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Test 4
    @Test
    public void checkout_JAKD_LaborDayWeekend_NoWeekendOrHolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
        int rentalDays = 6;
        int discountPercent = 0;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 3;
        LocalDate expectedDueDate = LocalDate.of(2015, 9, 9);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(JAKD.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Test 5
    @Test
    public void checkout_JAKR_IndependenceDayWeekend_NoWeekendOrHolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
        int rentalDays = 9;
        int discountPercent = 0;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 6;
        LocalDate expectedDueDate = LocalDate.of(2015, 7, 11);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(JAKR.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKR, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Test 6
    @Test
    public void checkout_JAKR_IndependenceDayWeekendWithDiscount_NoWeekendOrHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 4;
        int discountPercent = 50;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 1;
        LocalDate expectedDueDate = LocalDate.of(2020, 7, 6);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(JAKR.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKR, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Extra Test 1
    @Test
    public void checkout_WithInvalidRentalDays_ThrowsException() {
        //given
        LocalDate checkoutDate = LocalDate.of(2015, 9, 03);
        int invalidRentalDays = 0;
        int discount = 0;

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalAgreementService.checkout(JAKD, invalidRentalDays, discount, checkoutDate);
        });

        //then
        String expectedErrorMessage = "Please enter a valid rental day count. The rental day count must be 1 or greater.";
        String actualErrorMessage = exception.getMessage();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    //Extra Test 2
    @Test
    public void checkout_CHNS_FreeWeekend_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 8, 5);
        int rentalDays = 2;
        int discountPercent = 10;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedZeroChargeDays = 0;
        LocalDate expectedDueDate = LocalDate.of(2023, 8, 7);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedZeroChargeDays).multiply(BigDecimal.valueOf(CHNS.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(CHNS, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedZeroChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Extra Test 3
    @Test
    public void checkout_JAKD_FreeIndependenceDay_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 7, 4);
        int rentalDays = 1;
        int discountPercent = 10;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedZeroChargeDays = 0;
        LocalDate expectedDueDate = LocalDate.of(2023, 7, 5);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedZeroChargeDays).multiply(BigDecimal.valueOf(JAKD.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedZeroChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Extra Test 4
    @Test
    public void checkout_JAKD_FreeLaborDayWeekend_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 9, 2);
        int rentalDays = 3;
        int discountPercent = 10;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedZeroChargeDays = 0;
        LocalDate expectedDueDate = LocalDate.of(2023, 9, 5);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedZeroChargeDays).multiply(BigDecimal.valueOf(JAKD.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedZeroChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Extra Test 5
    @Test
    public void checkout_LADW_TwoYearRental_LargeFinalCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 730;
        int discountPercent = 25;

        //expected output
        int expectedRentalDays = rentalDays;
        int expectedChargeDays = 726;
        LocalDate expectedDueDate = LocalDate.of(2022, 7, 2);
        BigDecimal expectedPreDiscountCharge = BigDecimal.valueOf(expectedChargeDays).multiply(BigDecimal.valueOf(LADW.getDailyCharge()));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount).setScale(2, RoundingMode.HALF_UP);

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(LADW, rentalDays, discountPercent, checkoutDate);

        //then
        assertEquals(expectedRentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(expectedChargeDays, actualRentalAgreement.getChargeDays());
        assertEquals(expectedDueDate, actualRentalAgreement.getDueDate());
        assertEquals(expectedPreDiscountCharge, actualRentalAgreement.getPreDiscountCharge());
        assertEquals(expectedDiscountAmount, actualRentalAgreement.getDiscountAmount());
        assertEquals(expectedFinalCharge, actualRentalAgreement.getFinalCharge());
    }

    //Extra Test 6
    @Test
    public void calculateChargeDays_CHNS_IndependenceDay_HolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        LocalDate dueDate = LocalDate.of(2020, 7, 4);

        //expected output
        int expectedChargeDays = 2;

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(CHNS, checkoutDate, dueDate);

        //then
        assertEquals(expectedChargeDays, actualChargeDays);
    }

    //Extra Test 6
    @Test
    public void calculateChargeDays_JAKD_IndependenceDay_NoHolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        LocalDate dueDate = LocalDate.of(2020, 7, 4);

        //expected output
        int expectedChargeDays = 1;

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(JAKD, checkoutDate, dueDate);

        //then
        assertEquals(expectedChargeDays, actualChargeDays);
    }

    //Extra Test 7
    @Test
    public void calculateChargeDays_CHNS_LaborDay_HolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2023, 9, 4);
        LocalDate dueDate = LocalDate.of(2023, 9, 8);

        //expected output
        int expectedChargeDays = 4;

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(CHNS, checkoutDate, dueDate);

        //then
        assertEquals(expectedChargeDays, actualChargeDays);
    }

    //Extra Test 8
    @Test
    public void calculateChargeDays_JAKD_LaborDay_NoHolidayCharge() {
        //given input
        LocalDate checkoutDate = LocalDate.of(2023, 9, 4);
        LocalDate dueDate = LocalDate.of(2023, 9, 8);

        //expected output
        int expectedChargeDays = 3;

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(JAKD, checkoutDate, dueDate);

        //then
        assertEquals(expectedChargeDays, actualChargeDays);
    }
}
