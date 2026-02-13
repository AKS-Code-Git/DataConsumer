package controller.post;

import controller.KafkaController;
import model.TopicProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.CreateTopic;
import util.Constants;

@RestController
public class TopicManagement {
    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);
    /**
     *
     */
    @Autowired
    private CreateTopic createTopic;
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
     * @param topicProp
     */
    @PostMapping("/createTopic")
    public void createTopic(@RequestBody TopicProp topicProp) {
        String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        log.info("Server : {}", server);
        createTopic.CreateNewTopic(topicProp, server);
    }

    /**
     *
     */
    @PostMapping("/createAllTopics")
    public void createAllTopic() {
        createTopic.createAllTopics();
    }
}
