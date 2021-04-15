package com.alcore.voiceapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.models.EventModel;

import org.joda.time.DateTime;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    public interface EventController{
        void OnClickEvent(int position);
    }

    private final List<EventModel> product_list;
    private final EventController controller;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel list = product_list.get(position);
        holder.nameevent.setText(list.name);

        DateTime aux = new DateTime(list.date);
        holder.date.setText(aux.toString("dd MMM YYYY HH:mm"));
    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public EventAdapter(List<EventModel> productlist, EventController controller){
        this.product_list= productlist;
        this.controller = controller;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView nameevent;
        public final TextView date;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            nameevent = itemView.findViewById(R.id.eventname);
            date = itemView.findViewById(R.id.label_date);

        }

        @Override
        public void onClick(View v) {
            controller.OnClickEvent(getAdapterPosition());
        }
    }
}
