package rs.ac.uns.ftn.isa.kafkaconsumerexample.controller;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.isa.kafkaconsumerexample.component.Consumer;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

    private Consumer myTopicConsumer;

    public ConsumerController(Consumer myTopicConsumer) {
        this.myTopicConsumer = myTopicConsumer;
    }
   
    @GetMapping("/get")
    public List<String> getMessages() {
        return myTopicConsumer.getMessages();
    }

}