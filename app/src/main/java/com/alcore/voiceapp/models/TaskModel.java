package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.List;

public class TaskModel implements Serializable {

    public String name;

    public TaskModel(){
        this.name = "";
    }
    public TaskModel(String name){
        this.name = name;
    }
}
