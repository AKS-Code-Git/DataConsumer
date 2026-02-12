package service;

import model.TopicProp;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import util.Constants;
import util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class CreateTopic {
    private static final Logger log = LoggerFactory.getLogger(CreateTopic.class);

    public void CreateNewTopic(final TopicProp topicProp, final String bootStrap) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);
        log.info("Creating Topic :" + topicProp.toString());
        log.info("BootStrap Server :" + bootStrap);
        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newTopic = new NewTopic(topicProp.getName(), topicProp.getPartitions(), topicProp.getReplica());
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
            try {
                createTopicsResult.values().get(topicProp.getName()).get();
                log.info("Topic \"" + topicProp.toString() + "\" created successfully!");
            } catch (ExecutionException e) {
                // Check if the exception is due to the topic already existing
                if (e.getCause() instanceof TopicExistsException) {
                    log.warn("Topic \"" + topicProp.getName() + "\" already exists.");
                } else {
                    log.error("Failed to create topic: " + e.getMessage(), e);
                }

            } catch (InterruptedException e) {
                log.error("Topic creation interrupted", e);
            }
        }
    }

    public void createAllTopics() {
        String   path = null;
        try {
            path = ResourceUtils.getFile("classpath:topics.properties").getAbsolutePath();
        } catch (FileNotFoundException e) {
            log.info("FileNotFound Error " + e.getMessage());
        }
        if(path == null){
            path= Constants.CONFIG_DIR + "/topics.properties";
        }
        log.info("Config directory : " + Constants.CONFIG_DIR);
        log.info("Absolute path :" + path );

        Map<String, String[]> topics = Util.readTopics(path);
        if(topics!=null) {
            Iterator<String> keys = topics.keySet().iterator();
            while (keys.hasNext()) {
                String[] t = topics.get(keys.next());
                createTopic(t[0], Integer.parseInt(t[1]), Short.parseShort(t[2]),t[3]);
            }
        }else {
            log.warn("Failed to find properties at :" + path);
        }
    }

    private void createTopic(final String topicName,
                             final int partitions,
                             final short replicas,
                             final String bootStrap) {
        final TopicProp tp = new TopicProp();
        tp.setName(topicName);
        tp.setPartitions(partitions);
        tp.setReplica(replicas);
        CreateNewTopic(tp,bootStrap);
    }
}
