package com.rcelik.jaxrs.annotation.beanparam;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("productWithBean")
public class ProductWithBeanResource {

    @GET
    @Path("/getProductById")
    public String getProductById(@BeanParam Product product){
        return String.format("Product %s is requested", product.toString());
    }
}
