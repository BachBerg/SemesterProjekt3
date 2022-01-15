package dataAccesLayer;

import model.ekgMeasurements;
import model.ekgSession;
import model.ekgSessionList;

import javax.ws.rs.WebApplicationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ekgDB {

    public static void insertEkgData(List<Double> measurements, int patientID, int sessionID) {
        String sqlQuery = "INSERT INTO gruppe2DB.EkgData(measurement , sessionID , patientID) values(" + measurements.get(0) + "," + sessionID + "," + patientID + ")";

        for (int i = 1; i < measurements.size(); i++) {
            sqlQuery = sqlQuery + ",(" + measurements.get(i) + "," + sessionID + "," + patientID + ")";
        }

        sqlQuery = sqlQuery + ";";
        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement(sqlQuery);
            pp.execute();

            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException throwables) {
            throw new WebApplicationException(420);
        }
    }

    public static int[] getSessionsID(int patientID) {
        int[] List = new int[20];
        int i = 0;

        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement prep = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.sessionData WHERE patientID = ?;");
            prep.setString(1, String.valueOf(patientID));
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                List[i] = rs.getInt(1);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println(Arrays.toString(List));
        return List;
    }

    public static ekgSessionList getSessionsJson(int ID, String CPR){
        ekgSessionList liste = new ekgSessionList();
        try{
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement prep = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.sessionData WHERE patientID = ?;");
            prep.setString(1, String.valueOf(ID));
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                ekgSession ekgsession = new ekgSession();

                ekgsession.setSessionID(rs.getInt(1));
                ekgsession.setCpr(CPR);
                // mangler markers
                ekgsession.setTimeStart(rs.getString(3));
                ekgsession.setComment(rs.getString(5));

                liste.addEkgSession(ekgsession);
            }
            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static int getNewestSession(int patientID) {
        int newestSession = 0;

        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement prep = SQL.getSqlOBJ().myConn.prepareStatement("SELECT MAX(sessionID) FROM gruppe2DB.sessionData WHERE patientID= ?;");
            prep.setString(1, String.valueOf(patientID));

            ResultSet rs = prep.executeQuery();
            rs.next();

            newestSession = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println(newestSession);
        return newestSession;
    }

    public static ekgMeasurements getMeasurementFromSession(int sessionID) {
        ekgMeasurements ekgM = new ekgMeasurements();
        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement prep = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.EkgData WHERE sessionID = ?;");
            prep.setString(1, String.valueOf(sessionID));
            ResultSet rs = prep.executeQuery();

            while(rs.next()){
                ekgM.addMeasurments(rs.getDouble(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return ekgM;
    }

    public static void createNewSession(int patientID) {

        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("INSERT INTO gruppe2DB.sessionData (`patientID`) values(?);");
            pp.setString(1, String.valueOf(patientID));  //ID
            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();
            System.out.println("created new session til: " + patientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static int getID(String cpr) {
        int id = 0;

        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.patient WHERE cpr = ?;");
            pp.setString(1, cpr);

            ResultSet rs = pp.executeQuery();
            rs.next();
            id = rs.getInt(1);
            System.out.println("hentede succesfuldt id: " + id);
            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void createNewPatient(String cpr) {
        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("INSERT INTO gruppe2DB.patient (`cpr`) values(?);");
            pp.setString(1, cpr);
            pp.execute();

            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean doesPatientExist(String cpr) {
        boolean answer = false;
        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.patient WHERE cpr = ?;");
            pp.setString(1, cpr);
            ResultSet rs = pp.executeQuery();
            if(rs.next()){
                answer = true;
            }
            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;
    }

    public static void insertSessionNote(String note, int ID){

        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement(" UPDATE gruppe2DB.sessionData SET comment = ? WHERE (sessionID = ?);");
            pp.setString(1, note);
            pp.setInt(2, ID);
            pp.execute();


            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
