package com.rcelik.jaxrs.annotation.path;

import javax.ws.rs.Path;

@Path("/shoppingStore")
public class ShoppingStoreResource {


    @Path("/product")
    public ProductResource getProduct(){
        return new ProductResource();
    }

}
