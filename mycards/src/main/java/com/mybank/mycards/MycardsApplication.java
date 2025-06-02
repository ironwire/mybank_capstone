package com.mybank.mycards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.mybank.common.entity"})
@ComponentScan(basePackages = {"com.mybank.mycards", "com.mybank.common.security", "com.mybank.common.config"})
@EnableJpaRepositories(basePackages = {"com.mybank.mycards.repository"})
public class MycardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycardsApplication.class, args);
	}

}
