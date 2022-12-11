package rs.ac.uns.ftn.isa.kafkaconsumerexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
 * Za pokretanje primera potrebno je preuzeti Kafku - https://dlcdn.apache.org/kafka/3.3.1/kafka-3.3.1-src.tgz
 */
@SpringBootApplication
public class KafkaConsumerExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerExampleApplication.class, args);
	}

}
