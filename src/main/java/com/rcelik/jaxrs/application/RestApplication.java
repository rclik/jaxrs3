package com.rcelik.jaxrs.application;

import com.rcelik.jaxrs.annotation.context.ContextResource;
import com.rcelik.jaxrs.annotation.cookie.UserResource;
import com.rcelik.jaxrs.annotation.defaultvalue.AnimalResource;
import com.rcelik.jaxrs.annotation.encoded.EmployeeResource;
import com.rcelik.jaxrs.annotation.entityprovider.ProviderResource;
import com.rcelik.jaxrs.annotation.formparams.PersonResource;
import com.rcelik.jaxrs.annotation.headerparam.LoginResource;
import com.rcelik.jaxrs.annotation.matrixparam.CarResource;
import com.rcelik.jaxrs.objecttransformation.jaxb.PersonXmlResource;
import com.rcelik.jaxrs.annotation.path.ShoppingStoreResource;
import com.rcelik.jaxrs.annotation.pathparam.CustomerResource;
import com.rcelik.jaxrs.annotation.queryparam.BookResource;
import com.rcelik.jaxrs.objecttransformation.jettison.exception.mapper.NotFoundExceptionMapper;
import com.rcelik.jaxrs.services.message.RestMessageController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//@ApplicationPath("/") OK
//@ApplicationPath("/*") Don't use /*
//@ApplicationPath("/root-path/*") Don't use /*
@ApplicationPath("/root-path")
public class RestApplication extends Application {

    Set<Object> singletons = new HashSet<>();

    public RestApplication() {
        singletons.add(new RestMessageController());
        singletons.add(new ShoppingStoreResource());
        singletons.add(new CustomerResource());
        singletons.add(new CarResource());
        singletons.add(new BookResource());
        singletons.add(new PersonResource());
        singletons.add(new LoginResource());
        singletons.add(new UserResource());
        singletons.add(new AnimalResource());
        singletons.add(new EmployeeResource());
        singletons.add(new ContextResource());
        singletons.add(new ProviderResource());
        singletons.add(new PersonXmlResource());
        singletons.add(new com.rcelik.jaxrs.objecttransformation.jaxb.CustomerResource());
        singletons.add(new com.rcelik.jaxrs.objecttransformation.jackson.PersonResource());
        singletons.add(new com.rcelik.jaxrs.objecttransformation.jackson.CustomerResource());
        singletons.add(new com.rcelik.jaxrs.objecttransformation.jettison.CustomerResource());
        singletons.add(new NotFoundExceptionMapper());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
