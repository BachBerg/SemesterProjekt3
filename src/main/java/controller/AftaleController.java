package controller;

import com.google.gson.Gson;
import dataAccesLayer.SQL;
import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;

import java.sql.SQLException;

import static dataAccesLayer.ekgDB.*;

public class AftaleController {

    private AftaleController() {
    }

    static private final AftaleController AFTALE_CONTROLLER_OBJ = new AftaleController();

    static public AftaleController getAftaleControllerOBJ() {
        return AFTALE_CONTROLLER_OBJ;
    }

    // bolsk værdi til kontrol af cpr'er
    public boolean cprCheck(String cpr) {
        try {
            double test = Double.parseDouble(cpr);
            return cpr.length() == 10;
        } catch (Exception e) {
            return false;
        }
    }

    public AftaleListe getAftaleListeCprSearch(String cpr) throws SQLException {

        if (cprCheck(cpr)) {
            int ID = getID(cpr);
            return SQL.getSqlOBJ().getAftalerIDSearch(String.valueOf(ID));
        }else{
            return SQL.getSqlOBJ().getAftalerListe();
        }
    }


    public String createAftale(String cpr, String timestart, String timeend, String note) throws OurException {
        Aftale aftale = new Aftale();

        if (cprCheck(cpr)) {
            if(!doesPatientExist(cpr)){
                createNewPatient(cpr);
            }
            if (note.length() < 255) {
                int id = getID(cpr);

                aftale.setID(String.valueOf(id));
                aftale.setTimeStart(timestart);
                aftale.setTimeEnd(timeend);
                aftale.setNotat(note);
                aftale.setKlinikID("2");
                aftale.setCPR(cpr);
                System.out.println(aftale.toString());
                SQL.getSqlOBJ().insertAftaleSQL(aftale);
                return "added patient" + aftale;
            } else {
                //forkert note
                OurException ex = new OurException();
                ex.setMessage("For lang note, skal være under 255 tegn.");
                throw ex;
            }
        } else {
            // forkert cpr
            OurException ex = new OurException();
            ex.setMessage("CPR skal være 10 cifre, yyyymmddxxxx");
            throw ex;
        }
    }
}
