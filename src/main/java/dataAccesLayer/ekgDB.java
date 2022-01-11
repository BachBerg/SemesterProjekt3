package dataAccesLayer;

import model.Aftale;
import model.AftaleListe;

import javax.ws.rs.WebApplicationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ekgDB {

    public static void insertEkgData(String value, String id) {

        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("INSERT INTO `gruppe2DB`.`EkgData` (`measurement`, `sessionID`) values(?,?);");

            pp.setString(1, value);
            pp.setString(2, id);

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
        return List;
    }
    public static double getMeasurementFromSession(int sessionID){
        double measurement = 0;
        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement prep = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM gruppe2DB.EkgData WHERE sessionID = ?;");
            prep.setString(1, String.valueOf(sessionID));
            ResultSet rs = prep.executeQuery();

            rs.next();
            measurement = rs.getDouble(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return measurement;
    }



}
