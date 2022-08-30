package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.Priority.Priority;
import com.example.calendar.Dates;
import com.example.folders.Folder;
import com.example.task.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Helper {
    static private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final Map<Integer, Priority> intToPriority = new HashMap<Integer, Priority>();
    private static final int[] priorityIcon = {R.drawable.ic_baseline_trending_up_24, R.drawable.ic_baseline_trending_flat_24, R.drawable.ic_baseline_trending_down_24};
    private static final String[] priorityText = {"High", "Medium", "Low"};
    private static final String LOAD_FOLDERS = "folders";
    static public ArrayList<Folder> projects;
    private static final String LOAD_DATES = "dates";

    static {
        for (Priority type : Priority.values()) {
            intToPriority.put(type.getIntValue(), type);
        }
    }

    public static String dateToString(Date date){
        return simpleDateFormat.format(date);
    }

    public static String dateToTime(Date date) {
        int hour = date.getHours();
        int minute = date.getMinutes();
        return  String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public static Date stringToDate(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Priority getPriority(int priority) {
        Priority res = intToPriority.get(priority);
        if (res == null) throw new IllegalArgumentException("Value of priority is invalid");
        return res;
    }

    public static int getPriorityIconSrc(Priority priority) {
        Log.d("priorityIcon", "priority: " + priority + " value: " + priority.getIntValue());
        return priorityIcon[priority.getIntValue() - 1];
    }

    public static String getPriorityString(Priority priority) {
        return priorityText[priority.getIntValue() - 1];
    }

    public static ArrayList<Dates> loadDates(Context context){
        ArrayList<Dates> listOfDays;
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOAD_DATES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAD_DATES, null);
        Type type = new TypeToken<ArrayList<Dates>>() {
        }.getType();
        listOfDays = gson.fromJson(json, type);

        if (listOfDays == null)
            listOfDays = new ArrayList<>();

        return listOfDays;
    }

    public static ArrayList<Folder> loadFolders(Context context) {
        ArrayList<Folder> listOfFolders;
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOAD_FOLDERS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAD_FOLDERS, null);
        Type type = new TypeToken<ArrayList<Folder>>() {
        }.getType();
        listOfFolders = gson.fromJson(json, type);

        if (listOfFolders == null)
            listOfFolders = new ArrayList<>();

        Log.d("loadFolders", json);

        return listOfFolders;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void saveData(Context context, ArrayList<Folder> listOfFolders) {
        listOfFolders = sortTaskInProjects(listOfFolders);
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOAD_FOLDERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfFolders);
        editor.putString("folders", json);
        editor.apply();

        Log.d("saveData", json);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static ArrayList<Folder> sortTaskInProjects(ArrayList<Folder> listOfFolders) {
        for (Folder folder : listOfFolders) {
            ArrayList<Task> tasks = folder.getListOfTasks();
            Collections.sort(tasks, Comparator.comparing(Task::isDone).thenComparing(Task::getDeadline).thenComparing(Task::getPriority, Comparator.reverseOrder()));
            folder.setListOfTasks(tasks);
        }
        return listOfFolders;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}