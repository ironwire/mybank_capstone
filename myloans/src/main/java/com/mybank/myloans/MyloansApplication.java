package com.mybank.myloans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.mybank.myloans.repository","com.mybank.common.repository"})
@EntityScan(basePackages = {"com.mybank.common.entity"})
@ComponentScan(basePackages = {"com.mybank.myloans", "com.mybank.common.security", "com.mybank.common.config"})
public class MyloansApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyloansApplication.class, args);
	}

}
