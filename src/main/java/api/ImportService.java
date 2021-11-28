package api;

import com.google.gson.Gson;
import controller.ImportController;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("import")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED})
public class ImportService {

    @GET
    public String importerxml(@QueryParam("grp") int grp, @QueryParam("CPR") String cpr) throws SQLException {
        JSONObject jsonobj;
        switch (grp) {
            case 1: {
                jsonobj = ImportController.getimportControllerOBJ().getImportJSON("grp1");
                return jsonobj.toString();
            }
            case 2: {
                jsonobj = ImportController.getimportControllerOBJ().getImportJSON("grp2");
                return jsonobj.toString();
            }
            case 3: {
                jsonobj = ImportController.getimportControllerOBJ().getImportJSON("grp3");
                return jsonobj.toString();
            }
            case 4: {
                System.out.println(cpr);
                if (cpr.length()>5) {
                    System.out.println("imhere");
                    jsonobj = ImportController.getimportControllerOBJ().getImportJSON("https://grp4.it3.diplomportal.dk/IT3_Delopgave_2_war/data/aftaler?cpr="+cpr);
                }
                else{
                    jsonobj = ImportController.getimportControllerOBJ().getImportJSON("https://grp4.it3.diplomportal.dk/IT3_Delopgave_2_war/data/aftaler");
                }
                return jsonobj.toString();
            }
            case 5: {
                jsonobj = ImportController.getimportControllerOBJ().getImportJSON("grp5");
                return jsonobj.toString();
            }
        }
        return null;
    }
}
