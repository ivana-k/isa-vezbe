package rs.ac.uns.ftn.informatika.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * Ukljucivanje podrske za raspored izvrsavanja.
 */
@EnableScheduling
@SpringBootApplication
public class ScheduleExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleExampleApplication.class, args);
	}

}
