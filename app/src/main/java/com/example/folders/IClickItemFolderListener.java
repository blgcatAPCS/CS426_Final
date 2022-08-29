package com.example.folders;

// Interface này để dùng cho xử lý khi ấn vào 1 item trong folder list
public interface IClickItemFolderListener {
    void onClickItemFolder(Folder folder, int position);

    void onClickRenameFolder(int position);
}