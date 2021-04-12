package com.alcore.voiceapp.Database;

import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.models.ProductModel;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;

public class DB  {

    private DB(){}

    private static ArrayList<ItemModel> listitems;
    private static ArrayList<TaskModel> listproducts = null;



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
        /*if(list2 == null){
            list2 = new ArrayList<>();
            list2.add(new TaskModel("Run"));
            list2.add(new TaskModel("Gym"));
            list2.add(new TaskModel("Study"));
            list2.add(new TaskModel("Meet"));
            list2.add(new TaskModel("Play"));
            list2.add(new TaskModel("Ask a friend"));
            list2.add(new TaskModel("Go to the beach"));
        }
        return list2;*/
        return new ArrayList<>();
    }


}
