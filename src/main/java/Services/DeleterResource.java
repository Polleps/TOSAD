package Services;

import Domain.Controller;
import Domain.RequestData;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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


        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)


        return null;
    }
}

