package rs.ac.uns.ftn.isa.activemqconsumer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
/*
* @EnableJMS anotacija koja obezbedjuje detekciju svih @JmsListener anotacija unutar bilo koje komponente u nadleznosti Spring kontejnera.
* https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/annotation/EnableJms.html
* */
@EnableJms
public class JmsConfig {

    @Value("${active-mq.broker-url}")
    private String brokerUrl;

    @Value("${active-mq.broker-username}")
    private String brokerUsername;

    @Value("${active-mq.broker-password}")
    private String brokerPassword;

    /*
     * Registrujemo bean koji ce sluziti za konekciju na ActiveMQ gde se mi u
     * primeru kacimo u lokalu.
     * */
    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setUserName(brokerUsername);
        activeMQConnectionFactory.setPassword(brokerPassword);
        return  activeMQConnectionFactory;
    }

    /*
     * JmsListenerContainerFactory je interfejs odgovoran za kreiranje listener kontejnera za odredjeni endpoint.
     * Tipicna implementacija - DefaultJmsListenerContainerFactory - obezbedjuje neophodne opcije za konfiguraciju MessageListenerContainer-a.
     * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/config/JmsListenerContainerFactory.html
     * */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        // Potrebno je obezbediti publish/subscribe za topic. Nije neophodno za queue.
        // factory.setPubSubDomain(true);
        return factory;
    }
}
