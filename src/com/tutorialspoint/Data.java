package com.tutorialspoint;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "data")
public class Data implements Serializable {

   private static final long serialVersionUID = 1L;
   private int id;
   private String command;
   private String profession;

   public Data(){}

   public Data(int id, String command, String profession){
      this.id = id;
      this.command = command;
      this.profession = profession;
   }

   public int getId() {
      return id;
   }
   @XmlElement
   public void setId(int id) {
      this.id = id;
   }
   public String getCommand() {
      return command;
   }
   @XmlElement
      public void setCommand(String comand) {
      this.command = command;
   }
   public String getProfession() {
      return profession;
   }
   @XmlElement
   public void setProfession(String profession) {
      this.profession = profession;
   }	

   @Override
   public boolean equals(Object object){
      if(object == null){
         return false;
      }else if(!(object instanceof Data)){
         return false;
      }else {
         Data user = (Data)object;
         if(id == user.getId()
            && command.equals(user.getCommand())
            && profession.equals(user.getProfession())
         ){
            return true;
         }			
      }
      return false;
   }	
}