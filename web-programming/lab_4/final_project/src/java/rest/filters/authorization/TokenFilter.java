package rest.filters.authorization;

import services.AuthService;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Authorized
@Priority(Priorities.AUTHENTICATION)
@Provider
public class TokenFilter implements ContainerRequestFilter {
    @EJB
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        Optional<String> token = getTokenFromContext(containerRequestContext);

        if (!token.isPresent()) {
            containerRequestContext.abortWith(
                    Response.status(FORBIDDEN).entity("Для доступа требуется токен.").build()
            );
            return;
        }

        Optional<String> username = authService.getUsernameByToken(token.get());

        if (!username.isPresent()) {
            containerRequestContext.abortWith(
                    Response.status(FORBIDDEN).entity("Получен дефектный токен.").build()
            );
            return;
        }

        containerRequestContext.getHeaders().add("username", username.get());
    }

    private Optional<String> getTokenFromContext(ContainerRequestContext containerRequestContext) {
        String authHeader = containerRequestContext.getHeaderString(AUTHORIZATION);
        return authHeader == null ?
                Optional.empty() : Optional.of(authHeader.trim());
    }
}
