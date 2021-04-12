package com.alcore.voiceapp.Database;

import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.models.ProductModel;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;

public class DB  {

    private DB(){}

    private static ArrayList<ItemModel> listitems;
    private static ArrayList<TaskModel> listproducts;



    public static ArrayList<ItemModel> getShoppingList(){
        if(listitems == null){
            /*ItemModel.deleteAll(ItemModel.class);

            ItemModel lista1 = new ItemModel("Mediamarkt");
            lista1.getProducts().add(new ProductModel("Computer"));
            lista1.save();*/

            listitems = (ArrayList<ItemModel>) ItemModel.listAll(ItemModel.class);

        }
        return listitems;
    }
    public static  ArrayList<TaskModel> getTaskList(){
        if(listproducts == null){
           /* TaskModel.deleteAll(ItemModel.class);

            TaskModel lista1 = new TaskModel("Estudiar");
            lista1.save();*/

            listproducts = (ArrayList<TaskModel>) TaskModel.listAll(TaskModel.class);

        }
        return listproducts;
    }


}
