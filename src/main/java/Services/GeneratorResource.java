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
    public String fireGenerator(@PathParam("ruleData") String data){
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        //in request data staan de gegevens van de database dus je kan bijvoorbeeld requestData.getUrl() gebruiken
        Controller.printToConsole(requestData.toString());
        String URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();

        getRuleFromToolDb(URL,  USER, PASS, DB_ID);
        
        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)


        return null;
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
            Controller.printToConsole("Connected");
            stmt = conn.createStatement();
            sql = new StringBuilder().append("SELECT ").append("R.NAME RULE_NAME, ").append("RT.NAME RULETYPE, ").append("A1.TABEL TABLE_1, ").append("A1.COLUMN_NAME COLUMN_NAME, ").append("O.NAME OPERATOR, ").append("(SELECT TABEL FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) TABLE_2, ").append("(SELECT COLUMN_NAME FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) OPERAND1_ATTRIBUTE, ").append("(SELECT VAL FROM VAL V2 WHERE POS = 2 AND V2.IDRULE = R.IDRULE) OPERAND2_VALUE , ").append("(SELECT VAL FROM VAL V3 WHERE POS = 3 AND V3.IDRULE = R.IDRULE) OPERAND3_VALUE ").append("FROM RULE R ").append("LEFT JOIN RULETYPE RT ").append("ON R.IDRULETYPE = RT.IDRULETYPE ").append("LEFT JOIN ATTRIBUTE A1 ").append("ON A1.IDRULE = R.IDRULE ").append("LEFT JOIN OPERATOR O ").append("ON R.IDOPERATOR = O.IDOPERATOR ").append("WHERE IDTARGETDB = ").append(DB_ID).append(" AND A1.POS = 1").toString();

            ResultSet rs = stmt.executeQuery(sql);
            Controller.printToConsole(rs.toString());
            while (rs.next()) {
                Controller.printToConsole(rs.getString("RULE_NAME"));
                Controller.printToConsole(rs.getString("RULETYPE"));
                Controller.printToConsole(rs.getString("TABLE_1"));
                Controller.printToConsole(rs.getString("COLUMN_NAME"));
                Controller.printToConsole(rs.getString("OPERATOR"));
                Controller.printToConsole(rs.getString("TABLE_2"));
                Controller.printToConsole(rs.getString("OPERAND1_ATTRIBUTE"));
                Controller.printToConsole(rs.getString("OPERAND2_VALUE"));
                Controller.printToConsole(rs.getString("OPERAND3_VALUE"));
                ArrayList<Attribute> attributes = new ArrayList<Attribute>();
                attributes.add(new Attribute(rs.getString("TABLE_1"), rs.getString("COLUMN_NAME"),1));
                Controller.printToConsole(attributes.get(0).getName());
                attributes.add(new Attribute(rs.getString("TABLE_2"), rs.getString("OPERAND1_ATTRIBUTE"),2));
                ArrayList<Value> values = new ArrayList<Value>();
                values.add(new Value(rs.getString("OPERAND2_VALUE"),1));
                values.add(new Value(rs.getString("OPERAND3_VALUE"), 2));

                Controller.addRule(rs.getString("RULETYPE"),rs.getString("RULE_NAME"),null,attributes, values, rs.getString("OPERATOR"));
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

