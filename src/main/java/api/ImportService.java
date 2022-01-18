package api;

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
    public String importXml(@QueryParam("grp") int grp, @QueryParam("CPR") String cpr) {
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
                if (cpr.length()>5) {
                    jsonobj = ImportController.getimportControllerOBJ().getImportJSON(("http://localhost:8080/Semesterprojekt3_war/data/aftaler?cpr="+cpr));
                }
                else{
                    jsonobj = ImportController.getimportControllerOBJ().getImportJSON("http://localhost:8080/Semesterprojekt3_war/data/aftaler");
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
