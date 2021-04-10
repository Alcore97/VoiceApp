package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

   public String name;
   public boolean status = false;

   public ProductModel(){
      this.name = "";
   }
   public ProductModel(String name){
      this.name = name;
   }
   public Boolean getStatus(){
      return status;
   }

   public void setName(String name){ this.name = name;}
   public void setStatus(boolean status) {
      this.status = status;
   }

   public String getName() {
      return name;
   }
}
