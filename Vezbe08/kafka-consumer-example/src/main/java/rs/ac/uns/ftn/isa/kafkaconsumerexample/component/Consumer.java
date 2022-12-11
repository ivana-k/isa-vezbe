package rs.ac.uns.ftn.isa.kafkaconsumerexample.component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Consumer {

    private final List<String> messages = new ArrayList<>();
    /*
     * @KafkaListener anotacijom se definiše naziv topic-a iz koje anotirana metoda
     * osluškuje poruke.
     */
    @KafkaListener(topics = "test-topic", groupId = "kafka-sandbox")
    public void listen(String message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public List<String> getMessages() {
        return messages;
    }

}