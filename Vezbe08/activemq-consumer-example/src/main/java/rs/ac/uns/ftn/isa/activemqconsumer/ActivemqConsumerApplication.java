package rs.ac.uns.ftn.isa.activemqconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Za pokretanje primera potrebno je instalirati ActiveMQ - https://activemq.apache.org/components/classic/download/
 */
@SpringBootApplication
public class ActivemqConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivemqConsumerApplication.class, args);
	}

}
