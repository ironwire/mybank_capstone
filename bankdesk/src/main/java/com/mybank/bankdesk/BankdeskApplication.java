package com.mybank.bankdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.mybank.bankdesk.repository","com.mybank.common.repository"}) // note: correct spelling
@EntityScan(basePackages = {"com.mybank.common.entity"})
@ComponentScan(basePackages = {"com.mybank.bankdesk", "com.mybank.common"})
public class BankdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankdeskApplication.class, args);
	}

}
