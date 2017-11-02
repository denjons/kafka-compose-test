package org.kafka.test.producer;

import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;


@ApplicationScoped
public class ProducerProperties {

    Properties properties = new Properties();

    public ProducerProperties(){
        properties.setProperty("bootstrap.servers", "10.0.1.20:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
    }

    public Properties getProperties() {
        return properties;
    }
}
