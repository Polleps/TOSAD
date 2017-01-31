package com.tutorialspoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/DataService")
public class DataService {
	
   DataDao dataDao = new DataDao();
   TargetDatabaseConnector targetDb = new TargetDatabaseConnector();
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String FAILURE_RESULT="<result>failure</result>";


   @GET
   @Path("/data")
   @Produces(MediaType.APPLICATION_XML)
   public List<Data> getUsers(){
      return dataDao.getAllUsers();
   }

   @GET
   @Path("/data/{userid}")
   @Produces(MediaType.APPLICATION_XML)
   public Data getUser(@PathParam("userid") int userid){
      return dataDao.getUser(userid);
   }
   @GET
   @Path("/data/connection/{passwd}")
   @Produces(MediaType.APPLICATION_XML)
    public void connectToDb(@PathParam("passwd") String passwd){
	   //dataDao.connectToDb(passwd);
	   targetDb.connect(passwd);
   }

   @PUT
   @Path("/data")
   //@Produces(MediaType.APPLICATION_XML)
   @Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
   //@Produces("text/plain")
   @Consumes(MediaType.APPLICATION_JSON)
   //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String createUser(@FormParam("id") int id,
      @FormParam("command") String command,
      @FormParam("profession") String profession,
      @Context HttpServletResponse servletResponse) throws IOException{
      Data user = new Data(id, command, profession);
      int result = dataDao.addUser(user);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @POST
   @Path("/data")
   //@Produces("text/plain")
   @Produces(MediaType.APPLICATION_XML)
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String updateUser(@FormParam("id") int id,
      @FormParam("command") String command,
      //@FormParam("profession") String profession,
      @Context HttpServletResponse servletResponse) throws IOException{
      //Data user = new Data(id, command, profession);
	   Data user = new Data(id, command, "test");
      int result = dataDao.updateUser(user);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @DELETE
   @Path("/data/{userid}")
   @Produces(MediaType.APPLICATION_XML)
   public String deleteUser(@PathParam("userid") int userid){
      int result = dataDao.deleteUser(userid);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @OPTIONS
   @Path("/data")
   @Produces(MediaType.APPLICATION_XML)
   public String getSupportedOperations(){
      return "<operations>GET, PUT, POST, DELETE</operations>";
   }
}