package com.softlogic.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;

@Provider
public class JSONParseExceptionMapper implements ExceptionMapper<Exception>
{
	public static final String ERROR_INVALID_JSON_REQUEST = "Invalid JSON request";
	public static final String ERROR_INVALIDE_ATTR_NAME = "Invalid attribute name";
	
	public Response toResponse(final Exception e)
	{
		// return specific error message
		if(e instanceof JsonParseException) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ERROR_INVALID_JSON_REQUEST).build();
		} else if (e instanceof UnrecognizedPropertyException) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ERROR_INVALIDE_ATTR_NAME).build();
		}
		
		// return generic message
		return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

	}
}