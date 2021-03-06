package Services;

/**
 * Created by Polle on 31-1-2017.
 */

import javax.ws.rs.GET;

import Domain.Attribute;
import Domain.Controller;
import Domain.RequestData;
import Domain.Value;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 31-1-2017.
 */
@Path("/generator")
public class GeneratorResource {

    @GET
    @Produces("application/json")
    @Path("fireGenerator/{ruleData}")
    public String fireGenerator(@PathParam("ruleData") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        //in request data staan de gegevens van de database dus je kan bijvoorbeeld requestData.getUrl() gebruiken
        Controller.printToConsole("<span class=\"requestHead\">----------------------GENERATING RULES---------------------</span>");
        //Controller.printToConsole(requestData.toString());
        String URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();

        getRuleFromToolDb(URL, USER, PASS, DB_ID);
        ArrayList<String> s = getTargetDatabaseCredit(URL, USER, PASS, DB_ID);
        //Controller.printToConsole(Integer.toString(s.size()));
        if (s.size() > 2) {
            applyConstraints(s.get(0), s.get(1), s.get(2), s.get(3));
        }
        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)


        return null;
    }

    private void applyConstraints(String DB_URL, String USER, String PASS, String db_driver) {
        String driver = null;
        String url = null;
        if (db_driver.equals("oracle")) {
            url = "jdbc:oracle:thin:@" + DB_URL;
            driver = "oracle.jdbc.driver.OracleDriver";
        } else if (db_driver.equals("mysql")
                ) {
            url = "sql:mysql:thin:@" + DB_URL;
            driver = "mysql.jdbc.driver.OracleDriver";
        }

        Controller.printToConsole("Applying constraints:");
        //Controller.printToConsole("Database url: " + url);
        Connection conn = null;
        Statement stmt = null;
        String sql = null;


        ArrayList<String> rules = new ArrayList<String>();
        rules = Controller.generateRules();

        for (int i = 0; i < rules.size(); i++) {

            try {
                //Controller.printToConsole(driver);
                Class.forName(driver);
                conn = DriverManager.getConnection(url, USER, PASS);
                //Controller.printToConsole("Targetdb Connected");


                stmt = conn.createStatement();
                sql = rules.get(i);
                Controller.printToConsole(sql);
                //ResultSet rs = stmt.executeQuery(sql);
                stmt.execute(sql);
                stmt.close();




                conn.close();

            } catch (SQLException se) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                se.printStackTrace(pw);
                Controller.printToConsole(sw.toString());
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Controller.printToConsole(sw.toString());
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
            //Controller.printToConsole("Target DB:");
            /*Controller.printToConsole(url);
            Controller.printToConsole(USER);
            Controller.printToConsole(PASS);*/
            conn = DriverManager.getConnection(url, USER, PASS);
            //Controller.printToConsole("Connected");
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

    private void getRuleFromToolDb(String DB_URL, String USER, String PASS, String DB_ID) {
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        String url = "jdbc:oracle:thin:@" + DB_URL;
        ArrayList<String> DataList = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, USER, PASS);
            //Controller.printToConsole("Connected");
            stmt = conn.createStatement();
            sql = "SELECT " + "R.NAME RULE_NAME, " + "RT.NAME RULETYPE, " + "A1.TABEL TABLE_1, " + "A1.COLUMN_NAME COLUMN_NAME, " + "O.NAME OPERATOR, " + "(SELECT TABEL FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) TABLE_2, " + "(SELECT COLUMN_NAME FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) OPERAND1_ATTRIBUTE, " + "(SELECT VAL FROM VAL V2 WHERE POS = 2 AND V2.IDRULE = R.IDRULE) OPERAND2_VALUE , " + "(SELECT VAL FROM VAL V3 WHERE POS = 3 AND V3.IDRULE = R.IDRULE) OPERAND3_VALUE " + "FROM RULE R " + "LEFT JOIN RULETYPE RT " + "ON R.IDRULETYPE = RT.IDRULETYPE " + "LEFT JOIN ATTRIBUTE A1 " + "ON A1.IDRULE = R.IDRULE " + "LEFT JOIN OPERATOR O " + "ON R.IDOPERATOR = O.IDOPERATOR " + "WHERE IDTARGETDB = " + DB_ID + " AND A1.POS = 1";

            ResultSet rs = stmt.executeQuery(sql);
            //Controller.printToConsole(rs.toString());
            while (rs.next()) {
                ArrayList<Attribute> attributes = new ArrayList<Attribute>();
                attributes.add(new Attribute(rs.getString("TABLE_1"), rs.getString("COLUMN_NAME"), 1, "iets"));
                //Controller.printToConsole(attributes.get(0).getName());
                attributes.add(new Attribute(rs.getString("TABLE_2"), rs.getString("OPERAND1_ATTRIBUTE"), 2, "iets"));
                ArrayList<Value> values = new ArrayList<Value>();
                values.add(new Value(rs.getString("OPERAND2_VALUE"), 1));
                values.add(new Value(rs.getString("OPERAND3_VALUE"), 2));

                Controller.addRule(rs.getString("RULETYPE"), rs.getString("RULE_NAME"), null, attributes, values, rs.getString("OPERATOR"));
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
    }
}