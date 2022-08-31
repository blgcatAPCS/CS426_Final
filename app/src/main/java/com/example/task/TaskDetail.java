package com.example.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Priority.PrioritySpinnerAdapter;
import com.example.finalproject.Helper;
import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetail extends AppCompatActivity {
    private static final String FUNCTION_KEY = "function key";
    private static final String DATE = "date";
    private String function = "";
    private EditText taskName, description;
    private TextView deadline;
    private Button saveButton, cancelButton;
    private ActionBar actionBar;
    private Spinner prioritySpinner;
    private String date;
    private int priority;
    private int position;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        loadComponent();
        loadData();
        setupSelectingDate();
        saveButton.setOnClickListener(v -> {
            if (!checkValid()) return;
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("newTask", new ArrayList<String>() {
                {
                    add(taskName.getText().toString());
                    add(deadline.getText().toString());
                    add(String.valueOf(priority));
                    add(description.getText().toString());
                    add("false");
                }
            });
            resultIntent.putExtra("position", position);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
        });
    }

    private void loadData() {
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        Log.d("loadData", String.valueOf((bundle!=null)));
        function = bundle.getString(FUNCTION_KEY);
        date = bundle.getString(DATE);
        if (bundle.getBoolean("Add", false)) {
            addActivity();
        } else {
            updateActivity();
        }
        Log.d("loadData",function);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkValid() {
        String stringDeadline = deadline.getText().toString();
        if (taskName.getText() == null || taskName.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Your task must have name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (stringDeadline.length() == 0) {
            deadline.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "You must select deadline.", Toast.LENGTH_SHORT).show();
            return false;
        }
        Date selectedDeadline = Helper.stringToDate(deadline.getText().toString());
        Date curDate = Calendar.getInstance().getTime();
        if (!Helper.isSameDay(selectedDeadline, curDate) && selectedDeadline.before(curDate)) {
            deadline.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "You must select today or the day after", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadComponent() {
        taskName = findViewById(R.id.edit_text_task_name);
        deadline = findViewById(R.id.text_view_select_deadline);
        description = findViewById(R.id.edit_text_description);
        saveButton = findViewById(R.id.button_save_task);
        cancelButton = findViewById(R.id.button_cancel);
        loadPrioritySpinner();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadPrioritySpinner() {
        int[] icon = {R.drawable.ic_baseline_trending_up_24, R.drawable.ic_baseline_trending_flat_24, R.drawable.ic_baseline_trending_down_24};
        String[] priorityText = {"High", "Medium", "Low"};
        prioritySpinner = findViewById(R.id.spinner_priority);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PrioritySpinnerAdapter adapter = new PrioritySpinnerAdapter(getApplicationContext(), icon, priorityText);
        prioritySpinner.setAdapter(adapter);
    }

    private void updateActivity() {
        saveButton.setText("Save");
        ArrayList<String> task = getIntent().getStringArrayListExtra("newTask");
        if (task != null) {
            taskName.setText(task.get(0));
            deadline.setText(task.get(1));
            priority = Integer.valueOf(task.get(2));
            prioritySpinner.setSelection(priority - 1);
            description.setText(task.get(3));
        }
        position = getIntent().getIntExtra("position", -1);
        Log.d("updateActivity", "position: " + position + ", task: " + task);
    }

    private void addActivity() {
        saveButton.setText("Add");
        prioritySpinner.setSelection(0);
    }

    private void setupSelectingDate() {
        if (function.equals("folder")) {
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DATE);

            deadline.setOnClickListener(v -> {
                DatePickerDialog dialog = new DatePickerDialog(TaskDetail.this, (view, year1, month1, dayOfMonth) -> {
                    month1++;
                    String date = dayOfMonth + "/";
                    if (month1 < 10) {
                        date += "0" + month1 + "/" + year1;
                    } else date += month1 + "/" + year1;
                    deadline.setText(date);
                }, year, month, day);
                dialog.show();
                deadline.setTextColor(Color.BLACK);
            });
        } else {
            deadline.setText(date);
        }
    }

    private void calendarDialog(View v, int year, int month, int day) {
        DatePickerDialog dialog = new DatePickerDialog(TaskDetail.this, (view, year1, month1, dayOfMonth) -> {
            month1++;
            String date = dayOfMonth + "/";
            if (month1 < 10) {
                date += "0" + month1 + "/" + year1;
            } else date += month1 + "/" + year1;
            deadline.setText(date);
        }, year, month, day);
        dialog.show();
        deadline.setTextColor(Color.BLACK);
    }

    private void timerDialog(View v, int hour, int minute) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int hour1 = selectedHour;
                int minute1 = selectedMinute;
                deadline.setText(String.format(Locale.getDefault(), "%02d:%02d", hour1, minute1));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}