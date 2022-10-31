package moysklad.rest;


import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import moysklad.model.Product;
import moysklad.service.ProductService;


@Path("entity/product")
@RequestScoped
public class ProductResourceRESTService {

    private ProductService service;

    public ProductResourceRESTService() {
    }

    @Inject
    public ProductResourceRESTService(ProductService service) {
        this.service = service;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAll() {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(service.findAllOrderedByName());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductByName(@PathParam("name") String name) {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(service.findByName(name));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrUpdate(List<Product> products) {
        Response.ResponseBuilder builder = null;
        List<Product> list = new ArrayList<>();
        try {
            for (Product p : products) {
                service.validateProduct(p);
                list.add(p.getId() == null ? service.add(p) : service.update(p));
            }
            builder = Response.ok().entity(list);
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id:[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
    public Response updateById(@PathParam("id") UUID id, Product product) {
        Response.ResponseBuilder builder = null;
        try {
            product.setId(id);
            service.validateProduct(product);
            builder = Response.ok().entity(service.update(product));
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
            service.deleteById(id);
            builder = Response.ok();
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }


}
