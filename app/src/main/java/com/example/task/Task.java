package com.example.task;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class Task implements Serializable {
    private String name;
    private LocalDate deadline;
    private String description;
    private boolean done;

    public Task (String _name, LocalDate _deadLine, String _description){
        setName(_name);
        setDeadline(_deadLine);
        setDescription(_description);
        setDone(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task(String _name, String _deadline, String _description){
        setName(_name);
        setDeadline(_deadline);
        setDescription(_description);
        setDone(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task (String _name, Date _deadline, String _description){
        setName(_name);
        setDeadline(_deadline.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        setDescription(_description);
        setDone(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDeadline(String deadline){
        this.deadline = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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
    public String getFormattedDate(){
        return deadline.format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
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
