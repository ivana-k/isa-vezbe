package rs.ac.uns.ftn.isa.redispubsub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisMessageSubscriber implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] bytes) {
        messageList.add(message.toString());
        logger.info(">> Receiving: {}", message.toString());
    }
}
