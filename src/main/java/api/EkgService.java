package api;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("ekgSessions")
public class EkgService {


    /* metode der returnere de forskellige "recording/mållings sessioner som tilhøre en cpr*/
    @GET
    public String getEkgSession(@QueryParam("cpr") String cpr) {
        return "xml objekt der indeholder sessions tilhørende det givne cpr";
    }


    /* til at hente data fra vores server*/
    @Path("measurements")
    @GET
    public String getEkgSessions(@QueryParam("cpr") String cpr, @QueryParam("sessionID") int sessionID) {

        return "sessioner";
    }

    /* til at sende data ind med python */
    @Path("newMeasurements")
    @POST
    public String pythonDataReceive(String string, @Context HttpHeaders httpHeaders) {
        System.out.println(string);
        System.out.println(httpHeaders.getRequestHeader("Identifier").get(0));

        return "mesurment: " + string;
    }
}
