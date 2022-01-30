package br.com.academy.ratkovski.Poc.Java.Kafka.Producer.kafka;

import br.com.academy.ratkovski.Poc.Java.Kafka.Producer.domain.Post;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfiguration {


  @Value("${kafka.bootstrap.server}")
  private String bootStrapServer;


  @Bean
  public ProducerFactory<String, Post> producerFactory() {
    Map<String, Object> config = new HashMap<>();

    //create Producer Properties
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory(config);
  }

  @Bean
  public KafkaTemplate<String, Post> kafkaTemplate() {
    return new KafkaTemplate(producerFactory());
  }

  @Bean
  public NewTopic topic() {
    return new NewTopic("test_topic", 1, (short) 1);
  }
}