package moysklad.rest;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import moysklad.service.PositionService;
import moysklad.service.ProductService;

import java.util.UUID;


@Path("report/")
@RequestScoped
public class ReportsRESTService {

    private ProductService productService;
    private PositionService positionService;

    public ReportsRESTService() {
    }

    @Inject
    public ReportsRESTService(ProductService service, PositionService positionService) {
        this.productService = service;
        this.positionService = positionService;

    }

    @GET
    @Path("/product/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAllProducts(@QueryParam("filter") String name) {
        Response.ResponseBuilder builder = null;
        try {
            builder = name == null ? Response.ok().entity(positionService.reportAllProducts()) :
                    Response.ok().entity(productService.findByName(name));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }


    @GET
    @Path("/stock/byProduct")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listStockByStore(@QueryParam("filter") UUID storeId) {
        Response.ResponseBuilder builder = null;
        try {
            builder = storeId == null ? Response.ok().entity(positionService.reportProductStock()) :
                    Response.ok().entity(positionService.reportProductStockByStore(storeId));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

}
