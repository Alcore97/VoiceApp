package com.alcore.voiceapp.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class TaskModel extends SugarRecord implements Serializable {

    public String name;
    public boolean status = false;

    public TaskModel(){
        this.name = "";
    }
    public TaskModel(String name){
        this.name = name;
    }
    public void setName(String name){ this.name = name;}
    public Boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
