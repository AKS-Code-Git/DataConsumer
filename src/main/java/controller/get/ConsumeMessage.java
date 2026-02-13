package controller.get;

import kfk.consumer.KafkaMsgConsumer;
import model.TopicProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.Constants;

@RestController
public class ConsumeMessage {

    private static final Logger log = LoggerFactory.getLogger(ConsumeMessage.class);
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
     * @param topic
     */
    @GetMapping("/getMessage")
    public void readMessageFromTopic(@RequestBody TopicProp topic) {
        final String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        log.info("BootStrap server : {}",server);
        KafkaMsgConsumer kms = new KafkaMsgConsumer();
        kms.consumeMessage(topic.getName(), server);
    }
}
