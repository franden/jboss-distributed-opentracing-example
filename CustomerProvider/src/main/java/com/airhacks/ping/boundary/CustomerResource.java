package com.airhacks.ping.boundary;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;

@Path("customer")
public class CustomerResource {

    @Inject
    private Tracer tracer;

    @Traced
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getCustomer(@Context HttpHeaders headers) {
        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();

        String customerName = getCustomerPromDatabase();
        Map<String, String> returnValue = new HashMap<>();
        returnValue.put("customerName", customerName);
        return Response.ok(Entity.entity(customerName, MediaType.APPLICATION_JSON)).build();
    }

    private String getCustomerPromDatabase() {
        Span span = tracer.buildSpan("call database").start();
        try {
            span.log("start to call db");

            Thread.sleep(2000);
            span.log("finished to call db");

        } catch (Exception ex) {
            //TODO log EX
        } finally {
            span.finish();

        }

        return "Carl Zeiss";
    }

}
