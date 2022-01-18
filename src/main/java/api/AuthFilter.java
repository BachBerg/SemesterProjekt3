package api;

import controller.JWTHandler;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (!"login".equals(containerRequestContext.getUriInfo().getPath())) {
            if (containerRequestContext.getHeaderString("Authorization") == null) {
                throw new WebApplicationException("Ingen Token", 401);
            }

            /* Kontrol af private key på aftaler endpoint */
            if ("aftaler".equals(containerRequestContext.getUriInfo().getPath())
                    || "ekgSessions".equals(containerRequestContext.getUriInfo().getPath())
                    || "ekgSessions/measurements".equals(containerRequestContext.getUriInfo().getPath())) {
                if (!containerRequestContext.getHeaderString("Authorization").equals("Bearer" + System.getenv("apiKey"))) {
                    throw new WebApplicationException("psst hvad er kodeordet?", 401);
                }
                /* ellers almindelig kontrol af login token */
            }else {
                try {
                    String Auth = JWTHandler.validate(containerRequestContext.getHeaderString("Authorization"));
                } catch (Exception e) {
                    throw new WebApplicationException("Invalid Token", 401);
                }
            }
        }
    }
}
