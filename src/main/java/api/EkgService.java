package api;


import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import java.sql.SQLException;
import java.util.Arrays;

import static controller.EkgController.*;

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
    public double getEkgData(@QueryParam("sessionID") int sessionID) {
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

}
