package de.petermeissner.restjms;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
public class Router {
    @GET
    @Produces("text/plain")
    public String apiRoot() {
        return "API-Root";
    }

    @GET
    @Path("/dings")
    @Produces("text/plain")
    public String dings() {
        return "dings";
    }
}
