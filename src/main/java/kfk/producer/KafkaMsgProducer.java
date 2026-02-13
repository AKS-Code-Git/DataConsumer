package kfk.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import util.Util;


import java.util.Map;
import java.util.Properties;

@Service
public class KafkaMsgProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMsgProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg, String topic, String bootStrap){
        // Set up the producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);
        final Map<String, String> serializer= Util.getSerializable("String");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer.get("key"));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer.get("value"));

        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<>(props);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", msg);
            log.info("Message : "+ msg + " : Writing to topic :" + topic);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        log.error("Error sending record: " + exception.getMessage());
                    } else {
                        log.info("Record sent successfully to topic " + metadata.topic() +
                                " partition " + metadata.partition() + " at offset " + metadata.offset());
                    }
                }
            });
        }
        finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
}