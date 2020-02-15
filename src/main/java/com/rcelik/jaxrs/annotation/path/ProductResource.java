package com.rcelik.jaxrs.annotation.path;

import javax.ws.rs.GET;

public class ProductResource {

    @GET
    public String getProduct(){
        return "MacBook Pro";
    }
}
