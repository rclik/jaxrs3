package com.rcelik.jaxrs.objecttransformation.jaxb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("customersxml")
public class CustomerResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public final Customer getCustomer(){
        Address address = new Address();
        address.setCity("istanbul");
        address.setCountry("turkey");
        address.setZipCode("34010");

        Customer customer = new Customer();
        customer.setAddress(address);
        customer.setId(1);
        customer.setName("rahman");
        customer.setSurName("celik");

        return customer;
    }
}
