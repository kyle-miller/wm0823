package com.example.demo;

import com.example.demo.model.RentalAgreement;
import com.example.demo.model.Tool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.HolidayCheckerService.checkForIndependenceDay;
import static com.example.demo.HolidayCheckerService.checkForLaborDay;

@Service
public class RentalAgreementService {

    public static RentalAgreement checkout(Tool tool, int rentalDays, int discountPercent, LocalDate checkoutDate) throws IllegalArgumentException {
        RentalAgreement rentalAgreement = new RentalAgreement();

        if (rentalDays < 1) {
            throw new IllegalArgumentException("Please enter a valid rental day count. The rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Please enter a valid discount percentage. The discount percentage must be between 0% and 100%.");
        }

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);

        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);

        BigDecimal preDiscountCharge = tool.getDailyCharge().multiply(BigDecimal.valueOf(chargeDays));
        BigDecimal discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        rentalAgreement.setToolCode(tool.getToolCode());
        rentalAgreement.setToolType(tool.getToolType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(rentalDays);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setDueDate(dueDate);
        rentalAgreement.setDailyRentalCharge(dailyRentalCharge);
        rentalAgreement.setChargeDays(chargeDays);
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);
        rentalAgreement.setDiscountPercent(discountPercent);
        rentalAgreement.setDiscountAmount(discountAmount);
        rentalAgreement.setFinalCharge(finalCharge);

        printRentalAgreement(rentalAgreement);

        return rentalAgreement;
    }

    protected static int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;

        LocalDate rentalDate = checkoutDate;
        do {
            boolean isHoliday = !tool.isHolidayCharge() && (isIndependenceDay(rentalDate) || isLaborDay(rentalDate))
            boolean isWeekend = rentalDate.getDayOfWeek() == DayOfWeek.SATURDAY || rentalDate.getDayOfWeek() == DayOfWeek.SUNDAY;
            
            if (!isHoliday && (isWeekend ? tool.isWeekendCharge() : tool.isWeekdayCharge())) {
                chargeDays++;
            }
        } while ((rentalDate = rentalDate.plusDays(1)).isBefore(dueDate))

        return chargeDays;
    }

    private static void printRentalAgreement(RentalAgreement rentalAgreement) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        DecimalFormat decimalFormat = new DecimalFormat("$#,##0.00");

        System.out.println("----------------------------------------------------");
        System.out.println("Tool code: " + rentalAgreement.getToolCode());
        System.out.println("Tool type: " + rentalAgreement.getToolType());
        System.out.println("Brand: " + rentalAgreement.getToolBrand());
        System.out.println("Rental Days: " + rentalAgreement.getRentalDays());
        System.out.println("Charge Days: " + rentalAgreement.getChargeDays());
        System.out.println("Checkout Date: " + rentalAgreement.getCheckoutDate().format(dateTimeFormatter));
        System.out.println("Due Date: " + rentalAgreement.getDueDate().format(dateTimeFormatter));
        System.out.println("Daily Rental Charge: " + decimalFormat.format(rentalAgreement.getDailyRentalCharge()));
        System.out.println("Pre-Discount Charge: " + decimalFormat.format(rentalAgreement.getPreDiscountCharge()));
        System.out.println("Discount Percent: " + rentalAgreement.getDiscountPercent() + "%");
        System.out.println("Discount Amount: " + decimalFormat.format(rentalAgreement.getDiscountAmount()));
        System.out.println("Final Charge: " + decimalFormat.format(rentalAgreement.getFinalCharge()));
        System.out.println("----------------------------------------------------");
    }
}
