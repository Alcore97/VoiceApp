package com.alcore.voiceapp;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

   public String name;

   public ProductModel(){
      this.name = "";
   }
   public ProductModel(String name){
      this.name = name;
   }
}
