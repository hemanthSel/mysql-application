package com.mysql.mysql_application;

import ch.qos.logback.core.model.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlApplication.class, args);
	}

//
//	@GetMapping("/home")
//	public String home(){
//		return "wellCome to devops";
//	}
//
//	@GetMapping("/home/ncpl")
//	public String homeDemo(){
//		return "wellCome to devops Ncpl";
//	}

}
