package com.example.demo;

import com.example.demo.beans.User;
import com.example.demo.controllers.AccountController;
import com.example.demo.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.demo.enums.ApplicationUserRole.ADMIN;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);System.out.println("Lorem ipsum fofou la yam");
	}
//	CREATE A DEFAULT ADMIN AT THE APPLICATION STARTUP
//@Bean
//public CommandLineRunner commandLineRunner(
//		AccountService service
//) {
//	return args -> {
//		var account = new AccountController.AccountRequest(
//				"Admin",
//				"Admin",
//				"ADMIN"
//				);
//		var admin = new User(
//				null,
//				"Admin",
//				"ADMIN"
//		);
//		System.out.println("Admin token: " + service.addUser(account,admin).getToken());
//
//
//
//	};
//}

}
