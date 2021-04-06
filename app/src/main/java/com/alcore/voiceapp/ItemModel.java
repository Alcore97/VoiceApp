package com.alcore.voiceapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemModel implements Serializable {

   public String name;
   public List<String> products;

   public ItemModel(){
      this.name = "";
      this.products = new ArrayList<>();
   }

   public ItemModel(String name){
      this();
      this.name = name;
   }

   public String getName() {
      return name;
   }

}

