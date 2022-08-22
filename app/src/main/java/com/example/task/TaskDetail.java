package com.example.task;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Helper;
import com.example.finalproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDetail extends AppCompatActivity {
    private EditText taskName, description;
    private TextView deadline;
    private Button saveButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        loadComponent();

        setupSelectingDate();
        saveButton.setOnClickListener(v -> {
            Date selectedDeadline = null;
            try {
                selectedDeadline = new SimpleDateFormat("dd/MM/yyyy").parse(deadline.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date curDate = Calendar.getInstance().getTime();
            Log.d("saveButton", "selected Deadline: " + selectedDeadline.toString());
            Log.d("saveButton", "curDate: " + curDate);
            if (!selectedDeadline.after(curDate)) {
                deadline.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "You must select today or the day after", Toast.LENGTH_SHORT).show();
            } else {
                Task task = new Task(taskName.getText().toString(), selectedDeadline, description.getText().toString());
                if (Helper.tasks == null) {
                    Helper.tasks = new ArrayList<>();
                }
                Helper.tasks.add(task);
                Log.d("saveButton", Helper.tasks.toString());
            }
        });
    }

    private void loadComponent() {
        taskName = findViewById(R.id.edit_text_task_name);
        deadline = findViewById(R.id.text_view_select_deadline);
        description = findViewById(R.id.edit_text_description);
        saveButton = findViewById(R.id.button_save_task);
    }

    private void setupSelectingDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DATE);

        deadline.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(TaskDetail.this, (view, year1, month1, dayOfMonth) -> {
                Log.d("deadlineOnClickListener", "go in onDateSet");
                month1++;
                String date = dayOfMonth + "/" + month1 + "/" + year1;
                deadline.setText(date);
            }, year, month, day);
            dialog.show();
        });
    }
}