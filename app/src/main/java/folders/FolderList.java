package folders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FolderList extends AppCompatActivity {
    private static final int GO_TO_TASKOVERVIEW_REQUEST_CODE = 2;
    RecyclerFolderAdapter adapter;
    FloatingActionButton btnOpenDialog;
    RecyclerView recyclerView;
    ArrayList<Folder> listOfFolders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Your Folders");

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
        });
        recyclerView.setAdapter(adapter);
    }

    public void onClickGoToTaskOverview(Folder folder) {
        Intent intent = new Intent(this, TaskOverview.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item folder", folder);
        intent.putExtras(bundle);
        startActivityForResult(intent, GO_TO_TASKOVERVIEW_REQUEST_CODE);
    }
}