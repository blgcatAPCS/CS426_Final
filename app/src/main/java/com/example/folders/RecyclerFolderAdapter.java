package com.example.folders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFolderAdapter extends RecyclerView.Adapter<RecyclerFolderAdapter.FolderViewHolder>{
    ArrayList<Folder> listOfFolders;
    private IClickItemFolderListener iClickItemFolderListener;

    RecyclerFolderAdapter(ArrayList<Folder> listOfFolders, IClickItemFolderListener listener){
        this.listOfFolders = listOfFolders;
        this.iClickItemFolderListener = listener;
    }

    @NonNull
    @Override
    // Tạo view rỗng cho 1 item
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_row, parent, false);
        return new FolderViewHolder(view);
    }

    // Lấy dữ liệu cho view
    public void onBindViewHolder(@NonNull RecyclerFolderAdapter.FolderViewHolder holder, int position) {
        Folder folder = listOfFolders.get(position);

        holder.tvFolderName.setText(folder.name);

        holder.llRow.setOnClickListener(v -> {
            iClickItemFolderListener.onClickItemFolder(folder, position);
        });

        holder.llRow.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Folder")
                    .setMessage("Are you sure want to delete")
                    .setIcon(R.drawable.ic_baseline_delete_24)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        listOfFolders.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", (dialog, which) -> {

                    });
            builder.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listOfFolders.size();
    }


    public static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView tvFolderName;
        LinearLayout llRow;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.text_view_folder_name);
            llRow = itemView.findViewById(R.id.llRow);
        }
    }
}