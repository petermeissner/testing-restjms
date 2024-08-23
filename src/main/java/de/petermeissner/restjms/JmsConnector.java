package de.petermeissner.restjms;

import jakarta.annotation.Resource;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.jms.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Singleton
public class JmsConnector {

    int messageReceiveCounter = 0;
    int messageSendCounter = 0;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory jmsConnectionFactory;

    @Resource(mappedName = "java:jboss/exported/jms/queue/inbound")
    private Queue jmsQueueInbound;


    public String receive() {
        String res = null;

        try {
            Connection jmsConn = jmsConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    // preparation
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
                    MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsQueueInbound);

                    // receive
                    TextMessage message = (TextMessage) jmsConsumer.receiveNoWait();
                    res = message.getText();
                    jmsSession.commit();
                } finally {
                    jmsConn.close();
                }
            }
        } catch (JMSException ex) {
            System.err.println("Error receiving message: " + ex);
            return "Error receiving message: " + ex;
        }

        return res;
    }

    public String send(String msg_text) {
        try {
            Connection jmsConn = jmsConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    // preparation
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
                    MessageProducer jmsProducer = jmsSession.createProducer(jmsQueueInbound);

                    // message
                    TextMessage message = jmsSession.createTextMessage();
                    messageReceiveCounter++;
                    message.setText(messageReceiveCounter + " " + msg_text);

                    // send & commit
                    jmsProducer.send(message);
                    jmsSession.commit();
                } finally {
                    jmsConn.close();
                }
            }
        } catch (JMSException ex) {
            System.err.println("Error sending message: " + ex);
            return "Error sending message: " + ex;
        }

        return "OK, Message sent";
    }

    @Lock(LockType.READ)
    public List<String> browse() {
        List<String> res = new ArrayList<>();

        try {
            Connection jmsConn = jmsConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    // preparation
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.AUTO_ACKNOWLEDGE);

                    // browse
                    QueueBrowser jmsBrowser = jmsSession.createBrowser(jmsQueueInbound);
                    Enumeration messageEnum = jmsBrowser.getEnumeration();

                    // loop over messages
                    while (messageEnum.hasMoreElements()) {
                        TextMessage message = (TextMessage) messageEnum.nextElement();
                        res.add(message.getText());
                    }
                } finally {
                    jmsConn.close();
                }
            }
        } catch (JMSException ex) {
            System.err.println("Error browsing messages: " + ex);
            res.add("Error browsing messages: " + ex);
        }


        return res;
    }


}
