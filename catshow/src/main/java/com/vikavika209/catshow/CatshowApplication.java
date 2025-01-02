package com.vikavika209.catshow;

import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CatshowApplication {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private CatRepository catRepository;

	public static void main(String[] args) {
		SpringApplication.run(CatshowApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ApplicationContext context, CatRepository catRepository) {
		return args -> {
			ownerRepository.findAll().forEach(System.out::println);
			catRepository.findAll().forEach(System.out::println);
		};
	}
}
