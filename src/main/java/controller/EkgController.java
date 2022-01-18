package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dataAccesLayer.apiDAO;
import dataAccesLayer.ekgDB;
import model.ekgMeasurements;
import model.ekgSession;
import model.ekgSessionList;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import static dataAccesLayer.ekgDB.*;

public class EkgController {
    /* klaser til at interagere med EkgService*/



    public static String newData(String data, HttpHeaders httpHeaders) {
        System.out.println(data);
        JsonElement Jelement = new JsonParser().parse(data);
        JsonArray Jarray = Jelement.getAsJsonArray();
        List<Double> liste = new Gson().fromJson(Jarray, ArrayList.class);

        String cpr = httpHeaders.getRequestHeader("Identifier").get(0);

        System.out.println("array lsiten: " + liste.toString());
        System.out.println("cpr: " + cpr);

        Integer patientID = getID(cpr);
        if(patientID == null){
            createNewPatient(cpr);
            patientID = getID(cpr);
        }

        // først laves der en ny session
        createNewSession(patientID);

        //så indsættes den dataen tilhørende den nye session
        insertEkgData(liste, patientID, getNewestSession(patientID));

        return "measurement: " + data;
    }
    public static ekgMeasurements getData(int sessionID) {
        System.out.println(sessionID);
        return getMeasurementFromSession(sessionID);
    }

    public static ekgMeasurements getDataJson(int sessionID) {
        //ekgMeasurements measOBJ = new ekgMeasurements();

        /*if(gruppeID == 3){
            //JSONObject grp3 = apiDAO.getApiDAOOBJ().getJsonOBJ("https://ekg3.diplomportal.dk/data/measurements?sessionID=" + sessionID,+System.getenv("apiKey"));
            for (int i = 0; i < grp3.getJSONObject("measurements").getJSONArray("measurment").length(); i++) {
                measOBJ.addMeasurments(grp3.getJSONObject("measurements").getJSONArray("measurment").getDouble(i));
            }
        }*/
        return getMeasurementFromSession(sessionID);
    }

    public static ekgSessionList getAllSession(String cpr){
        int ID = getID(cpr);
        return ekgDB.getSessions(ID, cpr);
    }

    public static ekgSessionList getAllSessionJson(String cpr){

        /* her henter vi sessioner fra de andre grupper, men i dette tilfælde kun gruppe 3 */
        //JSONObject grp3 = apiDAO.getApiDAOOBJ().getJsonOBJ("https://ekg3.diplomportal.dk/data/ekgSessions?cpr=" + cpr,(System.getenv("apiKey")));
        /*for (int i = 0; i < grp3.getJSONObject("sessions").getJSONArray("ekgSession").length(); i++) {
            ekgSession ekgSession = new Gson().fromJson(grp3.getJSONObject("sessions").getJSONArray("ekgSession").get(i).toString(), ekgSession.class);
            ekgSession.setGruppeID(3);
            newSessions.addEkgSession(ekgSession);
        }*/

        int ID = getID(cpr);
        return ekgDB.getSessions(ID, cpr);
    }

}
