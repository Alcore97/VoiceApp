package Database;

import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.models.ProductModel;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;

public class DB {

    private DB(){}

    private static ArrayList<ItemModel> list = null;
    private static ArrayList<TaskModel> list2 = null;

    public static ArrayList<ItemModel> getShoppingList(){
        if(list == null){
            list = new ArrayList<>();
            list.add(new ItemModel("Caprabo"));
            list.get(0).products.add(new ProductModel("Tomato"));
            list.get(0).products.add(new ProductModel("Lemon"));
            list.get(0).products.add(new ProductModel("Water"));
            list.get(0).products.add(new ProductModel("Paper"));

            list.add(new ItemModel("Mediamarkt"));
            list.get(1).products.add(new ProductModel("Tomato"));
            list.get(1).products.add(new ProductModel("Lemon"));
            list.get(1).products.add(new ProductModel("Water"));
            list.get(1).products.add(new ProductModel("Paper"));

            list.add(new ItemModel("Amazon"));
            list.get(2).products.add(new ProductModel("Tomato"));
            list.get(2).products.add(new ProductModel("Lemon"));
            list.get(2).products.add(new ProductModel("Water"));
            list.get(2).products.add(new ProductModel("Paper"));

            list.add(new ItemModel("Tintoreria"));
            list.get(3).products.add(new ProductModel("Tomato"));
            list.get(3).products.add(new ProductModel("Lemon"));
            list.get(3).products.add(new ProductModel("Water"));
            list.get(3).products.add(new ProductModel("Paper"));

            list.add(new ItemModel("Glovo"));
            list.get(4).products.add(new ProductModel("Tomato"));
            list.get(4).products.add(new ProductModel("Lemon"));
            list.get(4).products.add(new ProductModel("Water"));
            list.get(4).products.add(new ProductModel("Paper"));
        }
        return list;
    }
    public static  ArrayList<TaskModel> getTaskList(){
        if(list2 == null){
            list2 = new ArrayList<>();
            list2.add(new TaskModel("Run"));
            list2.add(new TaskModel("Gym"));
            list2.add(new TaskModel("Study"));
            list2.add(new TaskModel("Meet"));
            list2.add(new TaskModel("Play"));
            list2.add(new TaskModel("Ask a friend"));
            list2.add(new TaskModel("Go to the beach"));
        }
        return list2;
    }


}
