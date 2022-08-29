package com.example.finalproject;

import android.util.Log;

import com.example.Priority.Priority;
import com.example.folders.Folder;
import com.example.task.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    static public ArrayList<Folder> projects;
    static private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final Map<Integer, Priority> intToPriority = new HashMap<Integer, Priority>();
    private static final int[] priorityIcon = {R.drawable.ic_baseline_trending_up_24, R.drawable.ic_baseline_trending_flat_24, R.drawable.ic_baseline_trending_down_24};
    private static final String[] priorityText = {"High", "Medium", "Low"};

    static {
        for (Priority type : Priority.values()){
            intToPriority.put(type.getIntValue(), type);
        }
    }

    public static String dateToString (Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String date){
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Priority getPriority(int priority){
        Priority res = intToPriority.get(priority);
        if (res == null) throw new IllegalArgumentException("Value of priority is invalid");
        return res;
    }

    public static int getPriorityIconSrc(Priority priority){
        Log.d("priorityIcon", "priority: " + priority + " value: " + priority.getIntValue());
        return priorityIcon[priority.getIntValue()-1];
    }

    public static String getPriorityString(Priority priority){
        return priorityText[priority.getIntValue()-1];
    }
}
