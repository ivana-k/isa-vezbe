package rs.ac.uns.ftn.isa.redispubsub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${redis.topic}")
    private String topicName;

    @Value("${redis.host-name}")
    private String hostName;

    @Value("${redis.port}")
    private int port;

    /*
    * Default Redis konekcija:
    *      @Autowired
    *      private RedisConnectionFactory redisConnectionFactory;
    *
    * Jos jedan nacin za uspostavljanje konekcije sa Redis serverom:
    *       @Bean
    *       public RedisConnectionFactory redisConnectionFactory() {
    *           JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
    *           redisConnectionFactory.setHostName("localhost");
    *           redisConnectionFactory.setPort(6379);
    *           return redisConnectionFactory;
    *       }
    * */

    /*
     * Registrujemo bean koji ce sluziti za konekciju na Redis
     * gde se mi u primeru kacimo u lokalu, definisanjem hostName i port parametara.
     * */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    /*
     * Registrujemo bean RedisTemplate koji ce sluziti kao pomocna klasa koja uproscava sinhronizovani
     * pristup Redis serveru za slanje i primanje poruka.
     * */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return redisTemplate;
    }

    /*
    * Registrujemo bean koriscenjem kreiranog MessagePublisher interfejsa i RedisMessagePublisher implementacije.
    * Ovaj bean se ponasa kao publisher koji salje poruke na definisani topic koriscenjem redisTemplate-a.
    * */
    @Bean
    public MessagePublisher messagePublisher()
    {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(topicName);
    }

    /*
    * Registrujemo MessageListenerAdapter bean koji sadrzi nasu implementaciju MessageListener interfejsa nazvanu RedisMessageSubscriber.
    * Ovaj bean se ponasa kao subscriber u pub-sub messaging modelu, koji cita poruke.
    * */
    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new RedisMessageSubscriber(),"onMessage");
    }

    /*
    * RedisMessageListenerContainer je klasa koja obezbedjuje asinhrono aponasanje za Redis message listener-e.
    * Poziva se interno, i sudeci po Spring Data Redis dokumentaciji:
    * – “handles the low level details of listening, converting and message dispatching.”
    * Ovde povezujemo prethodno definisanog subscriber-a i topic sa kojeg osluskuje poruke.
    * */
    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory());
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, topic());
        return redisMessageListenerContainer;
    }

}
