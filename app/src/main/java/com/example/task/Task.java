package com.example.task;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.finalproject.Helper;
import com.example.Priority.Priority;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
    private final int NUM_PROPERTY = 5;

    private String name;
    private Date deadline;
    private String description;
    private boolean done;
    private Priority priority;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, Date _deadline, String _description, Priority _priority, boolean _done) {
        setName(_name);
        setDeadline(_deadline);
        setDescription(_description);
        setDone(_done);
        setPriority(_priority);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, Date _deadline, String _description) throws ParseException {
        this(_name, _deadline, _description, Priority.LOW, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, Date _deadline, String _description, Priority _priority){
        this(_name, _deadline, _description, _priority, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, Date _deadline, String _description, boolean _done){
        this(_name, _deadline, _description, Priority.LOW, _done);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(ArrayList<String> info) throws ParseException {
        Log.d("TaskArrayList", "info: " + info);
        if (info.size()!=NUM_PROPERTY) throw new IllegalArgumentException("Info not enough argument");
        setName(info.get(0));
        setDeadline(info.get(1));
        setPriority(Helper.getPriority(Integer.valueOf(info.get(2))));
        setDescription(info.get(3));
        setDone(Boolean.parseBoolean(info.get(4)));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDeadline(String deadline) throws ParseException {
        this.deadline = new SimpleDateFormat("dd/MM/yyyy").parse(deadline);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> toArrayListString(){
        Log.d("TaskToArray",String.valueOf(priority.getIntValue()));
        return new ArrayList<String>(){{
            add(name);
            add(Helper.dateToString(deadline));
            add(String.valueOf(priority.getIntValue()));
            add(description);
            add(String.valueOf(done));
        }};
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", deadline=" + deadline +
                ", description='" + description + '\'' +
                ", done=" + done +
                ", priority=" + priority +
                '}';
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setPriority(int priority){
        switch (priority) {
            case 3:
                this.priority = Priority.LOW;
                break;

            case 2:
                this.priority = Priority.MEDIUM;
                break;

            case 1:
                this.priority = Priority.HIGH;
                break;

            default:
                throw new IllegalArgumentException("The value of priority is invalid!");
        }
    }
}
