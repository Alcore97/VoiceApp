package com.alcore.voiceapp.models;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.Serializable;
import java.util.List;

public class ProductModel extends SugarRecord implements Serializable {

   public String name;
   public boolean status = false;
   public long idlist;

   public ProductModel(){
      this.name = "";
   }
   public ProductModel(String name){
      this.name = name;
   }
   public Boolean getStatus(){
      return status;
   }

   public static List<ProductModel> getIdlist(long idlist){

      return Select.from(ProductModel.class).where(Condition.prop("IDLIST").eq(idlist)).list();
   }


   public void setIdlist(long id){
      this.idlist = id;
   }

   public void setName(String name){ this.name = name;}
   public void setStatus(boolean status) {
      this.status = status;
   }

   public String getName() {
      return name;
   }
}
