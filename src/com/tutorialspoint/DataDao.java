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

public class DataDao {
   public List<Data> getAllUsers(){
      List<Data> dataList = null;
      try {
         File file = new File("Users.dat");
         if (!file.exists()) {
            Data data = new Data(1, "get", "data");
            dataList = new ArrayList<Data>();
            dataList.add(data);
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