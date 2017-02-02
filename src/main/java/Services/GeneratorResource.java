package Services;

/**
 * Created by Polle on 31-1-2017.
 */

import javax.ws.rs.GET;

import Domain.Attribute;
import Domain.Controller;
import Domain.RequestData;
import com.google.gson.Gson;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Polle on 31-1-2017.
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

        String URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();


        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)


        return null;
    }


    private void getRuleFromToolDb(String DB_URL, String USER, String PASS, String DB_ID) {
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        ArrayList<String> DataList = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            sql = "SELECT " + "R.NAME RULE_NAME, " + "RT.NAME RULETYPE, " + "A1.TABEL TABLE_1, "
                    + "A1.COLUMN_NAME COLUMN_NAME, " + "O.NAME OPERATOR, "
                    + "(SELECT TABEL FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) TABLE_2, "
                    + "(SELECT COLUMN_NAME FROM ATTRIBUTE A2 WHERE POS = 2 AND A2.IDRULE = R.IDRULE) OPERAND1_ATTRIBUTE, "
                    + "(SELECT VAL FROM VAL V2 WHERE POS = 2 AND V2.IDRULE = R.IDRULE) OPERAND2_VALUE , "
                    + "(SELECT VAL FROM VAL V3 WHERE POS = 3 AND V3.IDRULE = R.IDRULE) OPERAND3_VALUE " + "FROM RULE R "
                    + "LEFT JOIN RULETYPE RT " + "ON R.IDRULETYPE = RT.IDRULETYPE " + "LEFT JOIN ATTRIBUTE A1 "
                    + "ON A1.IDRULE = R.IDRULE " + "LEFT JOIN OPERATOR O " + "ON R.IDOPERATOR = O.IDOPERATOR "
                    + "WHERE IDTARGETDB = " + DB_ID + " AND A1.POS = 1";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ArrayList<Attribute> a = new ArrayList<Attribute>();
                a.add(new Attribute(rs.getString("TABLE_1"), rs.getString("COLUM_NAME")));

                Controller.addRule(rs.getString("RULETYPE"),rs.getString("RULE_NAME"),DB_ID,);
                //DataList.add(rs.getString("RULE_NAME"));
                //DataList.add(rs.getString("RULETYPE"));

                DataList.add(rs.getString("TABLE_1"));
                DataList.add(rs.getString("COLUMN_NAME"));
                DataList.add(rs.getString("OPERATOR"));
                DataList.add(rs.getString("TABLE_2"));
                DataList.add(rs.getString("OPERAND1_ATTRIBUTE"));
                DataList.add(rs.getString("OPERAND2_VALUE"));
                DataList.add(rs.getString("OPERAND3_VALUE"));

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

