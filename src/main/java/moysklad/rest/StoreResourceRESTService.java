package moysklad.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import moysklad.model.Store;
import moysklad.service.StoreService;

@Path("entity/store")
@RequestScoped
public class StoreResourceRESTService {

    private StoreService service;

    public StoreResourceRESTService() {
    }

    @Inject
    public StoreResourceRESTService(StoreService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllProducts() {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(service.findAllOrderedByName());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStore(List<Store> stores) {
        Response.ResponseBuilder builder = null;
        List<Store> list = new ArrayList<>();
        try {
            for (Store store : stores) {
                service.validateStore(store);
                list.add(store.getId() == null ? service.add(store) : service.update(store));
            }
            builder = Response.ok().entity(stores);
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            builder = new ErrorResponse().createObjectAlreadyExistResponse(e.getMessage());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id:[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
    public Response updateById(@PathParam("id") UUID id, Store store) {
        Response.ResponseBuilder builder = null;
        try {
            store.setId(id);
            service.validateStore(store);
            builder = Response.ok().entity(service.update(store));
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @DELETE
    @Path("/{id:[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") UUID id) {
        Response.ResponseBuilder builder = null;
        try {
            service.delete(id);
            builder = Response.ok();
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

}
