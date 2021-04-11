package com.alcore.voiceapp.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemModel extends SugarRecord implements Serializable {

   public String name;
   @Ignore
   private List<ProductModel> products;

   public ItemModel(){
      this.name = "";
      this.products = null;
   }

   public ItemModel(String name){
      this();
      this.name = name;
   }

   public ItemModel(String name, List<ProductModel> products){
      this.name = name;
      this.products = products;
   }

   @Override
   public long save() {
      long new_id = super.save();

      if(!getProducts().isEmpty()){
         getProducts().forEach(l -> {
            l.setIdlist(new_id);
            l.save();
         });
      }
      return new_id;

   }

   @Override
   public boolean delete() {
      super.delete();
      if(!getProducts().isEmpty()){
         getProducts().forEach(l ->{
            l.delete();
         });
      }
      return true;
   }

   public String getName() {
      return name;
   }



   public List<ProductModel> getProducts(){
      if(products == null) {
         products = new ArrayList<>();
      }
      if(products.isEmpty() && this.getId() != null) {
         products = ProductModel.getIdlist(this.getId());
      }
      return products;
   }

}

