package org.kafka.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.kafka.test.consumer.DummyService;
import org.kafka.test.consumer.KafkaTestConsumer;

import java.io.File;
import java.util.Properties;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

@RunWith(Arquillian.class)
public class KafkaConsumerTest {

	KafkaTestConsumer.Subscriber subscriber = new KafkaTestConsumer.Subscriber() {
		@Override
		public void accept(String message) {
			System.out.println("accepted "+message);
		}

		@Override
		public boolean isAlive() {
			return true;
		}
	};

	KafkaTestConsumer.Subscriber deadSubscriber = new KafkaTestConsumer.Subscriber() {
		@Override
		public void accept(String message) {
			System.out.println("This should not happen!");
			assertFalse(true);
		}

		@Override
		public boolean isAlive() {
			return false;
		}
	};


	@Deployment
	public static WebArchive deploy() {

        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml").resolve("org.apache.kafka:kafka-clients")
                .withTransitivity().asFile();

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addPackage("org.kafka.test.consumer")
                .addAsLibraries(libs);


        return archive;

	}

	@Test
	public void dummyTest(){
		System.out.println("dummy test");
		assertTrue(true);
	}

	@Test
    public void dummyServiceTest(DummyService service){
        service.doSomething();
    }


	//@Ignore
	@Test
	public void startPollingTest(KafkaTestConsumer kafkaTestConsumer) {

        System.out.println(" ----- startPollingTest");

        kafkaTestConsumer.init();

		kafkaTestConsumer.addSubscriber(subscriber);

		kafkaTestConsumer.addSubscriber(deadSubscriber);

		kafkaTestConsumer.addSubscriber(subscriber);

        kafkaTestConsumer.startPolling();

		System.out.println("Sending messages");


        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "10.0.1.20:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer(properties);

		try{

			for(int i = 0; i < 100; i++){
				ProducerRecord record = new ProducerRecord("kafka_test", String.valueOf(i), "test message "+i);
					producer.send(record);
					System.out.println("Message sent to topic: kafka_test, round robin: "+i);
			}

			System.out.println("Waiting one second.");
			Thread.sleep(1000);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			producer.close();
            kafkaTestConsumer.killPollingThread();
		}

		assertEquals(2, kafkaTestConsumer.getSubscribers().size());


	}

}




