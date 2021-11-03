import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListeDB {
    private List<Patient> patientList;

    public static ListeDB sql;

    static {
        try {
            sql = new ListeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ListeDB getInstance() {
        return sql;
    }

    private ListeDB() throws SQLException {

    }

    public List<Patient> getpatientList() throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        patientList = new ArrayList<>();
        String query = "SELECT * FROM patients";
        try {
            ResultSet rs = SQL.getSqlOBJ().myStatement.executeQuery(query);

            while (rs.next()) {
                Patient p = new Patient();
                p.setCpr(String.valueOf(rs.getInt(1)));
                p.setName(rs.getString(2));
                p.setTimestart(rs.getString(3));
                p.setTimeend(rs.getString(4));
                p.setNote(rs.getString(5));

                patientList.add(p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return patientList;
    }

    public void insertPatientSQL(Patient p) throws SQLException {
        String cpr = p.getCpr();
        String navn = p.getName();
        String startTime = p.getTimestart();
        String endTime = p.getTimeend();
        String note = p.getNote();
        String query = "INSERT INTO patients(cpr, navn, startTime, endTime, note) values("+cpr+",'"+navn+"', '"+startTime+"', '"+endTime+"','"+note+"');";
        SQL.getSqlOBJ().makeConnectionSQL();
        SQL.getSqlOBJ().myStatement.execute(query);
        SQL.getSqlOBJ().removeConnectionSQL();
    }

    public List<Patient> getpatientListeDateTime(String fra, String til) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        patientList = new ArrayList<>();
        String query = "SELECT * FROM patients WHERE startTime BETWEEN '"+fra+"' and '"+til+"';";
        try {
            ResultSet rs = SQL.getSqlOBJ().myStatement.executeQuery(query);

            while (rs.next()) {
                Patient p = new Patient();
                p.setCpr(String.valueOf(rs.getInt(1)));
                p.setName(rs.getString(2));
                p.setTimestart(rs.getString(3));
                p.setTimeend(rs.getString(4));
                p.setNote(rs.getString(5));

                patientList.add(p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return patientList;
    }
}

