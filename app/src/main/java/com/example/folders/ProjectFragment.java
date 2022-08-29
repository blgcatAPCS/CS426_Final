package com.example.folders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.task.Task;
import com.example.task.TaskOverview;

import java.util.ArrayList;

public class ProjectFragment extends Fragment {
    private final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    private String TASK_ITEM = "task";
    private String FOLDER_POSITION = "position";

    private View view;

    RecyclerFolderAdapter adapter;
    ImageView btnOpenDialog;
    RecyclerView recyclerView;
    ArrayList<Folder> listOfFolders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, container, false);
        initView();

        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_view_folders);
        btnOpenDialog = view.findViewById(R.id.button_add_folder);

        btnOpenDialog.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getView().getContext());
            dialog.setContentView(R.layout.add_update_layout);

            EditText editFolderName = dialog.findViewById(R.id.edit_text_folder_name);
            Button buttonAction = dialog.findViewById(R.id.button_add);

            buttonAction.setOnClickListener(v1 -> {
                String name = "";

                if (!editFolderName.getText().toString().equals("")) {
                    name = editFolderName.getText().toString();
                }
                else{
                    Toast.makeText(getView().getContext(), "Please Enter Folder Name", Toast.LENGTH_SHORT).show();
                }

                listOfFolders.add(new Folder(name));

                adapter.notifyItemInserted(listOfFolders.size() - 1);

                recyclerView.scrollToPosition(listOfFolders.size() - 1);

                dialog.dismiss();
            });

            dialog.show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerFolderAdapter(listOfFolders, (folder, position) -> onClickGoToTaskOverview(folder, position));
        recyclerView.setAdapter(adapter);
    }

    public void onClickGoToTaskOverview(Folder folder, int position) {
        Intent intent = new Intent(view.getContext(), TaskOverview.class);
        Bundle args = new Bundle();
        args.putSerializable(TASK_ITEM, folder.listOfTasks);
        args.putInt(FOLDER_POSITION, position);

        intent.putExtra("BUNDLE", args);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("projectTaskResult", "requestCode: " + requestCode + " resultCode: " + resultCode);
        if (requestCode == GO_TO_TASKOVERVIEW_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getBundleExtra("BUNDLE");
            Log.d("projectTaskResult", "bundle exists: " + (bundle != null));
            int position = bundle.getInt(FOLDER_POSITION, -1);
            if (position != -1) {
                listOfFolders.get(position).listOfTasks = (ArrayList<Task>) bundle.getSerializable(TASK_ITEM);
            }
            Log.d("projectTaskResult", "position: " + position + " listOfFolders: " + listOfFolders.get(position).listOfTasks);
        }
    }
}
