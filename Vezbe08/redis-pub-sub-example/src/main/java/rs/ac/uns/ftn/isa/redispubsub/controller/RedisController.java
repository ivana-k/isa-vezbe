package rs.ac.uns.ftn.isa.redispubsub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.ac.uns.ftn.isa.redispubsub.config.RedisMessagePublisher;
import rs.ac.uns.ftn.isa.redispubsub.config.RedisMessageSubscriber;
import rs.ac.uns.ftn.isa.redispubsub.model.Message;

import java.util.List;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    private static Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisMessagePublisher messagePublisher;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Message message) {
        logger.info(">> Publishing: {}", message);
        messagePublisher.publish(message.toString());
        return new ResponseEntity<>("Your message has been sent, " + message.getAuthor() + "! :)", HttpStatus.OK);
    }

    @GetMapping("/subscribe")
    public List<String> getMessages(){
        return RedisMessageSubscriber.messageList;
    }

}
