package rs.ac.uns.ftn.informatika.websockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
/*
 * Koristi autokonfiguraciju da uveze potrebne artifakte kako bi se omogucilo slanje poruka preko web soketa.
 */
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	/*
	 * Metoda registruje Stomp (https://stomp.github.io/) endpoint i koristi SockJS JavaScript biblioteku
	 * (https://github.com/sockjs)
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket") // Definisemo endpoint koji ce klijenti koristiti da se povezu sa serverom.
									   // U ovom slucaju, URL za konekciju ce biti http://localhost:8080/socket/
				.setAllowedOrigins("*") // Dozvoljavamo serveru da prima zahteve bilo kog porekla
				.withSockJS(); // Koristi se SockJS: https://github.com/sockjs/sockjs-protocol
	}

	/*
	 * Metoda konfigurise opcije message brokera. U ovom slucaju klijenti koji hoce da koriste web socket broker
	 * moraju da se konektuju na /socket-publisher.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/socket-subscriber") // Prefiks koji koji se koristi za mapiranje svih poruka.
																	   // Klijenti moraju da ga navedu kada salju poruku serveru.
																	   // Svaki URL bi pocinjao ovako: http://localhost:8080/socket-subscriber/…/…
				.enableSimpleBroker("/socket-publisher"); // Definisanje topic-a (ruta) na koje klijenti mogu da se pretplate.
														 // SimpleBroker cuva poruke u memoriji i salje ih klijentima na definisane topic-e.
														 // Server kada salje poruke, salje ih na rute koje su ovde definisane, a klijenti cekaju na poruke.
														 // Vise ruta odvajamo zarezom, npr. enableSimpleBroker("/ruta1", "/ruta2");
	}
}
