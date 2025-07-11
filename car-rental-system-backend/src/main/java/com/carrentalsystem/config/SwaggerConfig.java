package com.carrentalsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	// http://localhost:8080/swagger-ui/index.html

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Car Rental System Application")
				.version("v0.0.1")
				.license(new License()
				.url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("Rent A Car")
				.url("http://localhost:3000/"));
	}
	
	
}
