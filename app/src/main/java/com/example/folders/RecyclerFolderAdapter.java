package com.example.folders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class RecyclerFolderAdapter extends RecyclerView.Adapter<RecyclerFolderAdapter.ViewHolder>{
    private static final int OPEN_TASKOVERVIEW_REQUEST_CODE = 2;
    //    Context context;
    ArrayList<Folder> listOfFolders;
    private IClickItemFolderListener iClickItemFolderListener;

    RecyclerFolderAdapter(ArrayList<Folder> listOfFolders, IClickItemFolderListener listener){
        this.listOfFolders = listOfFolders;
        this.iClickItemFolderListener = listener;
    }

    @NonNull
    @Override
    // Tạo view rỗng cho 1 item
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // Lấy dữ liệu cho view
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = listOfFolders.get(position);

        holder.tvFolderName.setText(folder.name);

        // Khi nhấn vào 1 item thì sẽ dẫn đến TaskOverview
        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFolderListener.onClickItemFolder(folder);
            }
        });

        // Khi nhấn dữ lâu 1 item thì sẽ gợi ý Delete
        holder.llRow.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Folder")
                        .setMessage("Are you sure want to delete")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listOfFolders.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

                return true;
            }
        });

        // Khi nhấn vào nút 3 chấm thì cho Rename
        holder.imageViewRenameFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imageViewRenameFolder);
                popupMenu.inflate(R.menu.folder_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.option_rename_folder){
                            iClickItemFolderListener.onClickRenameFolder(holder.getAdapterPosition());
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfFolders.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFolderName;
        LinearLayout llRow;
        ImageView imageViewRenameFolder;
      //  LinearLayout llRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.text_view_folder_name);
            llRow = itemView.findViewById(R.id.llRow);
            imageViewRenameFolder = itemView.findViewById(R.id.image_rename_folder);

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(Menu.NONE,R.id.option_rename_folder, Menu.NONE, "Rename");
                }
            });
        }


    }
}

