package util;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public final class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static Map<String, String[]> readTopics(String path) {
        Map<String, String[]> retVal = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            Set keys = properties.keySet();
            Iterator<String> it = keys.iterator();
            retVal = new HashMap<>();
            final String regex="^";
            while (it.hasNext()) {
                String s = it.next();
                String[] val = properties.getProperty(s).split("\\^");
                retVal.put(s, val);
                log.info("Key :'" + s + "' Elements :" + val.length );
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("Properties : " + retVal);
        return retVal;
    }
    public static Properties getSerializable(Properties prop,final String payLoadType){
        final Map<String,String> retVal=new HashMap<String,String>();
        switch (payLoadType){
            case "String":
                prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                break;
            case "Byte":
                prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());
                prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());
                break;
            case "ByteArray":
                prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
                prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
                break;
        }
        return prop;
    }
}
