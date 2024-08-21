package de.petermeissner.restjms;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.jms.*;

import javax.naming.NamingException;

@Singleton
public class JmsConnector {

    int messageReceiveCounter = 0;
    int messageSendCounter = 0;
    
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory replyQueueConnectionFactory;
    
    @Resource(mappedName = "java:jboss/exported/jms/queue/inbound")
    private Queue replyQueue;

    public String send(String msg_text) throws NamingException, JMSException {
        try {
            Connection jmsConn = replyQueueConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
                    MessageProducer messageProducer = jmsSession.createProducer(replyQueue);

                    TextMessage message = jmsSession.createTextMessage();
                    message.setText(msg_text);
                    messageProducer.send(message);
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
