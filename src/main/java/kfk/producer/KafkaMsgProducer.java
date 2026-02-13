package kfk.producer;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import util.Constants;
import util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
@Service
public class KafkaMsgProducer {
    /**
     *
     */
    private static final Logger log = LoggerFactory.getLogger(KafkaMsgProducer.class);
    /**
     *
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * @param msg
     * @param topic
     * @param bootStrap
     */
    public void sendMessage(String msg, String topic, String bootStrap) {
        Properties props = new Properties();
        Util.getSerializable(props, Constants.PAYLOAD_TYPE_STRING);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);

        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<>(props);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", msg);
            log.info("Message : " + msg + " : Writing to topic :" + topic);
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
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }

    /**
     *
     * @param file
     * @param topic
     * @param bootStrap
     */
    public void sendCSfile(InputStream file, String topic, String bootStrap) {
        Properties props = new Properties();
        Util.getSerializable(props, Constants.PAYLOAD_TYPE_BYTE);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);
        Producer<String, InputStream> producer = null;
        try {
            if (file != null && topic != null && bootStrap != null) {
                producer = new KafkaProducer<>(props);
                ProducerRecord<String, InputStream> record = new ProducerRecord<>(topic, "key", file);
                try {
                    log.info("File of bytes : {} : sent to topic : {}.", +file.readAllBytes().length, topic);
                } catch (IOException e) {
                    log.error("IOException occured : {}", e.getMessage());
                }
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception != null) {
                            log.error("Error sending record: {}", exception.getMessage());
                        } else {
                            log.info("Record sent successfully to topic {}  partition {}  at offset  {}."
                                    , metadata.topic(), metadata.partition(), metadata.offset());
                        }
                    }
                });
            }
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
}