package com.rcelik.jaxrs.objecttransformation.jackson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("person-json")
public class PersonResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persons")
    public List<Person> getPerson() {
        List<Person> list = new ArrayList<>();

        Person person = new Person();
        person.setId(1);
        person.setName("jack");
        person.setSurName("reacher");

        list.add(person);

        person.setId(2);
        person.setName("christiano");
        person.setSurName("ronaldo");

        list.add(person);

        return list;
    }
}
