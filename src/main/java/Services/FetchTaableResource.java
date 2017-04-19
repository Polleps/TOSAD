package Services;

import Domain.Controller;
import Domain.RequestData;
import Domain.Table;
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
public class FetchTaableResource {

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
        if(s.size() > 2) {
            ArrayList<Table> tables = getDataFromTargetDB(s.get(0), s.get(1), s.get(2), s.get(3), databaseSchema);
            addToDatabase(tables);
            return "{'STATUS' : 'DONE'}";
        }
        Controller.printToConsole("ERROR: Could not find target database credentials!");
        return null;
    }
    private void addToDatabase(ArrayList<Table> tables,String DB_URL, String USER, String PASS, String DB_ID){
        //Maak connectie met database
        //Sql dingen enzo
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        String db_name = null;
        String url = "jdbc:oracle:thin:@" + DB_URL;
        ArrayList<String> DataList = new ArrayList<String>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Controller.printToConsole("Copy Target DB:");
            Controller.printToConsole(url);
            Controller.printToConsole(USER);
            Controller.printToConsole(PASS);
            conn = DriverManager.getConnection(url, USER, PASS);
            Controller.printToConsole("Connected");
            stmt = conn.createStatement();



            for(Table table : tables){
                tabelenID
                        naam
                        TargetDBidTragerDB
                sql = "SELECT * FROM TARGETDB WHERE IDTARGETDB = " + DB_ID;
                stmt.executeQuery(sql);
                //Insert alle tabellen:

                //SQL Dingen enzo
            }


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
    private ArrayList<Table> getDataFromTargetDB(String DB_URL, String usr, String pwd, String db_driver, String schema){
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

        Controller.printToConsole("Get data from target db:");
        Controller.printToConsole("Database url: "+ url);
        Controller.printToConsole("User: " + usr);
        Controller.printToConsole("Password: " + pwd);
        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        ArrayList<Table> listofTable = new ArrayList<Table>();

        try {
            Controller.printToConsole(driver);
            Class.forName(driver);



            conn = DriverManager.getConnection(url, usr, pwd);

            DatabaseMetaData md = conn.getMetaData();

            ResultSet rs = md.getTables(null,schema , "%", null);

            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE")) {
                    //listofTable.add(new Table(rs.getString(3)));
                    listofTable.add(new Table(rs.getString(3)));
                }
            }

            rs.close();

            stmt = conn.createStatement();

            // sql = "SELECT * from customer";
            for (int i = 0; i < listofTable.size(); i++) {
                
                if(! listofTable.get(i).getName().equals("HTMLDB_PLAN_TABLE")) {
                    sql = "SELECT column_name FROM all_tab_cols WHERE table_name ='" + listofTable.get(i).getName() + "'";

                    ResultSet rs1 = stmt.executeQuery(sql);

                    // STEP 5: Extract data from result set
                    while (rs1.next()) {
                        // Retrieve by column name
                        // DataList.add(rs.getString("NAME"));
                        Controller.printToConsole(listofTable.get(i).getName() + " ----> " + rs1.getString("column_name"));
                        listofTable.get(i).addAttribute(rs1.getString("column_name"));


                        // DataList.add(rs.getString("IDTARGETDB"));
                    }
                }
            }
            // STEP 6: Clean-up environment
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
        //String json = new Gson().toJson(listofTable);
        String json = "[";
        for(Table t : listofTable){
            json += t.getJSON() + ",";
        }
        json += "]";
        Controller.printToConsole(json);
        return listofTable;
    }


    private ArrayList<String> getTargetDatabaseCredit(String DB_URL, String USER, String PASS, String DB_ID){
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