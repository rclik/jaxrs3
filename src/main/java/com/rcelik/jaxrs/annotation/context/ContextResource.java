package com.rcelik.jaxrs.annotation.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("context")
public class ContextResource {

    @GET
    @Path("contextPath")
    public final String getServletContext(@Context ServletContext servletContext){
        String messageFormat = "ServletContext is injected, context path is: %s";
        String contextPath = servletContext.getContextPath();
        // this should return context path of the request
        return String.format(messageFormat, contextPath);
    }

    @GET
    @Path("servlet")
    public final String getServletName(@Context ServletConfig servletConfig){
        String messageFormat = "Servlet name is %s";
        // this will return the servlet name. since servlet name is not defined,
        // then it will return canonical class name of the application class which is:
        // com.rcelik.jaxrs.application.RestApplication
        String servletName = servletConfig.getServletName();

        return String.format(messageFormat, servletName);
    }

    @GET
    @Path("requestPathInfo")
    public final String getPathInfo(@Context HttpServletRequest httpServletRequest){
        String messageFormat = "HttpServletRequest is injected. Request Path info is %s";
        // this will return the request path info, for this application it is /context/requestPathInfo
        String requestPathInfo = httpServletRequest.getPathInfo();

        // request path info is the url that is specific for this resource which is outer path of the class (context)
        // and path of the method (requestPathInfo)
        return String.format(messageFormat, requestPathInfo);
    }

    @GET
    @Path("responseCharEncoding")
    public String getResponseCharacterEncoding(@Context HttpServletResponse httpServletResponse){
        String messageFormat = "HttpServletResponse is injected. Response encoding character is %s";
        // this will return the character encoding of the response which is default ISO-8859-1
        String requestPathInfo = httpServletResponse.getCharacterEncoding();

        return String.format(messageFormat, requestPathInfo);
    }
}
