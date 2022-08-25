package com.example.task;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
    private String name;
    private Date deadline;
    private String description;
    private boolean done;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, String _deadline, String _description) throws ParseException {
        setName(_name);
        setDeadline(_deadline);
        setDescription(_description);
        setDone(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task (String _name, Date _deadline, String _description){
        setName(_name);
        setDeadline(_deadline);
        setDescription(_description);
        setDone(false);
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
    public String getFormattedDateDeadline(){
        return new SimpleDateFormat("dd/MM/yyyy").format(deadline);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> toArrayListString(){
        return new ArrayList<String>(){{
            add(name);
            add(getFormattedDateDeadline());
            add(description);
        }};
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", deadline=" + deadline +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }
}
