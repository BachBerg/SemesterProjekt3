package controller;

import dataAccesLayer.SQL;
import javax.ws.rs.core.HttpHeaders;

import static dataAccesLayer.ekgDB.*;

public class EkgController {
    /* klaser til at interagere med EkgService*/

    public static int[] getSessions(String cpr){
        int ID = SQL.getSqlOBJ().getID(cpr);

        return getSessionsID(ID);
    }

    public static double getData( int sessionID){
        return getMeasurementFromSession(sessionID);
    }

    public static String newData(String data, HttpHeaders httpHeaders){
        System.out.println(data);
        System.out.println(httpHeaders.getRequestHeader("Identifier").get(0));

        insertEkgData(data, httpHeaders.getRequestHeader("Identifier").get(0));
        return "measurement: " + data;
    }





}
