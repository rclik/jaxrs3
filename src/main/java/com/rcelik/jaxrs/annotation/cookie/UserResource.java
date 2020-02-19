package com.rcelik.jaxrs.annotation.cookie;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    private static final String COOKIE_NAME = "myCookie";

    @POST
    public Response addCookie() {
        String result = "cookie is added";
        Response.ResponseBuilder responseBuilder = Response.ok(result);
        responseBuilder.cookie(new NewCookie(COOKIE_NAME, "itsValue"));

        // Response.ok(result).cookie(new NewCookie("myCookie", "itsValue")).build();

        return responseBuilder.build();
    }

    @GET
    public String getCookie(@CookieParam(COOKIE_NAME) String cookie) {
        return String.format("Cookie %s is requested", cookie);
    }

    @GET
    @Path("/getCookiesWithContext")
    public Response getCookiesWithContext(@Context HttpHeaders headers) {
        StringBuilder sb = new StringBuilder("Cookies are: \n");
        headers.getCookies().forEach((name, cookie) -> {
            sb.append("\tName: ").append(name).append("\t");
            sb.append("Value: ").append(cookie.getValue()).append("\n");
        });
        return Response.ok(sb.toString()).build();
    }

}
