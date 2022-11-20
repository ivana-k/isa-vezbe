package rs.ac.uns.ftn.informatika.ratelimiter.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
/*
 * Potrebno je ukljuciti podrsku za kesiranje
 */
@EnableCaching
public class RateLimiterRedisExample {

	public static void main(String[] args) {
		SpringApplication.run(RateLimiterRedisExample.class, args);
	}

}

