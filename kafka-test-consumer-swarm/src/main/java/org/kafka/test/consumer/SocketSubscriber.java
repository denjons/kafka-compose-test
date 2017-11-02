package org.kafka.test.consumer;

import javax.websocket.Session;
import java.io.IOException;



public class SocketSubscriber implements KafkaTestConsumer.Subscriber {

    Session session;

    public SocketSubscriber(Session session){
        this.session = session;
    }

    @Override
    public void accept(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAlive() {
        return session != null && session.isOpen();
    }
}
