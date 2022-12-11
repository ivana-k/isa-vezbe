package rs.ac.uns.ftn.isa.activemqproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<String> publish(@RequestBody String message) {
        jmsTemplate.convertAndSend(queue, message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
