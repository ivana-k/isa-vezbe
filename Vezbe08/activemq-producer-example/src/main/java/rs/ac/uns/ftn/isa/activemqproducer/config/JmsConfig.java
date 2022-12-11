package rs.ac.uns.ftn.isa.activemqproducer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Configuration
public class JmsConfig {

    @Value("${active-mq.broker-url}")
    private String brokerUrl;

    @Value("${active-mq.broker-username}")
    private String brokerUsername;

    @Value("${active-mq.broker-password}")
    private String brokerPassword;

    @Value("${active-mq.queue}")
    private String queueName;

    /*
     * Registrujemo bean koji ce sluziti za konekciju na ActiveMQ gde se mi u
     * primeru kacimo u lokalu.
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setUserName(brokerUsername);
        activeMQConnectionFactory.setPassword(brokerPassword);
        return  activeMQConnectionFactory;
    }

    /*
     * Registrujemo bean JmsTemplate koji ce sluziti kao pomocna klasa koja uproscava sinhronizovani
     * pristup Java Message Service-u (JMS) za slanje i primanje poruka.
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        // Potrebno je obezbediti publish/subscribe za topic. Nije neophodno za queue.
        // jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(queueName);
    }
}
