package moysklad.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import moysklad.model.documents.Move;
import moysklad.model.documents.Sale;
import moysklad.model.documents.Supply;
import moysklad.service.DocumentService;

@Path("entity/document/")
@RequestScoped
public class DocumentResourceRESTService {

    private DocumentService documentService;

    public DocumentResourceRESTService(){}

    @Inject
    public DocumentResourceRESTService(DocumentService documentService){
        this.documentService = documentService;


    }

    @GET
    @Path("/supply")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAllSupplies() {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(documentService.findAllDocuments(Supply.class));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @POST
    @Path("/supply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSupply(Supply supply) {
        Response.ResponseBuilder builder = null;
        try {
            documentService.validateDocument(Supply.class, supply, supply.getCode());
            builder = Response.ok().entity(documentService.createSupply(supply));
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            builder = new ErrorResponse().createObjectAlreadyExistResponse(e.getMessage());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @GET
    @Path("/move")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAllMoves() {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(documentService.findAllDocuments(Move.class));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @POST
    @Path("/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMove(Move move) {
        Response.ResponseBuilder builder = null;
        try {
            documentService.validateDocument(Move.class, move, move.getCode());
            builder = Response.ok().entity(documentService.createMove(move));
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            builder = new ErrorResponse().createObjectAlreadyExistResponse(e.getMessage());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }


    @GET
    @Path("/sale")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAllSales() {
        Response.ResponseBuilder builder = null;
        try {
            builder = Response.ok().entity(documentService.findAllDocuments(Sale.class));
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }

    @POST
    @Path("/sale")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSale(Sale sale) {
        Response.ResponseBuilder builder = null;
        try {
            documentService.validateDocument(Sale.class, sale, sale.getCode());
            builder = Response.ok().entity(documentService.createSale(sale));
        } catch (ConstraintViolationException ce) {
            builder = new ErrorResponse().createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            builder = new ErrorResponse().createObjectAlreadyExistResponse(e.getMessage());
        } catch (Exception e) {
            builder = new ErrorResponse().createGeneralServerErrorResponse(e.getMessage());
        }
        return builder.build();
    }


}
