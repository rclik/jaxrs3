package com.rcelik.jaxrs.annotation.pathparam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/customer")
public class CustomerResource {

    @GET
    @Path("/{id}")
    public String getCustomerWithId(@PathParam("id") String customerId) {
        return String.format("Customer with id %s is requested ", customerId);
    }

    @GET
    @Path("/{first}-{last}")
    public String getCustomerWithFirstNameAndLastName(@PathParam("first") String firstName, @PathParam("last") String lastName) {
        return String.format("Customer with first name %s and last name %s is requested", firstName, lastName);
    }

    @GET
    @Path("/int/{id : \\d+}")
    public String getCustomerWithIntId(@PathParam("id") int id) {
        return String.format("Customer with id %s is requested", id);
    }

    @GET
    @Path("/identifier/{identifier : ID-\\d+\\w+}")
    public String getCustomerWithIdentifier(@PathParam("identifier") String identifier){
        return String.format("Customer with identifier %s is requested", identifier);
    }
}
