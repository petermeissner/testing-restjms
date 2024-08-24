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
public class JmsOutbound {

    int messagesOut = 0;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory jmsConnectionFactory;

    @Resource(mappedName = "java:jboss/exported/jms/queue/inbound")
    private Queue jmsQueueInbound;

    /**
     * Receives a message from the JMS queue.
     *
     * @return the received message as a String, or an error message if an exception occurs
     */
    @Lock(LockType.READ)
    public String getMessageFromQueue() {
        String res = null;

        try {
            Connection jmsConn = jmsConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    // preparation
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.CLIENT_ACKNOWLEDGE);
                    MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsQueueInbound);

                    // receive
                    TextMessage message = (TextMessage) jmsConsumer.receiveNoWait();

                    // handle no message / message received
                    if (message == null) {
                        return "No message received";
                    } else {
                        res = message.getText();
                    }

                    // acknowledge
                    jmsSession.commit();
                } finally {
                    jmsConn.close();
                }
            }
        } catch (JMSException ex) {
            System.err.println("Error receiving message: " + ex);
            return "Error receiving message: " + ex;
        }

        messagesOut++;
        return res;
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
