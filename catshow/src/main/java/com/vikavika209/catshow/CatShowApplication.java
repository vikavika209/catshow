package com.vikavika209.catshow;

import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Role;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.OwnerRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import com.vikavika209.catshow.service.CatService;
import com.vikavika209.catshow.service.OwnerService;
import com.vikavika209.catshow.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.vikavika209.catshow.model.Breed.*;

@SpringBootApplication
public class CatShowApplication {

    private final OwnerService ownerService;
	private final CatService catService;
	private final ShowService showService;
	private final OwnerRepository ownerRepository;

    @Autowired
	public CatShowApplication(OwnerService ownerService, CatService catService, ShowService showService, OwnerRepository ownerRepository) {
        this.ownerService = ownerService;
        this.catService = catService;
        this.showService = showService;
        this.ownerRepository = ownerRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(CatShowApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(OwnerService ownerService, CatService catService, ShowService showService, OwnerRepository ownerRepository) {
		return args -> {
			showService.deleteAll();
			catService.deleteAll();
			ownerService.deleteAll();

			LocalDate localDate = LocalDate.of(2025, 1, 4);
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Show show1 = showService.createShow("Москва", date, "гл. Проспект, 23");
			Show show2 = showService.createShow("Екатеринбург", date, "гл. Проспект, 23");
			Owner ivan = ownerService.createOwner("Иван", "ivanov@test.com", "123456", "Москва");
			Owner petr = ownerService.createOwner("Петр", "petr@test.com", "123456", "Екатеринбург");
			Cat cat1 = catService.createCat("Барсик", SPHYNX, ivan.getId());
			Cat cat2 = catService.createCat("Чёрт", MAINECOON, petr.getId());
			Cat cat3 = catService.createCat("Эдуард", MAINECOON, ivan.getId());
			Cat cat4 = catService.createCat("Дикий", SIAMESE, petr.getId());

			showService.addParticipant(cat1.getId(), show1.getId());
			showService.addParticipant(cat1.getId(), show2.getId());
			showService.addParticipant(cat2.getId(), show2.getId());

			Owner ivanAsAdmin = ownerService.setOwnerRole(ivan.getId(), Role.ADMIN);
			System.out.println(ivanAsAdmin.getUsername());
			System.out.println(ivanAsAdmin.getRoles());
		};
	}
}
