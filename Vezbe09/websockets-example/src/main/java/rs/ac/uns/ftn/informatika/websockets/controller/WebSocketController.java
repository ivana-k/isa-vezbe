package rs.ac.uns.ftn.informatika.websockets.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class WebSocketController {

	@Autowired

	private SimpMessagingTemplate simpMessagingTemplate;

	// REST enpoint
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value="/sendMessageRest", method = RequestMethod.POST)
	public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message) {
		if (message.containsKey("message")) {
			if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("toId"), message);
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("fromId"), message);
			} else {
				this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);
			}
			return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
		}

		return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	/*
	 * WebSockets endpoint
	 * 
	 * Kao sto smo koristili @RequestMapping za RestController, @MessageMapping se koristi za websocket-e
	 * 
	 * Poruka ce biti poslata svim klijentima koji su pretplatili na /socket-publisher topic, 
	 * a poruka koja im se salje je messageConverted (simpMessagingTemplate.convertAndSend metoda).
	 * 
	 * Na ovaj endpoint klijenti salju poruke, ruta na koju klijenti salju poruke je /send/message (parametar @MessageMapping anotacije)
	 * 
	 */
	@MessageMapping("/send/message")
	public Map<String, String> broadcastNotification(String message) {
		Map<String, String> messageConverted = parseMessage(message);

		if (messageConverted != null) {
			if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null
					&& !messageConverted.get("toId").equals("")) {
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("toId"),
						messageConverted);
				this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("fromId"),
						messageConverted);
			} else {
				this.simpMessagingTemplate.convertAndSend("/socket-publisher", messageConverted);
			}
		}

		return messageConverted;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> parseMessage(String message) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> retVal;

		try {
			retVal = mapper.readValue(message, Map.class); // parsiranje JSON stringa
		} catch (IOException e) {
			retVal = null;
		}

		return retVal;
	}
}
