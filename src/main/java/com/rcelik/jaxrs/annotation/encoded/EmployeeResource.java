package com.rcelik.jaxrs.annotation.encoded;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/employee")
public class EmployeeResource
{

    @GET
    public String getEmployeeByName(@QueryParam("name") String name, @Encoded @QueryParam("surname") String surname){
        System.out.println("name: " + name);
        System.out.println("surname: " + surname);
        return String.format("Employee with name %s and sur name %s is requested", name, surname);
    }
}
