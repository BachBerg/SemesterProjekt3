package api;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import static controller.EkgController.*;

@Path("ekgSessions")
public class EkgService {


    /* metode der returnere de forskellige "recording/mållings sessioner som tilhøre en cpr*/
    @GET
    public int[] getEkgSessions(@QueryParam("cpr") String cpr) {
        return getSessions(cpr);
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
}
