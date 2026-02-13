package controller.post;

import kfk.producer.KafkaMsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.Constants;
import util.SelectTopic;

import java.io.InputStream;

@RestController
public class SendFile {

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
}
