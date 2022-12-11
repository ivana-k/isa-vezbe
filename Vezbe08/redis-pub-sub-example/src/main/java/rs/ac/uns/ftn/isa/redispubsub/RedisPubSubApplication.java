package rs.ac.uns.ftn.isa.redispubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Za pokretanje primera potrebno je instalirati Redis - https://redis.io/download
 */
@SpringBootApplication
public class RedisPubSubApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisPubSubApplication.class, args);
	}

}
