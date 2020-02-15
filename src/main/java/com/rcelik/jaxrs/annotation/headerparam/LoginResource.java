package com.rcelik.jaxrs.annotation.headerparam;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/login")
public class LoginResource {

    @GET
    @Path("/withParam")
    public String getHeaderWithParam(@HeaderParam("user-agent") String userAgent) {
        return String.format("Login with header %s is requested", userAgent);
    }

    @GET
    public String getHeaderWithContext(@Context HttpHeaders headers) {
        StringBuilder sb = new StringBuilder("Coming header values: \n");
        headers.getRequestHeaders().forEach( (header, values) -> {
            sb.append("\t header: [").append(header).append(": ");
            sb.append(String.join(",", values));
            sb.append("] \n");
        });
        return String.format("Headers %s ", sb.toString());
    }
}
