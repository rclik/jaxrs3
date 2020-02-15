package com.rcelik.jaxrs.annotation.queryparam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/book")
public class BookResource {

    @GET
    public String getBookWithId(@QueryParam("id") int id) {
        return String.format("Book with id %s is requested", id);
    }

    @GET
    public String getBookByName(@Context UriInfo info) {
        String name = info.getQueryParameters().getFirst("name");
        return String.format("Book with name %s is requested. ", name);
    }
}
