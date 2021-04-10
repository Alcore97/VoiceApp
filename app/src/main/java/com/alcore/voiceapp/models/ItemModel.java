package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemModel implements Serializable {

   public String name;
   public List<ProductModel> products;

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

   public List<ProductModel> getProducts(){
      return products;
   }

}

