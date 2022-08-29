package com.example.today;

import android.app.Application;
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

import java.util.ArrayList;
import java.util.Date;

public class TodayFragment extends Fragment {
    private ArrayList<TodayTask> todayTasks;
    private RecyclerView rcvTodayTask;
    private TodayTaskAdapter adapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);
        loadData();
        initView();

        return view;
    }

    private void initView() {
        rcvTodayTask = view.findViewById(R.id.recycler_view_today_task);
        Log.d("initView", "rcv exists: " + (rcvTodayTask != null));

        if (rcvTodayTask != null) {
            rcvTodayTask.setLayoutManager(new LinearLayoutManager(view.getContext()));
            adapter = new TodayTaskAdapter(todayTasks, view.getContext());
            rcvTodayTask.setAdapter(adapter);
        }
    }

    private void loadData() {
        ArrayList<Folder> projects = Helper.loadFolders(getActivity());
        todayTasks = new ArrayList<>();
        Date curDate = new Date();
        for (int i = 0; i < projects.size(); ++i) {
            ArrayList<Task> tasks = projects.get(i).getListOfTasks();
            for (int j = 0; j < tasks.size(); ++j) {
                Task task = tasks.get(j);
                if (task.getDeadline().getDate() == curDate.getDate()) {
                    todayTasks.add(new TodayTask(task, i, projects.get(i).getName(), j));
                }
            }
        }

        Log.d("todayTask", todayTasks.toString());
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
        for (TodayTask todayTask : todayTasks){
            Log.d("todayTask", "projectPos: " + todayTask.getiProject() + " taskPos: " + todayTask.getiTask() +" todayTask: " + todayTask.getTask().toString());
            folders.get(todayTask.getiProject()).getListOfTasks().set(todayTask.getiTask(), todayTask.getTask());
        }
        Helper.saveData(this.getActivity(), folders);
    }
}
