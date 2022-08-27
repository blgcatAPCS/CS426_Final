package com.example.task;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.finalproject.Helper;
import com.example.finalproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskOverview extends AppCompatActivity implements TaskAdapter.CallbackInterface {
    private RecyclerView rcvTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;

    private final String LOAD_TASKS = "tasks";
    final int ADD_TASK_REQUEST = 0;
    final int EDIT_TASK_REQUEST = 1;

    private Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        loadData();
        if (tasks==null) tasks=new ArrayList<>();
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

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOAD_TASKS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAD_TASKS, null);
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        tasks = gson.fromJson(json, type);

        if (tasks == null)
            tasks = new ArrayList<>();

    }

    private void loadComponent() {
        rcvTasks = findViewById(R.id.rcv_tasks);
        addTaskButton = findViewById(R.id.button_add_task);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TaskDetailResult", "Request Code: " + requestCode + " Result code: " + resultCode);
        ArrayList<String> newTaskInfo = data.getStringArrayListExtra("newTask");
        if (newTaskInfo==null) return;
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                addTask(newTaskInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            updateTask(data.getIntExtra("position", -1), newTaskInfo);
        }
        Collections.sort(tasks, Comparator.comparing(Task::getDeadline).thenComparing(Task::getPriority, Comparator.reverseOrder()));
        taskAdapter.notifyItemRangeChanged(0, tasks.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTask(int position, ArrayList<String> newTaskInfo) {
        Log.d("updateTask", newTaskInfo.toString());
        try {
            tasks.set(position, new Task(newTaskInfo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addTask(ArrayList<String> newTaskInfo) throws ParseException {
        Task task = new Task(newTaskInfo);
        tasks.add(task);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onHandleSelection(int position, Task task) {
        Log.d("editTask", task.toString());
        Intent intent = new Intent(getApplicationContext(), TaskDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("newTask", task.toArrayListString());
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOAD_TASKS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("tasks", json);
        editor.apply();
        Log.d("save Data", json);
    }
}