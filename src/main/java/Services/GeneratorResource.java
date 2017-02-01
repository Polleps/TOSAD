package Services;

/**
 * Created by Polle on 31-1-2017.
 */

import javax.ws.rs.GET;

import Domain.RequestData;
import com.google.gson.Gson;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

        Domain.Controller.out += data + "<br>"; //Dit is voor debugging :)

        return null;
    }
}

