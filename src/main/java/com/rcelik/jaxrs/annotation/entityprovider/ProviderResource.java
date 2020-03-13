package com.rcelik.jaxrs.annotation.entityprovider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.OutputStream;

@Path("providers")
public class ProviderResource {

    @GET
    @Path("streamingOutput")
    @Produces({MediaType.TEXT_PLAIN})
    public StreamingOutput getStreamingOutput() {
        String message = "StreamingOutput is called.";
        return (OutputStream os) -> os.write(message.getBytes());
    }

    @GET
    @Path("byte")
    @Produces({MediaType.TEXT_PLAIN})
    public byte[] getByte() {
        String message = "Byte is called.";
        return message.getBytes();
    }
}
