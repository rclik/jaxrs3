package com.rcelik.jaxrs.objecttransformation.jackson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("customer-json")
public class CustomerResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Customer getCustomer() {
        Address address = new Address();
        address.setCity("istanbul");
        address.setCountry("turkey");
        address.setZip("34100");

        Customer customer = new Customer();
        customer.setAddress(address);
        customer.setId(1);
        customer.setName("rahnan");
        customer.setSurName("celik");

        return customer;
    }
}
