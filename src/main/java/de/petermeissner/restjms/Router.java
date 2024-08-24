package de.petermeissner.restjms;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/")
public class Router {

    @EJB
    TestConnector testConnector;

    @EJB
    JmsOutbound jmsOutbound;

    @EJB
    JmsInbound jmsInbound;


    @GET
    @Produces("text/html")
    public String apiRoot() {
        return RouterHelper.getHTTPEndpoinst(Router.class);
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
        return jmsOutbound.getMessageFromQueue();
    }

    @GET
    @Path("/v1/message-add")
    @Produces("text/plain")
    public String messageAdd() {
        String res = jmsInbound.addMessageToQueue("blah blöah blah");
        return "Message to queue: " + res;
    }

    @GET
    @Path("/v1/message-browse/json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> messageBrowseJSON() {
        return jmsOutbound.browse();
    }

    @GET
    @Path("/v1/message-browse/xml")
    @Produces(MediaType.TEXT_XML)
    public JaxbList<String> messageBrowseXML() {
        List<String> msgs = jmsOutbound.browse();
        return new JaxbList(msgs);
    }

}
