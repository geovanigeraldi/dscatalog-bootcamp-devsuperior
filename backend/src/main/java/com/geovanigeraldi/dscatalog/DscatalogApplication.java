package com.geovanigeraldi.dscatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.geovanigeraldi.dscatalog.services.S3Service;

@SpringBootApplication
public class DscatalogApplication implements CommandLineRunner {

	@Autowired
	private S3Service s3service;
	
	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		s3service.uploadFile("C:\\Temp\\print.jpg");
	}

	
}
