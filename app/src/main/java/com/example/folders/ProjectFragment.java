package com.example.folders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Helper;
import com.example.finalproject.R;
import com.example.task.Task;
import com.example.task.TaskOverview;

import java.util.ArrayList;

public class ProjectFragment extends Fragment {
    RecyclerFolderAdapter adapter;
    ImageView btnOpenDialog;
    RecyclerView recyclerView;
    ArrayList<Folder> listOfFolders;

    private View view;

    private final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    private final String TASK_ITEM = "task";
    private final String FOLDER_POSITION = "position";
    private final String LOAD_FOLDERS = "folders";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, container, false);
        listOfFolders = Helper.loadFolders(this.getActivity());
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
                } else {
                    Toast.makeText(getView().getContext(), "Please Enter Projects Name", Toast.LENGTH_SHORT).show();
                }

                listOfFolders.add(new Folder(name));

                adapter.notifyItemInserted(listOfFolders.size() - 1);
                recyclerView.scrollToPosition(listOfFolders.size() - 1);

                dialog.dismiss();
            });

            dialog.show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerFolderAdapter(listOfFolders, new IClickItemFolderListener() {
            @Override
            public void onClickItemFolder(Folder folder, int position) {
                onClickGoToTaskOverview(folder, position);
            }

            @Override
            public void onClickRenameFolder(int position) {
                renameFolder(position);
            }

        });
        recyclerView.setAdapter(adapter);
    }

    public void onClickGoToTaskOverview(Folder folder, int position) {
        Intent intent = new Intent(view.getContext(), TaskOverview.class);
        Bundle args = new Bundle();
        args.putSerializable(TASK_ITEM, folder.getListOfTasks());
        args.putInt(FOLDER_POSITION, position);

        intent.putExtra("BUNDLE", args);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }

    private void renameFolder(int position) {
        Folder folder = listOfFolders.get(position);
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.add_update_layout);

        EditText editTextFolderName = dialog.findViewById(R.id.edit_text_folder_name);
        Button buttonOk = dialog.findViewById(R.id.button_add);
        TextView tvTitle = dialog.findViewById(R.id.text_view_title);

        buttonOk.setText("Ok");
        tvTitle.setText("Rename");

        editTextFolderName.setText(folder.getName());

        buttonOk.setOnClickListener(v -> {
            if (editTextFolderName.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please Enter Projects Name", Toast.LENGTH_SHORT).show();
                return;
            }

            folder.setName(editTextFolderName.getText().toString());
            adapter.notifyItemChanged(position);
            dialog.dismiss();
        });

        dialog.show();
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
                listOfFolders.get(position).setListOfTasks((ArrayList<Task>) bundle.getSerializable(TASK_ITEM));
            }
            Log.d("projectTaskResult", "position: " + position + " listOfFolders: " + listOfFolders.get(position).getListOfTasks());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("folder list result", listOfFolders);
                intent.putExtras(bundle);
                this.getActivity().setResult(Activity.RESULT_OK, intent);
                this.getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        Helper.saveData(this.getActivity(), listOfFolders);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStop() {
        Helper.saveData(this.getActivity(), listOfFolders);
        super.onStop();
    }
}
