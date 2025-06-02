package com.mybank.myaccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.mybank.common.entity"})
@ComponentScan(basePackages = {"com.mybank.myaccounts", "com.mybank.common"})
public class MyaccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyaccountsApplication.class, args);
	}

}
