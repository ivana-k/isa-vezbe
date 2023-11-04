package rs.ac.uns.ftn.informatika.tx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
/*
 * Ukljucivanje podrske za upravljanje transakcijama
 * pomocu @EnableTransactionManagement anotacije
 */
@EnableTransactionManagement
@EnableJpaRepositories
public class TxExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxExampleApplication.class, args);
	}
}
