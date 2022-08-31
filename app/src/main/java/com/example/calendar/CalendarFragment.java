package com.example.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Helper;
import com.example.finalproject.R;
import com.example.task.Task;
import com.example.task.TaskOverview;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private static final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    private static final String FOLDER_POSITION = "position";
    private static final String FUNCTION_KEY = "function key";
    private static final String DATE = "date";
    private static final String TASK_ITEM = "task";

    private TextView textViewMonthYear;
    private ImageView imageViewBackMonth, imageViewNextMonth;
    private RecyclerView calendarRecyclerView;
    CalendarAdapter calendarAdapter;

    private LocalDate selectedDate;

    private View view;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        initView();
        selectedDate = LocalDate.now();
        setMonthView();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        textViewMonthYear.setText(monthYearFromDate(selectedDate));
        ArrayList<Dates> daysInMonth = daysInMonthArray(selectedDate);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarAdapter = new CalendarAdapter(daysInMonth, position -> onClickGoToTaskOverviewDaily(position));
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onClickGoToTaskOverviewDaily(int position) {
        String dayMonthYear = getDateSavedFormat(selectedDate, position);
        Intent intent = new Intent(view.getContext(), TaskOverview.class);
        Bundle args = new Bundle();
        args.putSerializable(TASK_ITEM, Helper.loadDates(getContext(), dayMonthYear));
        args.putString(FUNCTION_KEY, "daily");
        args.putInt(FOLDER_POSITION, position);
        args.putString(DATE, getDateViewFormat(selectedDate, position));

        intent.putExtra("BUNDLE", args);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateViewFormat(LocalDate selectedDate, int position) {
        String res = String.format(Locale.getDefault(), "%02d", position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/MM/yyyy");
        return  res +selectedDate.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getDateSavedFormat(LocalDate date, int position){
        String res = String.format(Locale.getDefault(), "%02d", position);
        res += monthYearFromDate_get_number(date);
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String monthYearFromDate_get_number(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Dates> daysInMonthArray(LocalDate date) {
        ArrayList<Dates> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add(new Dates("", ""));
            }
            else
            {
                daysInMonthArray.add(new Dates("", String.valueOf(i - dayOfWeek)));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        textViewMonthYear = view.findViewById(R.id.text_view_month_year);
        imageViewNextMonth = view.findViewById(R.id.image_view_next_month);
        imageViewBackMonth = view.findViewById(R.id.image_view_back_month);
        calendarRecyclerView = view.findViewById(R.id.recycler_view_calendar);

        imageViewBackMonth.setOnClickListener(v -> previousMonthAction(v));

        imageViewNextMonth.setOnClickListener(v -> nextMonthAction(v));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("projectTaskResult", "requestCode: " + requestCode + " resultCode: " + resultCode);
        if (requestCode == GO_TO_TASKOVERVIEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getBundleExtra("BUNDLE");
            Log.d("projectTaskResult", "bundle exists: " + (bundle != null));
            int position = bundle.getInt(FOLDER_POSITION, -1);
            if (position != -1) {
                Helper.saveDates(getContext(), getDateSavedFormat(selectedDate, position), (ArrayList<Task>) bundle.getSerializable(TASK_ITEM));
            }
        }
    }
}
