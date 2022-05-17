package com.zerotohero.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.zerotohero.admin.user","com.zerotohero.entities"})
public class ShopbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopbackendApplication.class, args);
	}

}
