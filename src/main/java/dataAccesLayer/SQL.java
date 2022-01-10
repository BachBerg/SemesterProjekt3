package dataAccesLayer;

import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;

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
    private Connection myConn;
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


                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return aftaleListe;
    }

    public void insertAftaleSQL(Aftale aftale) {

        try {
            makeConnectionSQL();
            PreparedStatement pp = myConn.prepareStatement("INSERT INTO gruppe2DB.aftaler (`patientID`, `timestart`, `timeend`, `note`, `klinikID`) values(?,?,?,?,?);");

            pp.setString(1, aftale.getID());  //ID
            pp.setString(2, aftale.getTimeStart());  //starttime
            pp.setString(3, aftale.getTimeEnd());  //endtime
            pp.setString(4, aftale.getNotat());  //note
            pp.setString(5, aftale.getKlinikID()); //klinikid

            pp.execute();

            removeConnectionSQL();
        } catch (SQLException throwables) {
            /*OurException ex = new OurException();
            ex.setMessage("Tiden er allerede optaget.");
            throw ex;*/
            throw new WebApplicationException("Tiden er allerede optaget.",420);
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

                aftaleListe.addAftaler(aftale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println("succes");
        return aftaleListe;
    }

    public String hentBrugerListe(String bruger) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement preparedStatement = myConn.prepareStatement("SELECT * FROM gruppe2DB.LoginOplysninger WHERE USERNAME = ?;");
        preparedStatement.setString(1, bruger);
        String svar = "";
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                svar = svar + rs.getString(1);
                svar = svar + "|" + rs.getString(2);
                //svar = svar + "|" + rs.getString(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return svar;
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

                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        System.out.println("succes");
        return aftaleListe;
    }

    public int getID(String cpr) {
        int id = 0;

        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement pp = myConn.prepareStatement("SELECT * FROM gruppe2DB.patient WHERE CPR = ?;");
            pp.setString(1,cpr);

            ResultSet rs = pp.executeQuery();
            rs.next();
            id = rs.getInt(1);
            System.out.println("hentede succesfuldt id: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return id;
    }

    public String getCPR(int id) {
        String cpr = null;
        try {
            SQL.getSqlOBJ().makeConnectionSQL();

            PreparedStatement pp = myConn.prepareStatement("SELECT * FROM gruppe2DB.patient WHERE patientID = ?;");
            pp.setString(1, String.valueOf(id));

            ResultSet rs = pp.executeQuery();
            rs.next();
            cpr = rs.getString(2);
            System.out.println("hentede succesfuldt cpr: " + cpr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return cpr;
    }



    /* metode til at hente sessioner tilhørende et cpr*/



}

