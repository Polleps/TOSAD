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
 * Created by Pawel on 1-2-2017.
 */
@Path("/fetch")
public class FetchTableResource {

    @GET
    @Produces("application/json")
    @Path("getTableData/{dbId}")
    public String getTableData(@PathParam("dbId") String data){
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        String databaseSchema = "TOSAD_2016_2D_TEAM6_TARGET";

        String DB_URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();

        Domain.Controller.out += "Fetching tables for Database (ID:" + requestData.getDbId() + ")<br>";
        ArrayList<String> s = getTargetDatabaseCredit(DB_URL, USER, PASS, DB_ID	);
        String str = getDataFromTargetDB(s.get(0), s.get(1), s.get(2),s.get(3), databaseSchema);
        String json = new Gson().toJson(str);
        return json;
    }

    private String getDataFromTargetDB(String DB_URL, String usr, String pwd, String db_driver, String schema){
        String driver = null;
        String url = null;
        if(db_driver.equals("oracle")){
            url = "jdbc:oracle:thin:@" + DB_URL;
            driver = "oracle.jdbc.driver.OracleDriver";
        }
        else if (db_driver.equals("mysql")
                ){
            url = "sql:mysql:thin:@" + DB_URL;
            driver = "mysql.jdbc.driver.OracleDriver";
        }


        Connection conn = null;
        Statement stmt = null;


        ArrayList<String> listofTable = new ArrayList<String>();

        try {
            Controller.printToConsole(driver);
            Class.forName(driver);

            Controller.printToConsole("Get data from target db:");
            Controller.printToConsole("Database url: "+ url);
            Controller.printToConsole("User: " + usr);
            Controller.printToConsole("Password: " + pwd);

            conn = DriverManager.getConnection(url, usr, pwd);

            DatabaseMetaData md = conn.getMetaData();

            ResultSet rs = md.getTables(null,schema , "%", null);

            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE")) {
                    listofTable.add(rs.getString(3));
                }
            }

            rs.close();
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
        String json = new Gson().toJson(listofTable);
        return json;
    }


    private ArrayList<String> getTargetDatabaseCredit(String DB_URL, String USER, String PASS, String DB_ID){
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        String db_name = null;
        ArrayList<String> DataList = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Controller.printToConsole("Target DB:");
            Controller.printToConsole(DB_URL);
            Controller.printToConsole(USER);
            Controller.printToConsole(PASS);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

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