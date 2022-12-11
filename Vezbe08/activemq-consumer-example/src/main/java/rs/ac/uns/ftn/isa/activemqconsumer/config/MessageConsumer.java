package rs.ac.uns.ftn.isa.activemqconsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    /*
    * @JmsListener anotacijom se definise naziv destinacija sa koje bi metoda anotirana ovom anotacijom trebalo da
    * osluskuje pristigle poruke - destination atribut; i referencira se JmsListenerContainerFactory
    * koji ce se koristiti za kreiranje message listener container-a - containerFactory atribut.
    *
    * https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#jms-annotated-method-signature
    * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/annotation/JmsListener.html
    * */
    @JmsListener(destination = "${active-mq.queue}", containerFactory = "jmsListenerContainerFactory")
    public void listener(String message){
        logger.info(">> Message received: {} ", message);
    }
}
