package Services;

import Domain.RequestData;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by Polle on 1-2-2017.
 */
@Path("/fetch")
public class FetchTableResource {

    @GET
    @Produces("application/json")
    @Path("getTableData/{dbId}")
    public String getTableData(@PathParam("dbId") String data){
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        //Database id krijg je met requestData.getDbId();
        Domain.Controller.out += "Fetching tables for Database (ID:" + requestData.getDbId() + ")<br>";


        return null; //Hier moet je de response returnen!
    }
}
