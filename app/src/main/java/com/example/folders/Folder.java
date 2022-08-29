package com.example.folders;

import com.example.task.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {
    String name;
    ArrayList<Task> listOfTasks;

    public Folder(String name) {
        this.name = name;
        listOfTasks = new ArrayList<>();
    }
}
