package rest.resources;

import rest.json.Credentials;
import services.AuthResult;
import services.AuthService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.security.NoSuchAlgorithmException;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/auth")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @EJB
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(
            @NotNull(message = "Для авторизации требуются учётные данные.")
            @Valid Credentials credentials
    ) {
        try {
            AuthResult result = authService.login(credentials.getUsername(), credentials.getPassword());
            if (result.isSuccessful())
                return Response.ok(tokenToJSON(result.getToken())).build();
            else
                return Response.status(FORBIDDEN).entity(result.getErrorMessage()).build();
        } catch (NoSuchAlgorithmException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(
            @NotNull(message = "Для регистрации требуются учётные данные.")
            @Valid Credentials credentials
    ) {
        try {
            AuthResult result = authService.register(credentials.getUsername(), credentials.getPassword());
            if (result.isSuccessful())
                return Response.ok(tokenToJSON(result.getToken())).build();
            else
                return Response.status(FORBIDDEN).entity(result.getErrorMessage()).build();
        } catch (NoSuchAlgorithmException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    private String tokenToJSON(String token) {
        return Json.createObjectBuilder().add("token", token).build().toString();
    }
}
