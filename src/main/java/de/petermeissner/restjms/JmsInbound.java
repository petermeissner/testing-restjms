package de.petermeissner.restjms;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.jms.*;

@Singleton
public class JmsInbound {

    int messagesIn = 0;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory jmsConnectionFactory;

    @Resource(mappedName = "java:jboss/exported/jms/queue/inbound")
    private Queue jmsQueueInbound;

    public String addMessageToQueue(String msg_text) {
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
                    messagesIn++;
                    message.setText(messagesIn + " " + msg_text);

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

}
