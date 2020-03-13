package com.rcelik.jaxrs.objecttransformation.jaxb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;

@Path("personxml")
public class PersonXmlResource {

    @GET
    @Path("person")
    @Produces({MediaType.APPLICATION_XML})
    public final Person getPerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("rahman");
        person.setSurName("celik");

        return person;
    }

    @GET
    @Path("personWithJaxbContext")
    @Produces(MediaType.APPLICATION_XML)
    public final String getPersonWithJaxbContext() throws JAXBException {
        Person person = new Person();
        person.setId(1);
        person.setName("rahman");
        person.setSurName("celik");

        JAXBContext context = JAXBContext.newInstance(Person.class);

        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(person, writer);

        return writer.toString();
    }

    @GET
    @Path("getAllPersons")
    @Produces(MediaType.APPLICATION_XML)
    public Person[] getAllPersons(){
        Person person = new Person();
        person.setId(1);
        person.setName("rahman");
        person.setSurName("celik");

        Person person2 = new Person();
        person2.setId(1);
        person2.setName("rahman");
        person2.setSurName("celik");

        return new Person[]{person, person2};
    }
}
