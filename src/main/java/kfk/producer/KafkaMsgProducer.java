package kfk.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Properties;

@Service
public class KafkaMsgProducer {

//    private static final String TOPIC= "apac_topic3";
    private static final Logger log = LoggerFactory.getLogger(KafkaMsgProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    public void writeMessage(String msg){
//        log.info( "\ngetConfigurationProperties : " + this.kafkaTemplate.getProducerFactory().getConfigurationProperties());
//        this.kafkaTemplate.send(TOPIC, msg);
//    }

    public void sendMessage(String msg, String topic, String bootStrap){

        // Set up the producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = null;

        try {
            // Create the producer
            producer = new KafkaProducer<>(props);

            // Create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", msg);
            log.info("Message : "+ msg + " : Writing to topic :" + topic);
            // Send the record with a callback to handle exceptions
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

        } catch (Exception e) {
            log.error("Error creating or sending producer: " + e.getMessage());
        } finally {
            // Close the producer
            if (producer != null) {
                try {
                    producer.close();
                } catch (Exception e) {
                    log.error("Error closing producer: " + e.getMessage());
                }
            }
        }
    }
}