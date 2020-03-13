package com.rcelik.jaxrs.objecttransformation.jettison.exception.mapper;

import com.rcelik.jaxrs.objecttransformation.jettison.exception.CustomerNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<CustomerNotFoundException> {

    @Override
    public Response toResponse(CustomerNotFoundException e) {
        Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
        builder.entity(e.getMessage());
        builder.type(MediaType.TEXT_PLAIN);
        return builder.build();
    }
}
