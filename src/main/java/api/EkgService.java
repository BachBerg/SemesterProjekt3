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


    /* metode der returnere de forskellige "recording/mållings sessioner som tilhøre en cpr*/
    @GET
    public ekgSessionList getEkgSessions(@QueryParam("cpr") String cpr) {
        ekgSessionList test = getAllSession(cpr);
        System.out.println("her: " + test.getEkgSessionList().toString());
        return test;
    }

    /* til at hente data fra vores server*/
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

    @Path("ekgSessionJson")
    @GET
    public String getSessionJson(@QueryParam("cpr") String cpr) {
        return new Gson().toJson(getAllSession(cpr));
    }

    /* til at hente data fra vores server*/
    @Path("measurementsJson")
    @GET
    public String getEkgDataJson(@QueryParam("sessionID") int sessionID) {
        return new Gson().toJson(getData(sessionID));
    }
    @Path("ekgSessionNote")
    @POST
    public void setSessionNote(@QueryParam("note") String note, @QueryParam("sessionID") int sessionID) {
        insertSessionNote(note, sessionID);
    }


}
