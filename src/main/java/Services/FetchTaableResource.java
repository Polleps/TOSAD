package Services;

import Domain.Column;
import Domain.Controller;
import Domain.RequestData;
import Domain.Table;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
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
        Controller.printToConsole("<span class=\"requestHead\">----------------------FETCHING TABLES---------------------</span>");
        String databaseSchema = "TOSAD_2016_2D_TEAM6_TARGET";

        String DB_URL = requestData.getUrl();
        String USER = requestData.getUserName();
        String PASS = requestData.getPassword();
        String DB_ID = requestData.getDbId();

        //Domain.Controller.out += "Fetching tables for Database (ID:" + requestData.getDbId() + ")<br>";
        ArrayList<String> s = getTargetDatabaseCredit(DB_URL, USER, PASS, DB_ID	);
        if(s.size() > 2) {
            ArrayList<Table> tables = getDataFromTargetDB(s.get(0), s.get(1), s.get(2), s.get(3), databaseSchema);
            addToDatabase(tables, DB_URL, USER, PASS, DB_ID);
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
            //Controller.printToConsole("Copy Target DB:");
            //Controller.printToConsole(url);
            //Controller.printToConsole(USER);
            //Controller.printToConsole(PASS);
            conn = DriverManager.getConnection(url, USER, PASS);
            //Controller.printToConsole("Connected");
            stmt = conn.createStatement();
            stmt.execute("TRUNCATE TABLE TABLETARGET");
            stmt.execute("TRUNCATE TABLE ATTRIBUTETARGET");
            for(Table table : tables){
                try {

                    String generatedColumns[] = { "TABLEID" };
                    PreparedStatement pS = conn.prepareStatement("INSERT INTO TABLETARGET(NAAM, IDTARGETDB) VALUES(?, ?)",generatedColumns);
                    Controller.printToConsole("Table to insert: " + table.getName());
                    pS.setString(1, table.getName());
                    pS.setInt(2, Integer.parseInt(DB_ID));
                    pS.executeUpdate();
                    ResultSet key = pS.getGeneratedKeys();
                    key.next();
                    int tabId = key.getInt(1);
                    //Controller.printToConsole("TAB_ID: " + tabId);
                    for (Column col : table.getAttributes()){
                        PreparedStatement colStatement = conn.prepareStatement("INSERT INTO ATTRIBUTETARGET(NAAM, TABLETARGETID, RELATIONAL) VALUES(?, ?, ?)");
                        colStatement.setString(1, col.getName());
                        colStatement.setInt(2, tabId);
                        colStatement.setString(3, col.getRelationType());
                        colStatement.executeUpdate();
                    }
                    //Insert alle tabellen:
                }
                catch (SQLException se){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    se.printStackTrace(pw);
                    Controller.printToConsole(sw.toString());
                }
                catch (Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    Controller.printToConsole(sw.toString());
                }

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

        //Controller.printToConsole("Get data from target db:");
        //Controller.printToConsole("Database url: "+ url);
       // Controller.printToConsole("User: " + usr);
        //Controller.printToConsole("Password: " + pwd);
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
            Controller.printToConsole("Size of tables: " + listofTable.size());
            for (int i = 0; i < listofTable.size(); i++) {
                ArrayList<Column> colsConstraints = new ArrayList<Column>();
                sql = "SELECT COLUMN_NAME FROM all_tab_cols WHERE table_name = '"+ listofTable.get(i).getName() +"' and IDENTITY_COLUMN = 'YES'";
                ResultSet rsPrim = stmt.executeQuery(sql);
                //Controller.printToConsole("<strong>RESULTSET</strong> " + rsPrim.getString(1));
                //Controller.printToConsole("rsPrim.next() --> " +  rsPrim.next());
                while(rsPrim.next()){
                    Column col = new Column(rsPrim.getString("COLUMN_NAME"), "PK", null);
                    //Controller.printToConsole(col.toString());
                    colsConstraints.add(col);
                }

                sql = "SELECT COLUMN_NAME FROM ALL_CONS_COLUMNS A JOIN ALL_CONSTRAINTS C  ON A.CONSTRAINT_NAME = C.CONSTRAINT_NAME WHERE C.TABLE_NAME = '" + listofTable.get(i).getName() +"' AND C.CONSTRAINT_TYPE = 'R'";
                ResultSet rsForeign = stmt.executeQuery(sql);
                //Controller.printToConsole("rsForeign.next() --> " +  rsForeign.next());
                while(rsForeign.next()){
                    Column col = new Column(rsForeign.getString(1), "FK", null);
                    //Controller.printToConsole(col.toString());
                    colsConstraints.add(col);
                }

                if(! listofTable.get(i).getName().equals("HTMLDB_PLAN_TABLE")) {
                    sql = "SELECT column_name FROM all_tab_cols WHERE table_name ='" + listofTable.get(i).getName() + "'";

                    ResultSet rs1 = stmt.executeQuery(sql);

                    // STEP 5: Extract data from result set
                    while (rs1.next()) {
                        // Retrieve by column name
                        // DataList.add(rs.getString("NAME"));
                        String name = rs1.getString("column_name");
                        boolean found = false;
                        for(Column c : colsConstraints){
                            if(c.getName().equals(name)){
                                listofTable.get(i).addAttribute(c);
                                found = true;
                            }
                        }
                        if(!found){
                            listofTable.get(i).addAttribute(new Column(name, null, null));
                        }

                        //Controller.printToConsole(listofTable.get(i).getName() + " ----> " + name);



                        // DataList.add(rs.getString("IDTARGETDB"));
                    }
                }
            }
            // STEP 6: Clean-up environment
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
        //String json = new Gson().toJson(listofTable);
        String json = "[";
        for(Table t : listofTable){
            json += t.getJSON() + ",";
        }
        json += "]";
        //Controller.printToConsole(json);
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
            /*Controller.printToConsole("Target DB:");
            Controller.printToConsole(url);
            Controller.printToConsole(USER);
            Controller.printToConsole(PASS);*/
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