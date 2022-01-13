package api;

import com.google.gson.Gson;
import controller.AftaleController;
import controller.JWTHandler;
import dataAccesLayer.SQL;
import exceptions.OurException;
import model.AftaleListe;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Objects;

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
    public String selectFromTime(@QueryParam("from") String from, @QueryParam("to") String to,@QueryParam("cpr") String cpr) throws SQLException {
        String auth = JWTHandler.validate(context.getHeaderString("Authorization"));
        if(Objects.equals(auth, "1")){
            return new Gson().toJson(SQL.getSqlOBJ().getAftaleListeDateTime(from, to));
        }else{
            return new Gson().toJson(SQL.getSqlOBJ().getAftaleListeDateTimeAndCPR(from, to, cpr));
        }
    }
}
