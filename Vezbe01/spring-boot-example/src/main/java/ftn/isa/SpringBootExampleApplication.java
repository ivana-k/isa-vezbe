package ftn.isa;

import ftn.isa.controller.AssetController;
import ftn.isa.service.AssetService;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/*
 * @SpringBootApplication anotacija obuhvata
 * 	1. @Configuration,
 * 	2. @EnableAutoConfiguration i
 * 	3. @ComponentScan anotacije
 * sa njihovim default-nim atributima.
 *
 * @EnableAutoConfiguration anotacija upravlja konfiguracijom aplikacije. Sadrzi 'auto-configuration feature' na osnovu kojeg
 * Spring Boot gledajuci classpath (pom.xml), anotacije i konfiguraciju dodaje potrebne tehnologije i kreira aplikaciju.
 *
 * Iako koristimo @SpringBootApplication anotaciju, mogu se eksplicitno navesti sve navedene anotacije da bismo promenili njihove default-ne vrednost
 * npr. @ComponentScan("rs.ac.uns.ftn.informatika.spring.boot")
 *
 */
@SpringBootApplication
public class SpringBootExampleApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootExampleApplication.class, args);

		// svi registrovani bean-ovi interfejsa AssetService
		ctx.getBeansOfType(AssetService.class).values().stream().forEach(System.out::println);

		AssetController assetController = ctx.getBean(AssetController.class);
		assetController.getAssets().stream().forEach(x -> System.out.println(x.getName()));
	}

}
