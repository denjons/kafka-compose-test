package org.kafka.test.producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaTestProducer {

    @Inject
    ProducerProperties properties;

    KafkaProducer producer;


    @PostConstruct
    public void init(){
        producer = new KafkaProducer(properties.getProperties());
    }

    public void send(ProducerRecord record){
        producer.send(record);
    }

}
