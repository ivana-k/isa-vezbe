package rs.ac.uns.ftn.informatika.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * Hibernate nudi jedan kes (first-level ili L1 cache) kroz koji svi
 * zahtevi moraju proci. Second-level ili L2 cache je opcion i konfigurabilan (i eksterni za Hibernate).
 * L1 cache omogucava da, unutar sesije, zahtev za objektom iz baze uvek vraca istu instancu objekta
 * i tako sprecava konflikte u podacima i sprecava Hibernate da ucita isti objekat vise puta.
 */
@SpringBootApplication
@EnableJpaRepositories
/*
 * Potrebno je ukljuciti podrsku za kesiranje
 */
@EnableCaching
public class CacheExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheExampleApplication.class, args);
	}
}
