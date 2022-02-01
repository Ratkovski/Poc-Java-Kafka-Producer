package br.com.academy.ratkovski.Poc.Java.Kafka.Producer.kafka;

import br.com.academy.ratkovski.Poc.Java.Kafka.Producer.domain.Post;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
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
  @Value("${spring.kafka.producer.enable-idempotence}")
  private String idempotence;
  @Value("${spring.kafka.producer.acks-config}")
  private String acks;
  @Value("${spring.kafka.producer.retries-config}")
  private String retries;
  @Value("${spring.kafka.producer.schema-registry-url}")
  private String schemaRegistryUrl;
  @Value("${spring.kafka.producer.topic-name}")
  private String topicName;


  @Bean
  public ProducerFactory<String, Post> producerFactory() {
    Map<String, Object> config = new HashMap<>();

    //create Producer Properties
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


    //create save Producer
    config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
    config.put(ProducerConfig.ACKS_CONFIG, acks);
    config.put(ProducerConfig.RETRIES_CONFIG, retries);
    config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,schemaRegistryUrl);

    return new DefaultKafkaProducerFactory(config);
  }

  @Bean
  public KafkaTemplate<String, kafka.avro.Post> kafkaTemplate() {
    return new KafkaTemplate(producerFactory());
  }

  @Bean
  public NewTopic topic() {
    return new NewTopic(topicName, 1, (short) 1);
  }
}