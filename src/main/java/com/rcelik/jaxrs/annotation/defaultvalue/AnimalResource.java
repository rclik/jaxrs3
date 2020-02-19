package com.rcelik.jaxrs.annotation.defaultvalue;

import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/animal")
public class AnimalResource {


    @GET
    public String getAnimalById(@DefaultValue("100") @QueryParam("id") String id) {
        return String.format("Animal with id %s is requested", id);
    }

    @GET
    @Path("/withMatrixParam")
    public String getAnimalByMatrixParam(
            @DefaultValue("red") @MatrixParam("color") String color,
            @DefaultValue("cat") @MatrixParam("kinds") List<String> kinds) {
        return String.format("Animal with color %s and %s colors is requested", color, kinds);
    }
}