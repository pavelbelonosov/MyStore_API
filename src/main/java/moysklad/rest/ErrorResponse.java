package moysklad.rest;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ErrorResponse {

    @Inject
    private Logger log;

    public Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());
        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    public Response.ResponseBuilder createObjectAlreadyExistResponse(String msg){
        return Response.status(Response.Status.CONFLICT).entity(msg);
    }

    public Response.ResponseBuilder createGeneralServerErrorResponse(String msg){
        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("error", msg);
        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
}
