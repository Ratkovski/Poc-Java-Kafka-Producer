package br.com.academy.ratkovski.Poc.Java.Kafka.Producer.controller;

import br.com.academy.ratkovski.Poc.Java.Kafka.Producer.domain.Post;
import br.com.academy.ratkovski.Poc.Java.Kafka.Producer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/posts/")
@RequiredArgsConstructor
public class PostController {

  @Value("${spring.kafka.producer.topic-name}")
  private String topicName;

  private final PostService postService;

  private final KafkaTemplate<String, Post> kafkaTemplate;

  @GetMapping("{userId}")
  private ResponseEntity<Post> post(@PathVariable String userId) {
    try {
      Post post = postService.consulta(userId);
      kafkaTemplate.send(topicName, userId, post);
      return ResponseEntity.ok().body(post);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }
}
