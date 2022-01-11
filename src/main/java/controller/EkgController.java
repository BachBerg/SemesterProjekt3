package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dataAccesLayer.SQL;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import static dataAccesLayer.ekgDB.*;

public class EkgController {
    /* klaser til at interagere med EkgService*/

    public static int[] getSessions(String cpr) {
        int ID = SQL.getSqlOBJ().getID(cpr);

        return getSessionsID(ID);
    }

    public static double getData(int sessionID) {
        return getMeasurementFromSession(sessionID);
    }

    public static String newData(String data, HttpHeaders httpHeaders) {
        System.out.println(data);
        JsonElement Jelement = new JsonParser().parse(data);
        JsonArray Jarray = Jelement.getAsJsonArray();
        List<Double> liste = new Gson().fromJson(Jarray, ArrayList.class);

        System.out.println("array lsiten: " + liste.toString());
        System.out.println("cpr: " + httpHeaders.getRequestHeader("Identifier").get(0));

        int patientID = SQL.getSqlOBJ().getID(httpHeaders.getRequestHeader("Identifier").get(0));

        // først laves der en ny session
        createNewSession(patientID);

        //så indsættes den dataen tilhørende den nye session
        insertEkgData(liste, patientID, getNewestSession(patientID));

        return "measurement: " + data;
    }
}
