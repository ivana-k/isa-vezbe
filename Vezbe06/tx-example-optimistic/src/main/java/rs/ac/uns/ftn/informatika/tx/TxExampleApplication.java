package rs.ac.uns.ftn.informatika.tx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
 * Ukljucivanje podrske za upravljanje transakcijama
 * pomocu @EnableTransactionManagement anotacije.
 * Ako se u projektu koristi spring-data-jpa podrska ce biti ukljucena automatski.
 */
//@EnableTransactionManagement
public class TxExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxExampleApplication.class, args);
	}
}
