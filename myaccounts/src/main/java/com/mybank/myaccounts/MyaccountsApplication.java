package com.mybank.myaccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.mybank.common.entity"})
@ComponentScan(basePackages = {"com.mybank.myaccounts", "com.mybank.common"})
@EnableJpaRepositories(basePackages = {"com.mybank.myaccounts.repository"}) //,"com.mybank.common.repository"
public class MyaccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyaccountsApplication.class, args);
	}

}
