package com.example.folders;

import com.example.task.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {
    private String name;
    private ArrayList<Task> listOfTasks;

    public Folder(String name) {
        this.setName(name);
        setListOfTasks(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void setListOfTasks(ArrayList<Task> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }
}
