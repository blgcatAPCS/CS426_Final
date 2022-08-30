package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendar.CalendarAdapter;
import com.example.calendar.Dates;
import com.example.calendar.OnItemListener;
import com.example.finalproject.R;
import com.example.task.Task;
import com.example.task.TaskOverview;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    private static final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    private static final String TASK_ITEM = "task";
    private static final String FOLDER_POSITION = "position";
    private static final String FUNCTION_KEY = "function key";

    private TextView textViewMonthYear;
    private ImageView imageViewBackMonth, imageViewNextMonth;
    private RecyclerView calendarRecyclerView;
    CalendarAdapter calendarAdapter;

    private ArrayList<Dates> listOfDays;

    private LocalDate selectedDate;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        listOfDays = Helper.loadDates(this.getActivity());
        initView();
        selectedDate = LocalDate.now();
        setMonthView();
        return view;
    }

    private void setMonthView() {
        textViewMonthYear.setText(monthYearFromDate(selectedDate));
        ArrayList<Dates> daysInMonth = daysInMonthArray(selectedDate);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarAdapter = new CalendarAdapter(daysInMonth, new OnItemListener() {
            @Override
            public void onItemClick(Dates date, int position) {
                onClickGoToTaskOverviewDaily(date, position);
            }
        });
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void onClickGoToTaskOverviewDaily(Dates date, int position) {
        Intent intent = new Intent(view.getContext(), TaskOverview.class);
        Bundle args = new Bundle();
        args.putSerializable(TASK_ITEM, date.listOfTasks);
        args.putInt(FOLDER_POSITION, position);
        args.putString(FUNCTION_KEY, "daily");

        intent.putExtra("BUNDLE", args);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }

    private static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

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

    private void initView() {
        textViewMonthYear = view.findViewById(R.id.text_view_month_year);
        imageViewNextMonth = view.findViewById(R.id.image_view_next_month);
        imageViewBackMonth = view.findViewById(R.id.image_view_back_month);
        calendarRecyclerView = view.findViewById(R.id.recycler_view_calendar);

        imageViewBackMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(v);
            }
        });

        imageViewNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(v);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("projectTaskResult", "requestCode: " + requestCode + " resultCode: " + resultCode);
        if (requestCode == GO_TO_TASKOVERVIEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getBundleExtra("BUNDLE");
            Log.d("projectTaskResult", "bundle exists: " + (bundle != null));
            int position = bundle.getInt(FOLDER_POSITION, -1);
            if (position != -1) {
                listOfDays.get(position).listOfTasks = ((ArrayList<Task>) bundle.getSerializable(TASK_ITEM));
            }
            Log.d("projectTaskResult", "position: " + position + " listOfFolders: " + listOfDays.get(position).getListOfTasks());
        }
    }

}
