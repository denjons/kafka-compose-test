package org.kafka.test;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kafka.test.client.BasicClient;
import org.wildfly.swarm.arquillian.DefaultDeployment;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;


@RunWith(Arquillian.class)
@DefaultDeployment
public class KafkaProducerTest {

    Thread pollingThread;
    ArrayList<String> messages;
    UUID uuid;

    @After
    public void tearDown(){
        if(pollingThread != null && pollingThread.isAlive()){
            pollingThread.interrupt();
        }
    }

    @Before
    public void setup() {
        uuid = UUID.randomUUID();
        messages = new ArrayList<String>();
    }


	@Test
	public void producerEndpointTest() throws InterruptedException {

        System.out.print("---- producerEndpointTest ");

        BasicClient client = new BasicClient();


        String group = "kafka-test-group";

        ArrayList<String> topics = new ArrayList<String>();
        topics.add("kafka_test");

        for(int i = 0; i < 100; i++){
            Response response = client.postFor(uuid+" "+i, /*System.getenv().get("PRODUCER_HOST") +*/ "http://localhost:8080/rest/test/post",
                    MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_PLAIN_TYPE);
            assertEquals(204, response.getStatus());
        }

        startConsumer(topics, group);
        

        //Thread.sleep(1000);

        assertEquals(100, messages.size());

	}

	private void startConsumer(ArrayList<String> topics, String group){

        System.out.println(" ---- starting consumer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "10.0.1.20:9092");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("group.id", "kafka-test-group");


        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);


        int count = 0;

        kafkaConsumer.subscribe(topics);
        try{

            while(true && count < 10){
                count ++;
                ConsumerRecords<String, String> consumerRecord = kafkaConsumer.poll(100);
                System.out.println(" ---- received "+consumerRecord.count()+" messages.");
                consumerRecord.iterator().forEachRemaining(this::feedSubscriber);
            }

        }finally {
            kafkaConsumer.close();
        }


    }

    public void feedSubscriber(ConsumerRecord message){
        if(message.value().toString().contains(uuid.toString())){
            messages.add(message.value().toString());
        }

    }
}