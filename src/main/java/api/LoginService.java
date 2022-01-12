package api;

import controller.JWTHandler;
import controller.LoginController;
import model.LoginData;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("login")
@Produces({MediaType.TEXT_PLAIN})
public class LoginService {

    @Context
    ContainerRequestContext context;

    @GET
    public String loginKontrol(@QueryParam("username") String user, @QueryParam("password") String pass) {
        LoginData loginData = new LoginData(user, pass);
        return LoginController.getLoginControllerOBJ().doLogin(loginData);
    }

    @GET
    @Path("auth")
    public String loginKontrol() {
        return JWTHandler.validate(context.getHeaderString("Authorization"));
    }


}
