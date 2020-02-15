package com.rcelik.jaxrs.annotation.matrixParam;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.PathSegment;
import java.util.List;

@Path("/car")
public class CarResource {

    @GET
    @Path("/{search : criteria}")
    public String getCarWithSearchCriteria(@PathParam("search") PathSegment pathSegment) {
        String brand = pathSegment.getMatrixParameters().getFirst("brand");
        List<String> colors = pathSegment.getMatrixParameters().get("color");

        return String.format("Car with brand: %s and color: %s is called", brand, colors);
    }

    @GET
    @Path("/{model : .+}/year/{year}")
    public String getCarByYear(@PathParam("model") List<PathSegment> car, @PathParam("year") String year) {
        return String.format("Car with properties %s and year %s is requested", car, year);
    }

    @GET
    @Path("/properties")
    public String getCarWithProperties(@MatrixParam("brand") String brand, @MatrixParam("color") List<String> colors){
        return String.format("Car with brand: %s and color: %s is called", brand, colors);
    }

}
