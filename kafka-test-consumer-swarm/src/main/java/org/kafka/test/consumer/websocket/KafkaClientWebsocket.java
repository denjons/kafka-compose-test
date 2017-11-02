package org.kafka.test.consumer.websocket;

import org.kafka.test.consumer.KafkaTestConsumer;
import org.kafka.test.consumer.SocketSubscriber;

import javax.inject.Inject;
//import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/speedlayer")
public class KafkaClientWebsocket {

    @Inject
    KafkaTestConsumer kafkaTestConsumer;


    @OnMessage
    public String hello(String message) {
        System.out.println("Received : " + message);
        return message;
    }

    @OnOpen
    public void myOnOpen(Session session, EndpointConfig config) {
        System.out.println("WebSocket opened: " + session.getId());
        SocketSubscriber socketSubscriber = new SocketSubscriber(session);
        kafkaTestConsumer.addSubscriber(socketSubscriber);

    }

    @OnClose
    public void myOnClose(CloseReason reason) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }
}
