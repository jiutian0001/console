package com.bxsbot.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.bxsbot.console.service.InitService;

@SpringBootApplication
@MapperScan("com.bxsbot.console.mapper") 
public class ConsoleApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ConsoleApplication.class, args);
		run.getBean(InitService.class).init();
	}

}
