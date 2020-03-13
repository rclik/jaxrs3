package com.rcelik.jaxrs.objecttransformation.jettison;

import com.rcelik.jaxrs.objecttransformation.jettison.exception.CustomerNotFoundException;
import com.rcelik.jaxrs.objecttransformation.jettison.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("customer-jetti-json")
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

    @GET
    @Path("/customer/{id}")
    public Customer getCustomer(@PathParam("id") String id) {
        System.out.println("getCustomer is called....");

        CustomerService service = new CustomerService();
        Customer customer = service.findCustomerById(id);

        if (customer == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return customer;
    }

    @GET
    @Path("/name/{id}")
    public String getCustomerName(@PathParam("id") int id) {
        System.out.println("getCustomerName is called...");
        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    }

    @GET
    @Path("/customerWithId/{name}")
    public Customer getCustomerBySurName(@PathParam("name") String name) {
        System.out.println("getCustomerBySurName is called....");

        CustomerService service = new CustomerService();
        Customer customer = service.findCustomerById(name);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        return customer;
    }

}
