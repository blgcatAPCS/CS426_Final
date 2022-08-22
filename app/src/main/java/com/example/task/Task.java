package com.example.task;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Task {
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
}
