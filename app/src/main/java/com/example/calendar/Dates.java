package com.example.calendar;

import com.example.task.Task;

import java.util.ArrayList;

public class Dates {
    private String dayOfWeek;
    private String dayOfMonth;
    public ArrayList<Task> listOfTasks;

    public Dates(String dayOfWeek, String dayOfMonth){
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        listOfTasks = null;
    }

    public String getDayOfWeek(){
        return dayOfWeek;
    }

    public String getDayOfMonth(){
        return dayOfMonth;
    }

    public ArrayList<Task> getListOfTasks(){
        return listOfTasks;
    }

    public void setListOfTasks(ArrayList<Task> serializable) {
        listOfTasks = serializable;
    }
}