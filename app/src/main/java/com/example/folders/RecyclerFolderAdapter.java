package com.example.folders;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class RecyclerFolderAdapter extends RecyclerView.Adapter<RecyclerFolderAdapter.FolderViewHolder> {
    private static final int OPEN_TASKOVERVIEW_REQUEST_CODE = 2;

    ArrayList<Folder> listOfFolders;
    private final IClickItemFolderListener iClickItemFolderListener;

    RecyclerFolderAdapter(ArrayList<Folder> listOfFolders, IClickItemFolderListener listener) {
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

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = listOfFolders.get(position);
        holder.tvFolderName.setText(folder.getName());
        // Khi nhấn vào 1 item thì sẽ dẫn đến TaskOverview
        holder.llRow.setOnClickListener(v -> iClickItemFolderListener.onClickItemFolder(folder, position));

        // Khi nhấn dữ lâu 1 item thì sẽ gợi ý Delete
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

        // Khi nhấn vào nút 3 chấm thì cho Rename
        holder.imageViewRenameFolder.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imageViewRenameFolder);
            popupMenu.inflate(R.menu.folder_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.option_rename_folder) {
                    iClickItemFolderListener.onClickRenameFolder(holder.getAdapterPosition());
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return listOfFolders.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView tvFolderName;
        LinearLayout llRow;
        ImageView imageViewRenameFolder;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.text_view_folder_name);
            llRow = itemView.findViewById(R.id.llRow);
            imageViewRenameFolder = itemView.findViewById(R.id.image_rename_folder);

            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> menu.add(Menu.NONE, R.id.option_rename_folder, Menu.NONE, "Rename"));
        }
    }
}
