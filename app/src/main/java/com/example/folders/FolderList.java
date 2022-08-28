package com.example.folders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.task.TaskOverview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FolderList extends AppCompatActivity {
    private static final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    RecyclerFolderAdapter adapter;
    FloatingActionButton btnOpenDialog;
    RecyclerView recyclerView;
    ArrayList<Folder> listOfFolders;

    // Lưu lại dữ liệu cho app
    private final String LOAD_FOLDERS = "folders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        // Xử lý nhận list Folder
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            listOfFolders = (ArrayList<Folder>) bundle.get("folder list");
//        }

        // Lấy lại dữ liệu đã lưu
        loadData();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Folders");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // RecyclerView
        recyclerView = findViewById(R.id.recycler_view_folders);
        btnOpenDialog = findViewById(R.id.button_add_folder);

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(FolderList.this);
                dialog.setContentView(R.layout.add_update_layout);

                EditText editFolderName = dialog.findViewById(R.id.edit_text_folder_name);
                Button buttonAction = dialog.findViewById(R.id.button_add);

                buttonAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "";

                        if (!editFolderName.getText().toString().equals("")) {
                            name = editFolderName.getText().toString();
                        }
                        else{
                            Toast.makeText(FolderList.this, "Please Enter Folder Name", Toast.LENGTH_SHORT).show();
                        }

                        listOfFolders.add(new Folder(name));

                        adapter.notifyItemInserted(listOfFolders.size() - 1);

                        recyclerView.scrollToPosition(listOfFolders.size() - 1);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerFolderAdapter(listOfFolders, new IClickItemFolderListener() {
            @Override
            public void onClickItemFolder(Folder folder) {
                onClickGoToTaskOverview(folder);
            }

            @Override
            public void onClickRenameFolder(int position) {
                renameFolder(position);
            }

        });
        recyclerView.setAdapter(adapter);
    }

    private void renameFolder(int position) {
        Folder folder = listOfFolders.get(position);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_update_layout);

        EditText editTextFolderName = dialog.findViewById(R.id.edit_text_folder_name);
        Button buttonOk = dialog.findViewById(R.id.button_add);
        TextView tvTitle = dialog.findViewById(R.id.text_view_title);

        buttonOk.setText("Ok");
        tvTitle.setText("Rename");

        editTextFolderName.setText(folder.name);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";

                if (!editTextFolderName.getText().toString().equals("")) {
                    name = editTextFolderName.getText().toString();
                }
                else{
                    Toast.makeText(FolderList.this, "Please Enter Folder Name", Toast.LENGTH_SHORT).show();
                }

                folder.name = editTextFolderName.getText().toString();
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onClickGoToTaskOverview(Folder folder) {
        Intent intent = new Intent(this, TaskOverview.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item folder", folder);
        intent.putExtras(bundle);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }

    // Khi bấm nút quay lại trên navigation bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("folder list result", listOfFolders);
                intent.putExtras(bundle);

                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(LOAD_FOLDERS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAD_FOLDERS, null);
        Type type = new TypeToken<ArrayList<Folder>>(){}.getType();
        listOfFolders = gson.fromJson(json, type);

        if (listOfFolders == null)
            listOfFolders = new ArrayList<>();
    }

    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LOAD_FOLDERS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfFolders);
        editor.putString("folders", json);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }
}