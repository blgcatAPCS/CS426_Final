package com.example.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.text.ParseException;
import java.util.ArrayList;

public class TaskOverview extends AppCompatActivity implements TaskAdapter.CallbackInterface {
    private final String LOAD_TASKS = "tasks";
    private static final String FUNCTION_KEY = "function key";
    private final String FOLDER_POSITION = "position";
    private final String TASK_ITEM = "task";
    private final String DATE = "date";
    private final int ADD_TASK_REQUEST = 0;
    private final int EDIT_TASK_REQUEST = 1;
    private RecyclerView rcvTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;
    private ImageView addTaskButton;
    private int folderPosition;

    private String function = "";
    static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        loadData();
        if (tasks == null) tasks = new ArrayList<>();
        loadComponent();

        if (rcvTasks != null) {
            taskAdapter = new TaskAdapter(this, tasks);
            rcvTasks.setAdapter(taskAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rcvTasks.setLayoutManager(layoutManager);
        }

        registerForContextMenu(rcvTasks);

        addTaskButton.setOnClickListener(v -> {
            Log.d("addTaskButton", "goIn");
            Intent intent = new Intent(v.getContext(), TaskDetail.class);
            intent.putExtra("Add", true);
            Bundle bundle = new Bundle();
            bundle.putBoolean("Add", true);
            bundle.putString(FUNCTION_KEY, function);
            bundle.putString(DATE, date);
            intent.putExtra("BUNDLE", bundle);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");

        folderPosition = bundle.getInt(FOLDER_POSITION, -1);
        Log.d("loadData", "position: " + folderPosition);
        if (folderPosition == -1) {
            cancelTaskOverview();
        }
        tasks = (ArrayList<Task>) bundle.getSerializable(TASK_ITEM);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        date = bundle.getString(DATE);
        Log.d("loadData", "tasks: " + tasks);
        function = bundle.getString(FUNCTION_KEY);
    }

    private void cancelTaskOverview() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    private void loadComponent() {
        rcvTasks = findViewById(R.id.rcv_tasks);
        addTaskButton = findViewById(R.id.image_button_add_task);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TaskDetailResult", "Request Code: " + requestCode + " Result code: " + resultCode);
        ArrayList<String> newTaskInfo = data.getStringArrayListExtra("newTask");
        if (newTaskInfo == null) return;
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            addTask(newTaskInfo);
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                updateTask(data.getIntExtra("position", -1), newTaskInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addTask(ArrayList<String> newTaskInfo) {
        try {
            tasks.add(new Task(newTaskInfo));
            taskAdapter.notifyAdapterWholeDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTask(int position, ArrayList<String> newTaskInfo) throws ParseException {
        Log.d("updateTask", newTaskInfo.toString());
        if (position == -1) throw new IllegalArgumentException("Position invalid");
        taskAdapter.notifyAdapterItemMoved(position, new Task(newTaskInfo));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onHandleSelection(int position, Task task) {
        Log.d("editTask", task.toString());
        Intent intent = new Intent(getApplicationContext(), TaskDetail.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("newTask", task);
        bundle.putString(FUNCTION_KEY, function);
        bundle.putString(DATE, date);
        intent.putExtra("BUNDLE", bundle);
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    private void saveData() {
        Log.d("saveData", "folderPosition: " + folderPosition + " taskItem: " + tasks);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_ITEM, tasks);
        bundle.putInt(FOLDER_POSITION, folderPosition);
        resultIntent.putExtra("BUNDLE", bundle);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveData();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}