package de.franden.opentracing.bulkorder.boundary;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.client.ClientTracingFeature;
import io.opentracing.propagation.Format;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("bulkorder")
public class BulkOrderResource {

    @Inject
    private Tracer tracer;

    @PostConstruct
    void initialize() {
        GlobalTracer.register(tracer);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Traced
    @GET
    public Response getOrder() {

        //Response response  = getManually();

        Response response = getWithFilter();

        String body = response.readEntity(String.class);
        if (response.getStatus() == 200) {
            return Response.ok().entity(Entity.entity("BulkOrder was placed for " + body, MediaType.APPLICATION_JSON)).build();
        } else {
            throw new InternalServerErrorException("customer provider returned some error. code " + response.getStatus() + ": body: " + body);
        }
    }

    private Response getWithFilter() {

        Client client = ClientBuilder.newClient();
        client.register(ClientTracingFeature.class);
        return client.target("http://localhost:8080/CustomerProvider/resources/customer")
                .request(MediaType.APPLICATION_JSON)
                .get();

    }

    public Response getManually() {
        Span callCustomer = tracer.buildSpan("callCustomer").start();
        try (Scope scope = tracer.scopeManager().activate(callCustomer, false)) {

            MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
            HeaderCarrier headerCarrier = new HeaderCarrier(headers);
            tracer.inject(scope.span().context(), Format.Builtin.HTTP_HEADERS, headerCarrier);

            Client client = ClientBuilder.newClient();
            WebTarget webTarget
                    = client.target("http://localhost:8080/CustomerProvider/resources/customer");

            return webTarget.request(MediaType.APPLICATION_JSON).
                    headers(headers).get();


        } finally {
            callCustomer.finish();
        }

    }
}
