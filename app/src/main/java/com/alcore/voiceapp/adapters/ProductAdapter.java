package com.alcore.voiceapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.models.ProductModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private final List<ProductModel> product_list;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel list = product_list.get(position);
        holder.check.setText(list.name);
        holder.check.setChecked(list.status);
    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public ProductAdapter(List<ProductModel> productlist){
        this.product_list= productlist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final CheckBox check;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            check = itemView.findViewById(R.id.checkbox);
        }
    }
}
