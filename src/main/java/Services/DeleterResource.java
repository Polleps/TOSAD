package Services;

import Domain.Controller;
import Domain.RequestData;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Polle on 3-2-2017.
 */
@Path("/generator")
public class DeleterResource {

    @GET
    @Produces("application/json")
    @Path("deleteRule/{ruleData}")
    public String deleteRule(@PathParam("ruleData") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        //in request data staan de gegevens van de database dus je kan bijvoorbeeld requestData.getUrl() gebruiken
        Controller.printToConsole(requestData.toString());
        String URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();
        String TABLE = requestData.getRuleTable();
        String RULE = requestData.getRuleName();

        ArrayList<String> s = getTargetDatabaseCredit(URL, USER, PASS, DB_ID);
        Controller.printToConsole(Integer.toString(s.size()));
        if (s.size() > 2) {
            deleteConstraints(s.get(0), s.get(1), s.get(2), s.get(3), TABLE, RULE);
        }

        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)


        return null;
    }

    private void deleteConstraints(String DB_URL, String USER, String PASS, String DB_DRIVER, String TABLE, String RULE) {
        String driver = null;
        String url = null;
        if (DB_DRIVER.equals("oracle")) {
            url = "jdbc:oracle:thin:@" + DB_URL;
            driver = "oracle.jdbc.driver.OracleDriver";
        } else if (DB_DRIVER.equals("mysql")
                ) {
            url = "sql:mysql:thin:@" + DB_URL;
            driver = "mysql.jdbc.driver.OracleDriver";
        }

        Controller.printToConsole("Applying constraints:");
        Controller.printToConsole("Database url: " + url);
        Connection conn = null;
        Statement stmt = null;
        String sql = "ALTER TABLE " + TABLE + " DROP CONSTRAINT " + RULE;

            try {
                Controller.printToConsole(driver);
                Class.forName(driver);
                conn = DriverManager.getConnection(url, USER, PASS);
                Controller.printToConsole("Targetdb Connected");

                stmt = conn.createStatement();
                Controller.printToConsole(sql);
                //ResultSet rs = stmt.executeQuery(sql);
                stmt.execute(sql);
                stmt.close();
                conn.close();

            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

        Controller.printToConsole("Done!");

    }

    private ArrayList<String> getTargetDatabaseCredit(String DB_URL, String USER, String PASS, String DB_ID) {
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        String db_name = null;
        String url = "jdbc:oracle:thin:@" + DB_URL;
        ArrayList<String> DataList = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Controller.printToConsole("Target DB:");
            Controller.printToConsole(url);
            Controller.printToConsole(USER);
            Controller.printToConsole(PASS);
            conn = DriverManager.getConnection(url, USER, PASS);
            Controller.printToConsole("Connected");
            stmt = conn.createStatement();

            sql = "SELECT * FROM TARGETDB WHERE IDTARGETDB = " + DB_ID;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                DataList.add(rs.getString("URL"));
                DataList.add(rs.getString("USERNAME"));
                DataList.add(rs.getString("PASSWORD"));
                DataList.add(rs.getString("LANGUAGE"));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return DataList;
    }

}

