package api;

import com.google.gson.Gson;
import controller.AftaleController;
import controller.JWTHandler;
import dataAccesLayer.SQL;
import exceptions.OurException;
import model.AftaleListe;
import model.User;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("aftaler")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML})
public class AftaleService {
    @Context
    ContainerRequestContext context;

    @GET
    public AftaleListe getPatient(@QueryParam("cpr") String cpr) throws SQLException {
        return AftaleController.getAftaleControllerOBJ().getAftaleListeCprSearch(cpr);
    }

    @Path("aftalerSQL")
    @POST
    @Produces({MediaType.TEXT_PLAIN})
    public String makepatientSQL(@QueryParam("cpr") String cpr, @QueryParam("timestart")
            String timestart, @QueryParam("timeend") String timeend, @QueryParam("note") String notat) throws OurException {
        return AftaleController.getAftaleControllerOBJ().createAftale(cpr, timestart, timeend, notat);
    }

    @Path("aftalerSQL")
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String selectFromTime(@QueryParam("from") String from, @QueryParam("to") String to) throws SQLException {
        return new Gson().toJson(SQL.getSqlOBJ().getAftaleListeDateTime(from, to));
    }
}
