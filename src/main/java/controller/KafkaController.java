package controller;

import kfk.consumer.KafkaMsgConsumer;
import kfk.producer.KafkaMsgProducer;
import model.Message;
import model.TopicProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.CreateTopic;
import util.Constants;
import util.SelectTopic;

import java.io.InputStream;

/**
 *
 */
@RestController
public class KafkaController {
    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaMsgProducer producer;

    /**
     *
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrap;

    /**
     *
     */
    @Value("${dev.bootStrap-server}")
    private String devBootStrap;

    /**
     *
     */
    @Autowired
    private CreateTopic createTopic;

    /**
     * @param message
     */
    @PostMapping("/publish")
    public void writeMessageToTopic(@RequestBody Message message) {
        final String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        this.producer.sendMessage(message.getMessage(), message.getTopic(), server);
    }

    /**
     * Set the HTTP method to POST or PUT.
     * Set the Content-Type header to multipart/form-data.
     * Include necessary authentication headers.
     *
     * @param file
     */
    @PostMapping("/sendCSfile")
    public void sendFile(@RequestBody InputStream file) {
        final String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        SelectTopic st = new SelectTopic();
        this.producer.sendCSfile(file, st.getTopic(), server);
    }

    /**
     * @param topicProp
     */
    @PostMapping("/createTopic")
    public void createTopic(@RequestBody TopicProp topicProp) {
        String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        log.info("Server :" + server);
        createTopic.CreateNewTopic(topicProp, server);
    }

    /**
     *
     */
    @PostMapping("/createAllTopics")
    public void createAllTopic() {
        createTopic.createAllTopics();
    }

    /**
     * @param topic
     */
    @GetMapping("/getMessage")
    public void readMessageFromTopic(@RequestBody TopicProp topic) {
        final String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        KafkaMsgConsumer kms = new KafkaMsgConsumer();
        kms.consumeMessage(topic.getName(), server);
    }
}