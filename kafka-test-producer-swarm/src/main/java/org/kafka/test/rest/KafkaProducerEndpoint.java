package org.kafka.test.rest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafka.test.producer.KafkaTestProducer;
import org.kafka.test.producer.ProducerProperties;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
public class KafkaProducerEndpoint {

	static int roundRobin = 0;

	private static String TOPIC = "kafka_test";


	@Inject
	KafkaTestProducer producer;



	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	public Response testMessage() {

		return Response.ok("method service is running").build();
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public void postClichedMessage(String message) {
		System.out.println("Sending message");

		ProducerRecord record = new ProducerRecord(TOPIC, String.valueOf(roundRobin), "Some message :)");
		roundRobin ++;
		producer.send(record);
		System.out.println("message sent to topic: "+TOPIC+", round robin: "+roundRobin);

	}
}