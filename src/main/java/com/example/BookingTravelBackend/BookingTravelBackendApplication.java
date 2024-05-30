package com.example.BookingTravelBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingTravelBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingTravelBackendApplication.class, args);
	}

}

