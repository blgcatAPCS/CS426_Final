package com.example.task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.Helper;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDetail extends AppCompatActivity {
    private EditText taskName, description;
    private TextView deadline;
    private Button saveButton;

    private Task task;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        loadComponent();

        setupSelectingDate();
        saveButton.setOnClickListener(v -> {
            String stringDeadline = deadline.getText().toString();
            if (stringDeadline == null || stringDeadline.length()==0){
                Toast.makeText(getApplicationContext(), "You must select deadline", Toast.LENGTH_SHORT).show();
                return;
            }
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
                Log.d("saveButton", "successfully");
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("newTask", new ArrayList<String>(){
                    {
                        add(taskName.getText().toString());
                        add(deadline.getText().toString());
                        add(description.getText().toString());
                    }
                });
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void loadComponent() {
        taskName = findViewById(R.id.edit_text_task_name);
        deadline = findViewById(R.id.text_view_select_deadline);
        description = findViewById(R.id.edit_text_description);
        saveButton = findViewById(R.id.button_save_task);

        Toolbar toolbar = findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
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
                String date = dayOfMonth + "/";
                if (month1<10){
                    date+= "0" + month1 + "/" + year1;
                }
                else date += month1 + "/" + year1;
                deadline.setText(date);
            }, year, month, day);
            dialog.show();
        });
    }
}