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
        //given
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
    public void checkout_LADWIndependenceDay_NoHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 3;
        int discount = 10;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(LADW, rentalDays, discount, checkoutDate);

        //then
        assertEquals(2, actualRentalAgreement.getChargeDays());
        assertNotEquals(rentalDays, actualRentalAgreement.getChargeDays());
        assertEquals(3, actualRentalAgreement.getRentalDays());
        assertEquals(BigDecimal.valueOf(0.40).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Test 3
    @Test
    public void checkout_CHNSIndependenceDayWeekend_HolidayChargeButNoWeekendCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
        int rentalDays = 5;
        int discount = 25;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(CHNS, rentalDays, discount, checkoutDate);

        //then
        assertEquals(3, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(BigDecimal.valueOf(1.12).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Test 4
    @Test
    public void checkout_JAKDLaborDayWeekend_NoWeekendOrHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
        int rentalDays = 6;
        int discount = 0;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discount, checkoutDate);

        //then
        assertEquals(3, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays()); //maybe don't need
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Test 5
    @Test
    public void checkout_JAKRIndependenceDayWeekend_NoWeekendOrHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
        int rentalDays = 9;
        int discount = 0;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKR, rentalDays, discount, checkoutDate);

        //then
        assertEquals(6, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays()); //maybe don't need
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Test 6
    @Test
    public void checkout_JAKRIndependenceDayWeekendWithDiscount_NoWeekendOrHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 4;
        int discount = 50;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKR, rentalDays, discount, checkoutDate);

        //then
        assertEquals(1, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays()); //maybe don't need
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
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
    public void checkout_CHNSFreeWeekend_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 8, 5);
        int rentalDays = 2;
        int discount = 10;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(CHNS, rentalDays, discount, checkoutDate);

        //then
        assertEquals(0, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays());
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Extra Test 3
    @Test
    public void checkout_JAKDFreeIndependenceDay_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 7, 4);
        int rentalDays = 1;
        int discount = 10;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discount, checkoutDate);

        //then
        assertEquals(0, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays()); //maybe don't need
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Extra Test 4
    @Test
    public void checkout_JAKDFreeLaborDayWeekend_ZeroCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 9, 2);
        int rentalDays = 3;
        int discount = 10;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(JAKD, rentalDays, discount, checkoutDate);

        //then
        assertEquals(0, actualRentalAgreement.getChargeDays());
        assertEquals(rentalDays, actualRentalAgreement.getRentalDays()); //maybe don't need
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), actualRentalAgreement.getDiscountAmount());
    }

    //Extra Test 5
    @Test
    public void checkout_LADWTwoYearRental_LargeFinalCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        int rentalDays = 730;
        int discount = 25;

        //when
        RentalAgreement actualRentalAgreement = rentalAgreementService.checkout(LADW, rentalDays, discount, checkoutDate);

        //then
        assertEquals(726, actualRentalAgreement.getChargeDays());
    }

    //Extra Test 6
    @Test
    public void calculateChargeDays_CHNSIndependenceDay_HolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        LocalDate dueDate = LocalDate.of(2020, 7, 4);

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(CHNS, checkoutDate, dueDate);

        //then
        assertEquals(2, actualChargeDays);
    }

    //Extra Test 6
    @Test
    public void calculateChargeDays_JAKDIndependenceDay_NoHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        LocalDate dueDate = LocalDate.of(2020, 7, 4);

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(JAKD, checkoutDate, dueDate);

        //then
        assertEquals(1, actualChargeDays);
    }

    //Extra Test 7
    @Test
    public void calculateChargeDays_CHNSLaborDay_HolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 9, 4);
        LocalDate dueDate = LocalDate.of(2023, 9, 8);

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(CHNS, checkoutDate, dueDate);

        //then
        assertEquals(4, actualChargeDays);
    }

    //Extra Test 8
    @Test
    public void calculateChargeDays_JAKDLaborDay_NoHolidayCharge() {
        //given
        LocalDate checkoutDate = LocalDate.of(2023, 9, 4);
        LocalDate dueDate = LocalDate.of(2023, 9, 8);

        //when
        int actualChargeDays = rentalAgreementService.calculateChargeDays(JAKD, checkoutDate, dueDate);

        //then
        assertEquals(3, actualChargeDays);
    }
}
