package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dataAccesLayer.ekgDB;
import model.ekgMeasurements;
import model.ekgSessionList;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import static dataAccesLayer.ekgDB.*;

public class EkgController {

    /* Denne metode oversætter føst dataen til et Json objekt, derefter kontrolere den om brugeren eksistere
     * og henter et id til det givne cpr. Til dette id laves så en ny session,
     * og dataen vidergives til sql med det tilhørende patient ID og sessions ID */
    public static String newData(String data, HttpHeaders httpHeaders) {
        System.out.println(data);
        /* først oversættes data til et Json objekt */
        JsonElement jsonElement = new JsonParser().parse(data);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Double> measurementList = new Gson().fromJson(jsonArray, ArrayList.class);

        String cpr = httpHeaders.getRequestHeader("Identifier").get(0);

        System.out.println("array lsiten: " + measurementList.toString());
        System.out.println("cpr: " + cpr);

        /* så kontrollers der om patienten eksitere i databasen og hvis ikke laves der en ny */
        Integer patientID = getID(cpr);
        if (patientID == null) {
            createNewPatient(cpr);
            patientID = getID(cpr);
        }

        // først laves der en ny session
        createNewSession(patientID);

        //så indsættes den dataen tilhørende den nye session
        insertEkgData(measurementList, patientID, getNewestSession(patientID));

        return "measurement: " + data;
    }

    public static ekgMeasurements getData(int sessionID) {
        System.out.println(sessionID);
        return getMeasurementFromSession(sessionID);
    }

    /* denne metode skal bruges til at implementere unirest så vi kan hente mållinger fra andre grupper i form af et Json objekt*/
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

    public static ekgSessionList getAllSession(String cpr) {
        int ID = getID(cpr);
        return ekgDB.getSessions(ID, cpr);
    }

    /* denne metode skal bruges til at implementere unirest så vi kan hente sessioner fra andre grupper i form af et Json objekt*/
    public static ekgSessionList getAllSessionJson(String cpr) {

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
