package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.finalproject.Helper;
import com.example.finalproject.R;

public class TaskOverview extends AppCompatActivity {
    private RecyclerView rcvTasks;
    private TaskAdapter taskAdapter;

    private Button addTaskButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        loadComponent();

        if (rcvTasks!=null){
            taskAdapter = new TaskAdapter(this, Helper.tasks);
            rcvTasks.setAdapter(taskAdapter);
            rcvTasks.setLayoutManager(new LinearLayoutManager(this));
        }

        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TaskDetail.class);
            startActivity(intent);
            taskAdapter.notifyAll();
        });
    }

    private void loadComponent() {
        rcvTasks = findViewById(R.id.rcv_tasks);
        addTaskButton = findViewById(R.id.button_add_task);
    }
}