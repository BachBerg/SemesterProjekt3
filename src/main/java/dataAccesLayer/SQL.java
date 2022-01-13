package dataAccesLayer;

import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;
import model.User;

import javax.ws.rs.WebApplicationException;
import java.sql.*;

public class SQL {

    private SQL() {
    }

    static private final SQL SQLOBJ = new SQL();

    static public SQL getSqlOBJ() {
        return SQLOBJ;
    }

    private final String url = "jdbc:mysql://130.225.170.204:3306/gruppe2DB";
    private final String DatabaseUser = "gruppe2";
    private final String DatabasePassword = "MisdannetHelLy";
    // System.getenv("dbpass"); //tomcat system startups
    public Connection myConn;
    public Statement myStatement;

    public void makeConnectionSQL() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        myConn = DriverManager.getConnection(url, DatabaseUser, DatabasePassword);
        myStatement = myConn.createStatement();
    }

    public void removeConnectionSQL() {
        try {
            if (!myConn.isClosed()) {
                myConn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public AftaleListe getAftaleListeDateTime(String fra, String til) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        AftaleListe aftaleListe = new AftaleListe();
        try {
            PreparedStatement pp = myConn.prepareStatement("SELECT * FROM gruppe2DB.aftaler WHERE timestart BETWEEN ? and ?;");
            pp.setString(1, fra);
            pp.setString(2, til);

            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setID(String.valueOf(rs.getInt(1)));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setKlinikID(rs.getString(5));
                aftale.setCPR(rs.getString(6));

                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return aftaleListe;
    }


    public AftaleListe getAftaleListeDateTimeAndCPR(String fra, String til, String cpr) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        AftaleListe aftaleListe = new AftaleListe();
        try {
            PreparedStatement pp = myConn.prepareStatement("SELECT * FROM gruppe2DB.aftaler WHERE cpr = ? and timestart BETWEEN ? and ?;");
            pp.setString(1, cpr);
            pp.setString(2, fra);
            pp.setString(3, til);

            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setID(String.valueOf(rs.getInt(1)));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setKlinikID(rs.getString(5));
                aftale.setCPR(rs.getString(6));

                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return aftaleListe;
    }


    public void insertAftaleSQL(Aftale aftale) throws OurException {

        try {
            makeConnectionSQL();
            PreparedStatement pp = myConn.prepareStatement("INSERT INTO gruppe2DB.aftaler (patientID, timestart, timeend, note, klinikID, cpr) values(?,?,?,?,?,?);");
            pp.setString(1, aftale.getID());
            pp.setString(2, aftale.getTimeStart());
            pp.setString(3, aftale.getTimeEnd());
            pp.setString(4, aftale.getNotat());
            pp.setString(5, aftale.getKlinikID());
            pp.setString(6, aftale.getCPR());
            pp.execute();

            removeConnectionSQL();
        } catch (SQLException throwables) {
            OurException ex = new OurException();
            ex.setMessage("Tiden er allerede optaget.");
            throw ex;
        }
    }

    public AftaleListe getAftalerListe() throws SQLException {
        System.out.println("returnere aftale liste");
        SQL.getSqlOBJ().makeConnectionSQL();
        AftaleListe aftaleListe = new AftaleListe();
        String query = "SELECT * FROM gruppe2DB.aftaler;";
        try {
            ResultSet rs = SQL.getSqlOBJ().myStatement.executeQuery(query);

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setID(String.valueOf(rs.getInt(1)));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setKlinikID(rs.getString(5));
                aftale.setCPR(rs.getString(6));
                aftaleListe.addAftaler(aftale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println("succes");
        return aftaleListe;
    }

    public User getUserObjekt(String username) throws SQLException {

        User newUser = new User();
        try {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement preparedStatement = myConn.prepareStatement("SELECT * FROM gruppe2DB.LoginOplysninger WHERE USERNAME = ?;");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            newUser.setUsername(rs.getString(1));
            newUser.setPassword(rs.getString(2));
            newUser.setAuth(rs.getString(3));

            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("henter user obj: " + newUser.getAuth() + " " + newUser.getUsername() + " " + newUser.getPassword());
        return newUser;
    }

    public AftaleListe getAftalerIDSearch(String ID) throws SQLException {
        System.out.println("returnere aftale liste til et id");
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = myConn.prepareStatement("SELECT * FROM gruppe2DB.aftaler WHERE ID = ?;");
        AftaleListe aftaleListe = new AftaleListe();
        try {
            pp.setString(1, ID);
            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setID(String.valueOf(rs.getInt(1)));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setKlinikID(rs.getString(5));
                aftale.setCPR(rs.getString(6));
                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println("succes");
        return aftaleListe;
    }
}

