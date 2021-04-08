package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

   public String name;
   public Boolean status = false;

   public ProductModel(){
      this.name = "";
   }
   public ProductModel(String name){
      this.name = name;
   }
   public Boolean getStatus(){
      return status;
   }

   public void setStatus(Boolean status) {
      this.status = status;
   }

   public String getName() {
      return name;
   }
}
