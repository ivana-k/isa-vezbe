package rs.ac.uns.ftn.informatika.schedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingContoller {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * Logika se izvrsava u vremenskim trenucima definisanim cron sintaksom. Vise o cron sintaksi mozete procitati na linku:
	 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
	 * 
	 * ${greeting.cron} -> cita se vrednost koja je definisana u src/main/application.properties fajlu.
	 */
	@Scheduled(cron = "${greeting.cron}")
	public void cronJob() {
		logger.info("> cronJob");
		
		// neka logika - simuliramo da izvrsavanje metode traje 5000 ms
		long pause = 5000;
		long start = System.currentTimeMillis();
		
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		
		logger.info("Procesiranje je trajalo {} sekundi.", pause / 1000);
		logger.info("< cronJob");
	}

	/*
	 * Logika pocinje da se izvrsava u fiksnim intervalima sa odlozenim pocetkom  izvrsavanja prilikom pokretanja aplikacije.
	 * 
	 * 'fixedRate' se koristi kao indikacija u kojem intervalu ce se pozivati metoda.
	 * 'initialDelay' se koristi kao indikacija koliko posle pokretanja aplikacije treba da se ceka do prvog pokretanja metode.
	 * 
	 * ${greeting.initialdelay} -> cita se vrednost koja je definisana u src/main/application.properties fajlu.
	 * ${greeting.fixedrate} -> cita se vrednost koja je definisana u src/main/application.properties fajlu.
	 */
	//@Scheduled(initialDelayString = "${greeting.initialdelay}", fixedRateString = "${greeting.fixedrate}")
	public void fixedRateJobWithInitialDelay() {
		logger.info("> fixedRateJobWithInitialDelay");
		
		// neka logika - simuliramo da izvrsavanje metode traje 5000 ms
		long pause = 5000;
		long start = System.currentTimeMillis();
		
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		
		logger.info("Procesiranje je trajalo {} sekundi.", pause / 1000);
		logger.info("< fixedRateJobWithInitialDelay");
	}

	/*
	 * Logika se izvrsava sa razmakom izmedju kraja poslednjeg izvrsavanja i pocetka sledeceg. 
	 * 
	 * 'fixedDelay' se koristi kao indikacija vremena koje treba da prodje izmedju izvrsavanja. 
	 * 'initialDelay' se koristi kao indikacija koliko da se saceka posle startovanja aplikacije sa prvim izvrsavanjem metode.
	 * 
	 * ${greeting.initialdelay} -> cita se vrednost koja je definisana u src/main/application.properties fajlu.
	 * ${greeting.fixeddelay} -> cita se vrednost koja je definisana u src/main/application.properties fajlu.
	 */
	//@Scheduled(initialDelayString = "${greeting.initialdelay}", fixedDelayString = "${greeting.fixeddelay}")
	public void fixedDelayJobWithInitialDelay() {
		logger.info("> fixedDelayJobWithInitialDelay");
		
		// neka logika - simuliramo da izvrsavanje metode traje 5000 ms
		long pause = 5000;
		long start = System.currentTimeMillis();
		
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		
		logger.info("Procesiranje je trajalo {} sekundi.", pause / 1000);
		logger.info("< fixedDelayJobWithInitialDelay");
	}

}
