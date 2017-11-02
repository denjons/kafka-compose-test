package org.kafka.test.consumer;


import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DummyService {

    public void doSomething(){
        System.out.println(" ------- method invoked");
    }
}
