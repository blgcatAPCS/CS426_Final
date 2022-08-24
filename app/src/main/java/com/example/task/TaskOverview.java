package com.example.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class TaskOverview extends AppCompatActivity {
    private RecyclerView rcvTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;

    final int TASK_DETAIL_REQUEST = 0;

    private Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        loadComponent();

        if (rcvTasks != null) {
            taskAdapter = new TaskAdapter(this, tasks);
            rcvTasks.setAdapter(taskAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rcvTasks.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvTasks.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());

            rcvTasks.addItemDecoration(dividerItemDecoration);
        }

        registerForContextMenu(rcvTasks);

        addTaskButton.setOnClickListener(v -> {
            Log.d("addTaskButton", "goIn");
            Intent intent = new Intent(v.getContext(), TaskDetail.class);
            startActivityForResult(intent, TASK_DETAIL_REQUEST);
        });
    }

    private void loadComponent() {
        rcvTasks = findViewById(R.id.rcv_tasks);
        addTaskButton = findViewById(R.id.button_add_task);

        tasks = new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_DETAIL_REQUEST && resultCode == Activity.RESULT_OK) {
            ArrayList<String> newTaskInfo = data.getStringArrayListExtra("newTask");
            if (newTaskInfo != null) {
                addTask(newTaskInfo);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addTask(ArrayList<String> newTaskInfo) {
        Task task = new Task(newTaskInfo.get(0), newTaskInfo.get(1), newTaskInfo.get(2));
        tasks.add(task);
        taskAdapter.notifyItemInserted(tasks.size() - 1);
    }
}