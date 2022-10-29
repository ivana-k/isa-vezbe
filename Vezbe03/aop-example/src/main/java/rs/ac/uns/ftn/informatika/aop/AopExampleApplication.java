package rs.ac.uns.ftn.informatika.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import rs.ac.uns.ftn.informatika.aop.AopExampleApplication;
import rs.ac.uns.ftn.informatika.aop.service.SampleService;

/*
 * U slucaju da se nije koristio Spring Boot, podrska za aspekte bi se mogla ukljuciti
 * dodavanjem anotacije @EnableAspectJAutoProxy na konfiguracionu klasu ili <aop:aspectj-autoproxy/>
 * u slucaju XML konfiguracije
 */
@SpringBootApplication
public class AopExampleApplication implements CommandLineRunner {
	
	/* da bismo testirali aspekte,
	 * direktno smo pozvali u glavnoj klasi metodu,
	 * inace bi pozivi isli npr. u nekom kontroleru
	 */
	@Autowired
	private SampleService sampleService;

	@Override
	public void run(String... args) {
		this.sampleService.someMethodReturning();
		this.sampleService.someMethodAround();
		this.sampleService.someMethodBefore("neki string");
	}

	public static void main(String[] args) {
		
		SpringApplication.run(AopExampleApplication.class, args);
	}
}
