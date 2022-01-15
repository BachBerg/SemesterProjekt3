package api;


import com.google.gson.Gson;
import model.ekgMeasurements;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.Arrays;

import static controller.EkgController.*;
import static dataAccesLayer.ekgDB.insertSessionNote;

@Path("ekgSessions")
public class EkgService {


    /* metode der returnere de forskellige "recording/mållings sessioner som tilhøre en cpr*/
    @GET
    public String getEkgSessions(@QueryParam("cpr") String cpr) {
        int[] hvadermeningen = getSessions(cpr);
        System.out.println(Arrays.toString(hvadermeningen));
        return Arrays.toString(hvadermeningen);
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
        return new Gson().toJson(getAllSessionJson(cpr));
    }

    /* til at hente data fra vores server*/
    @Path("measurementsJson")
    @GET
    public String getEkgDataJson(@QueryParam("sessionID") int sessionID) {
        return new Gson().toJson(getDataJson(sessionID));
    }
    @Path("ekgSessionNote")
    @POST
    public void setSessionNote(@QueryParam("note") String note, @QueryParam("sessionID") int sessionID) {
        insertSessionNote(note, sessionID);
    }


}
