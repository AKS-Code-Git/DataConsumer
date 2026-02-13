package controller.post;

import kfk.producer.KafkaMsgProducer;
import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.Constants;

@RestController
public class SendMessage {

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
     * @param message
     */
    @PostMapping("/publish")
    public void writeMessageToTopic(@RequestBody Message message) {
        final String server = Constants.CURRENT_DIR.indexOf("/app") < 0 ? devBootStrap : bootStrap;
        this.producer.sendMessage(message.getMessage(), message.getTopic(), server);
    }
}
