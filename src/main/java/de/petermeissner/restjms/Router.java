package de.petermeissner.restjms;

import jakarta.ejb.EJB;
import jakarta.jms.JMSException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.naming.NamingException;
import java.util.List;

@Path("/")
public class Router {

    @EJB
    TestConnector testConnector;

    @EJB
    JmsConnector jmsConnector;

    @GET
    @Produces("text/html")
    public String apiRoot() {
        return "<html>" +
                "<h1>API-Root</h1>" +
                "\n<br><a href=/restjms/api/v1/test-message-add>/restjms/api/v1/test-message-add</a>" +
                "\n<br><a href=/restjms/api/v1/test-message-get>/restjms/api/v1/test-message-get</a>" +
                "\n<br><a href=/restjms/api/v1/message-add>/restjms/api/v1/message-add</a>" +
                "\n<br><a href=/restjms/api/v1/message-browse>/restjms/api/v1/message-browse</a>" +
                "\n<br><a href=/restjms/api/v1/message-get>/restjms/api/v1/message-get</a>";
    }

    @GET
    @Path("/v1/test-message-get")
    @Produces("text/plain")
    public String testMessageGet() {
        String m = testConnector.receive();
        return "here you go: " + m;
    }

    @GET
    @Path("/v1/test-message-add")
    @Produces("text/plain")
    public String testMessageAdd() {
        String res = testConnector.send("blah blöah blah");
        return "send message: " + res;
    }

    @GET
    @Path("/v1/message-get")
    @Produces("text/plain")
    public String messageGet() {
        return jmsConnector.receive();
    }

    @GET
    @Path("/v1/message-add")
    @Produces("text/plain")
    public String messageAdd() {
        String res = jmsConnector.send("blah blöah blah");
        return "Message to queue: " + res;
    }

    @GET
    @Path("/v1/message-browse/json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> messageBrowseJSON() {
        return jmsConnector.browse();
    }

    @GET
    @Path("/v1/message-browse/xml")
    @Produces(MediaType.TEXT_XML)
    public JaxbList<String> messageBrowseXML() {
        List<String> msgs = jmsConnector.browse();
        return new JaxbList(msgs);
    }


    @GET
    @Path("/test2")
    public JaxbList test2() {
        List<String> msgs = jmsConnector.browse();
        return new JaxbList(msgs);
    }
}
