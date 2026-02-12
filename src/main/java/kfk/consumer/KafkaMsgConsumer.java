package kfk.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class KafkaMsgConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaMsgConsumer.class);
//    private static final String TOPIC = "apac_topic3";

    public void consumeMessage(String topic, String server) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "Group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        Consumer<String, String> consumer = null;
        try {
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(topic));
            log.info("Reading Message from topic :" + topic);
//                while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            log.info("Record count :" + records.count());
            for (ConsumerRecord<String, String> record : records) {
                log.info("################Record has been read : " + record.value());
            }
////                    }
        } finally {
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (Exception e) {
                    log.error("Error closing consumer: " + e.getMessage());
                }
            }
        }

    }
}