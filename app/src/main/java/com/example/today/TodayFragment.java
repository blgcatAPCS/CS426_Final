package com.example.today;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Helper;
import com.example.finalproject.R;
import com.example.folders.Folder;
import com.example.task.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayFragment extends Fragment {
    private ArrayList<TodayTask> fromProjects, fromCalendar;
    private RecyclerView rcvFromProjects, rcvFromCalendar;
    private TodayTaskAdapter adapterFromProjects, adapterFromCalendar;
    private View view;
    private Date curDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);
        loadData();
        initView();

        return view;
    }

    private void initView() {
        rcvFromProjects = view.findViewById(R.id.recycler_view_from_projects);
        Log.d("initView", "rcv exists: " + (rcvFromProjects != null));

        if (rcvFromProjects != null) {
            rcvFromProjects.setLayoutManager(new LinearLayoutManager(view.getContext()));
            adapterFromProjects = new TodayTaskAdapter(fromProjects, view.getContext());
            rcvFromProjects.setAdapter(adapterFromProjects);
        }

        rcvFromCalendar = view.findViewById(R.id.recycler_view_from_calendar);
        if (rcvFromCalendar!=null){
            rcvFromCalendar.setLayoutManager(new LinearLayoutManager(view.getContext()));
            adapterFromCalendar = new TodayTaskAdapter(fromCalendar, view.getContext());
            rcvFromCalendar.setAdapter(adapterFromCalendar);
        }
    }

    private void loadData() {
        //From projects
        ArrayList<Folder> projects = Helper.loadFolders(getActivity());
        fromProjects = new ArrayList<>();
        curDate = new Date();
        for (int i = 0; i < projects.size(); ++i) {
            ArrayList<Task> tasks = projects.get(i).getListOfTasks();
            for (int j = 0; j < tasks.size(); ++j) {
                Task task = tasks.get(j);
                if (task.getDeadline().getDate() == curDate.getDate()) {
                    fromProjects.add(new TodayTask(task, i, projects.get(i).getName(), j));
                }
            }
        }

        //From daily planner
        fromCalendar=new ArrayList<>();
        ArrayList<Task> dailyPlanner = Helper.loadDates(getContext(), getSavedDayFormat(curDate));
        for (int i=0;i<dailyPlanner.size();++i) {
            fromCalendar.add(new TodayTask(dailyPlanner.get(i), -1, "Daily Planner", i));
        }

        Log.d("todayTask", fromProjects.toString());
    }

    private String getSavedDayFormat(Date date){
        return new SimpleDateFormat("ddMMyyyy").format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveData() {
        ArrayList<Folder> folders = Helper.loadFolders(this.getActivity());
        ArrayList<Task> dailyPlanner = new ArrayList<>();
        for (TodayTask todayTask : fromProjects) {
            Log.d("todayTask", "projectPos: " + todayTask.getiProject() + " taskPos: " + todayTask.getiTask() + " todayTask: " + todayTask.getTask().toString());
            int i=todayTask.getiProject();
            if (i!=-1){
                folders.get(i).getListOfTasks().set(todayTask.getiTask(), todayTask.getTask());
            }
        }
        for (TodayTask todayTask : fromCalendar) dailyPlanner.add(todayTask.getTask());
        Helper.saveData(this.getActivity(), folders);
        Helper.saveDates(getActivity(), getSavedDayFormat(curDate), dailyPlanner);
    }
}
