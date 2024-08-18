package de.petermeissner.restjms;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.jms.*;

import javax.naming.NamingException;

@Singleton
public class JmsConnector {

    int messageReceiveCounter = 0;
    int messageSendCounter = 0;
    @Resource(mappedName = "jms/ReplyQueueConnectionFactory")
    private ConnectionFactory replyQueueConnectionFactory;
    @Resource(mappedName = "jms/ReplyQueue")
    private Queue replyQueue;

    public void send() throws NamingException, JMSException {
        try {
            Connection jmsConn = replyQueueConnectionFactory.createConnection();
            if (jmsConn != null) {
                try {
                    jmsConn.start();
                    Session jmsSession = jmsConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
                    MessageProducer messageProducer = jmsSession.createProducer(replyQueue);

                    TextMessage message = jmsSession.createTextMessage();
                    message.setText("Message Text");
                    messageProducer.send(message);
                } finally {
                    jmsConn.close();
                }
            }
        } catch (JMSException ex) {
            System.err.println("Error sending message: " + ex);
        }
    }


}
