package com.authentication_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.authentication_gateway.Repository") // Adjust the package if needed

public class AuthenticationGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationGatewayApplication.class, args);
	}

}
