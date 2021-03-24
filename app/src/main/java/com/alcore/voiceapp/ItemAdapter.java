package com.alcore.voiceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    public interface ItemController {
        void OnClickItem(int position);
    }

    private final List<ItemModel> shopping_list;
    private final ItemController controller;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel list = shopping_list.get(position);
        holder.item.setText(list.name);
    }

    @Override
    public int getItemCount() {
        return shopping_list.size();
    }

    public ItemAdapter(List<ItemModel> shoppinglist, ItemController controller){
        this.shopping_list = shoppinglist;
        this.controller = controller;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView item;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            item = itemView.findViewById(R.id.itemmodel);
        }

        @Override
        public void onClick(View v) {
            controller.OnClickItem(getAdapterPosition());
        }
    }
}
