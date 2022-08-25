package com.example.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.text.ParseException;
import java.util.ArrayList;

public class TaskOverview extends AppCompatActivity implements TaskAdapter.CallbackInterface {
    private RecyclerView rcvTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;

    final int ADD_TASK_REQUEST = 0;
    final int EDIT_TASK_REQUEST = 1;

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
            intent.putExtra("Add", true);
            startActivityForResult(intent, ADD_TASK_REQUEST);
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
        Log.d("TaskDetailResult", "Request Code: " + requestCode + " Result code: " + resultCode);
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            ArrayList<String> newTaskInfo = data.getStringArrayListExtra("newTask");
            if (newTaskInfo != null) {
                try {
                    addTask(newTaskInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            ArrayList<String> newTaskInfo = data.getStringArrayListExtra("newTask");
            if (newTaskInfo != null) {
                updateTask(data.getIntExtra("position", -1), newTaskInfo);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTask(int position, ArrayList<String> newTaskInfo) {
        try {
            tasks.set(position, new Task(newTaskInfo.get(0), newTaskInfo.get(1), newTaskInfo.get(2)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        taskAdapter.notifyItemChanged(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addTask(ArrayList<String> newTaskInfo) throws ParseException {
        Task task = new Task(newTaskInfo.get(0), newTaskInfo.get(1), newTaskInfo.get(2));
        tasks.add(task);
        taskAdapter.notifyItemInserted(tasks.size() - 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onHandleSelection(int position, Task task) {
        Log.d("editTask", task.toString());
        Intent intent = new Intent(getApplicationContext(), TaskDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("task", task.toArrayListString());
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }
}