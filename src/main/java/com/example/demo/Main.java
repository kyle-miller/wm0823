package com.example.demo;

import com.example.demo.model.RentalAgreement;
import com.example.demo.model.Tool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
//		RentalAgreementService rentalAgreementService = new RentalAgreementService();
//		Tool LADW = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
//		LocalDate checkoutDate = LocalDate.of(2023, 07, 04);
//		int rentalDays = 15;
//		int discountPercent = 10;
//
//		RentalAgreement rentalAgreement = rentalAgreementService.checkout(LADW, rentalDays, discountPercent, checkoutDate);
		SpringApplication.run(Main.class, args);

//		System.out.println(rentalAgreement);
	}

}
