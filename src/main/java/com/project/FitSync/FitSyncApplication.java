package com.project.FitSync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitSyncApplication.class, args);
		log.info("Application Started Successfully");
		System.out.println("Hello Hell");
	}

}
