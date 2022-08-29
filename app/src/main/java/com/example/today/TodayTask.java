package com.example.today;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.Priority.Priority;
import com.example.task.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodayTask {
    private Task task;
    private int iProject;
    private String projectName;
    private int iTask;

    public TodayTask(Task task, int iProject, String projectName, int iTask) {
        this.task = task;
        this.iProject = iProject;
        this.projectName = projectName;
        this.iTask = iTask;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getName() {
        return task.getName();
    }

    public void setName(String name) {
        this.task.setName(name);
    }

    public Date getDeadline() {
        return task.getDeadline();
    }

    public void setDeadline(Date deadline) {
        this.task.setDeadline(deadline);
    }

    public String getDescription() {
        return task.getDescription();
    }

    public void setDescription(String description) {
        this.task.setDescription(description);
    }

    public boolean isDone() {
        return task.isDone();
    }

    public void setDone(boolean done) {
        this.task.setDone(done);
    }

    public Priority getPriority() {
        return task.getPriority();
    }

    public void setPriority(Priority priority) {
        this.task.setPriority(priority);
    }

    public int getiProject() {
        return iProject;
    }

    public void setiProject(int iProject) {
        this.iProject = iProject;
    }

    public int getiTask() {
        return iTask;
    }

    public void setiTask(int iTask) {
        this.iTask = iTask;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
