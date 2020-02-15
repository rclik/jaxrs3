package com.rcelik.jaxrs.annotation.formparams;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/person")
public class PersonResource {

    @POST
    public String addPerson(@FormParam("name") String name, @FormParam("surname") String surName) {
        Person person = new Person(name, surName);
        return String.format("Person %s is added.", person);
    }
}
