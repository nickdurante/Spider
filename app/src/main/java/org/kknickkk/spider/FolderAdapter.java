package org.kknickkk.spider;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView filename;
        public ImageView icon;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            filename = itemView.findViewById(R.id.filename);
            icon = itemView.findViewById(R.id.icon);

        }
    }

    private List<DirectoryElement> mDirectoryElement;

    public FolderAdapter(List<DirectoryElement> dir) {
        mDirectoryElement = dir;
    }


    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_connection, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DirectoryElement directoryElement = mDirectoryElement.get(position);

        // Set item views based on your views and data model
        if (directoryElement.isDirectory) {
            viewHolder.icon.setImageResource(R.drawable.folder);
        } else if (directoryElement.sftpInfo.getAttrs().isLink()) {
            viewHolder.icon.setImageResource(R.drawable.right);
        } else {
            viewHolder.icon.setImageResource(R.drawable.file);
        }
        viewHolder.filename.setText(directoryElement.name);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mDirectoryElement.size();
    }

}
