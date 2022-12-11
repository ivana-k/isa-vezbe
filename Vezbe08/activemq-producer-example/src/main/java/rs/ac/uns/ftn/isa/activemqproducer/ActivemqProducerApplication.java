package rs.ac.uns.ftn.isa.activemqproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Za pokretanje primera potrebno je instalirati ActiveMQ - https://activemq.apache.org/components/classic/download/
 */
@SpringBootApplication
public class ActivemqProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivemqProducerApplication.class, args);
	}

}
