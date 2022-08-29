package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.folders.ProjectFragment;
import com.example.task.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private final String LOAD_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_today);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_project:
                startProjectView();
                break;

            case R.id.nav_today:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();
                break;

            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                break;

            default:
                return false;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void startProjectView() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Your Projects");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProjectFragment()).commit();
    }

    void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(LOAD_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAD_DATA, null);
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        Helper.projects = gson.fromJson(json, type);

        if (Helper.projects == null)
            Helper.projects = new ArrayList<>();
    }
}