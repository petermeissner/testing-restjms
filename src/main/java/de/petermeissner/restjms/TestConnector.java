package de.petermeissner.restjms;

import jakarta.ejb.Singleton;

@Singleton
public class TestConnector {


    int messageReceiveCounter = 0;
    int messageSendCounter = 0;

    public String send( String message) {
        messageSendCounter++;
        String res = "JMS: " + message + " " + messageSendCounter;
        System.out.println(res);
        return res;
    }

    public String receive() {
        messageReceiveCounter++;
        String res = "JMS message:" + "blah" + " " + messageReceiveCounter;
        System.out.println(res);
        return res;
    }
}
