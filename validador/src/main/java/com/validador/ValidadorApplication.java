package com.validador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ValidadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidadorApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));

	}
}
