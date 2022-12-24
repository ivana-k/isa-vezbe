package rs.ac.uns.ftn.informatika.websockets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Primer Spring aplikacije koja koristi WebSockets i STOMP (Simple Text Oriented Messaging Protocol)
// Detaljnije na https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html
// WebSockets i Security: https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/websocket.html
@SpringBootApplication
public class WebsocketsExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketsExampleApplication.class, args);
	}

}
