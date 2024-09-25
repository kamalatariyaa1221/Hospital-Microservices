package com.hospitalDepartment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HospitalDepartmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalDepartmentApplication.class, args);
	}

}
