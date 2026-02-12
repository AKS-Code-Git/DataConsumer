package controller;

import kfk.consumer.KafkaMsgConsumer;
import kfk.producer.KafkaMsgProducer;
import model.Message;
import model.TopicProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.CreateTopic;

@RestController
public class KafkaController {
    private final KafkaMsgProducer producer;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrap;
    @Autowired
    private CreateTopic ct;

    public KafkaController(KafkaMsgProducer producer) {
        this.producer = producer;
    }
    @PostMapping("/publish")
    public void writeMessageToTopic(@RequestBody Message message) {
        this.producer.sendMessage(message.getMessage());
    }
    @PostMapping("/createTopic")
    public void createTopic(@RequestBody TopicProp topicProp) {
        ct = new CreateTopic();
        ct.CreateNewTopic(topicProp, bootStrap);
    }
    @PostMapping("/createAllTopics")
    public void createAllTopic() {
        ct = new CreateTopic();
        ct.createAllTopics ();
    }
    @GetMapping("/getMessage")
    public void readMessageFromTopic() {
        KafkaMsgConsumer kms = new KafkaMsgConsumer();
        kms.consumeMessage();
    }
}

