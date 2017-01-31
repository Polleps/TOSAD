package com.tutorialspoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;


public class DataDao {
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver"; 
	//static final String DB_URL = "jdbc:mysql://localhost/EMP";	
	static final String DB_URL = "jdbc:oracle:thin:@ondora02.hu.nl:8521/cursus02.hu.nl";
	
	static final String USER = "TESTDB";
	static final String PASS = "TOSAD_2016_2D_TEAM6";
	
	public static void connectToDb(String password){
		Connection conn = null;
		//PASS = password;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			//Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			System.out.println("username: " + USER);
			System.out.println("password: " + password);
		    conn = DriverManager.getConnection(DB_URL,USER,password);
		    System.out.println("Connected");
		}catch(SQLException se){
	      //Handle errors for JDBC
			se.printStackTrace();
	    }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	}
	}
	
   public List<Data> getAllUsers(){
      List<Data> dataList = null;
      try {
         File file = new File("Users.dat");
         if (!file.exists()) {
            Data data = new Data(1, "get", "data");
            Data te = new Data(2, "set", "data");
            dataList = new ArrayList<Data>();
            dataList.add(data);
            dataList.add(te);
            saveUserList(dataList);
         }
         else{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataList = (List<Data>) ois.readObject();
            ois.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }		
      return dataList;
   }

   public Data getUser(int id){
      List<Data> users = getAllUsers();

      for(Data user: users){
         if(user.getId() == id){
            return user;
         }
      }
      return null;
   }

   public int addUser(Data pUser){
      List<Data> userList = getAllUsers();
      boolean userExists = false;
      for(Data user: userList){
         if(user.getId() == pUser.getId()){
            userExists = true;
            break;
         }
      }		
      if(!userExists){
         userList.add(pUser);
         saveUserList(userList);
         return 1;
      }
      return 0;
   }

   public int updateUser(Data pUser){
      List<Data> userList = getAllUsers();

      for(Data user: userList){
         if(user.getId() == pUser.getId()){
            int index = userList.indexOf(user);			
            userList.set(index, pUser);
            saveUserList(userList);
            return 1;
         }
      }		
      return 0;
   }

   public int deleteUser(int id){
      List<Data> userList = getAllUsers();

      for(Data user: userList){
         if(user.getId() == id){
            int index = userList.indexOf(user);			
            userList.remove(index);
            saveUserList(userList);
            return 1;   
         }
      }		
      return 0;
   }

   private void saveUserList(List<Data> userList){
      try {
         File file = new File("Users.dat");
         FileOutputStream fos;

         fos = new FileOutputStream(file);

         ObjectOutputStream oos = new ObjectOutputStream(fos);		
         oos.writeObject(userList);
         oos.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}