package de.petermeissner.restjms;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello")
public class Router {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}
