package api;


import com.google.gson.Gson;
import model.ekgMeasurements;
import model.ekgSessionList;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static controller.EkgController.*;
import static dataAccesLayer.ekgDB.insertSessionNote;

@Path("ekgSessions")
@Produces({MediaType.APPLICATION_XML})
public class EkgService {


    /* endpoint til at hente sessioner fra vores server i form af xml*/
    @GET
    public ekgSessionList getEkgSessions(@QueryParam("cpr") String cpr) {
        ekgSessionList test = getAllSession(cpr);
        System.out.println("her: " + test.getEkgSessionList().toString());
        return test;
    }

    /* endpoint til at hente data fra vores server i form af xml*/
    @Path("measurements")
    @GET
    public ekgMeasurements getEkgData(@QueryParam("sessionID") int sessionID) {
        return getData(sessionID);
    }

    /* til at modtage data fra python */
    @POST
    public String receiveData(String data, @Context HttpHeaders httpHeaders) {
        return newData(data, httpHeaders);
    }

    /* endpoint til at hente alle sessioner til et givent cpr*/
    @Path("ekgSessionJson")
    @GET
    public String getSessionJson(@QueryParam("cpr") String cpr) {
        return new Gson().toJson(getAllSessionJson(cpr));
    }

    /* til at hente hente data fra i form af et json objekt */
    @Path("measurementsJson")
    @GET
    public String getEkgDataJson(@QueryParam("sessionID") int sessionID) {
        return new Gson().toJson(getDataJson(sessionID));
    }

    /* metode til at opdatere note til en session */
    @Path("ekgSessionNote")
    @POST
    public void setSessionNote(@QueryParam("note") String note, @QueryParam("sessionID") int sessionID) {
        insertSessionNote(note, sessionID);
    }


}
