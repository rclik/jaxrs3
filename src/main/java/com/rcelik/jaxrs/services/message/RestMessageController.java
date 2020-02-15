package com.rcelik.jaxrs.services.message;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/messages")
public class RestMessageController {

    @GET
    @Path("/hello")
    public String sayHello() {
        return "Hello";
    }
}
