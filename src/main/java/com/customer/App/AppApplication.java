package com.customer.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tka")
@EntityScan("com")
public class AppApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(AppApplication.class, args);
	}

}
