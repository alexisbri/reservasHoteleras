package com.reservashoteleras.huespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.reservashoteleras.commons", "com.reservashoteleras.huespedes"})
public class HuespedesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuespedesApplication.class, args);
	}

}
