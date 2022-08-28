package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.folders.Folder;
import com.example.folders.FolderList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 3;
    private ArrayList<Folder> listOfFolders = new ArrayList<>();
    Button button;

    // T thêm 1 Button để test thử chạy đc hong nha m


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_go_to_folder);

        // Xử lý chuyển và lấy dữ liệu qua list of folders

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<Folder> folders = new ArrayList<>();
//                folders = (ArrayList) listOfFolders.clone();
                Intent intent = new Intent(MainActivity.this, FolderList.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("folder list", listOfFolders);
                intent.putExtras(bundle);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (MY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode)
        {
            Bundle bundle = data.getExtras();
            listOfFolders = (ArrayList<Folder>) bundle.get("folder list result");
        }
    }
}