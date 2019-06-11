package org.kknickkk.spider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView connectionIp, connectionPort, connectionUser;
        public Button connectButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            connectionIp= (TextView) itemView.findViewById(R.id.connection_ip);
            connectButton = (Button) itemView.findViewById(R.id.connect_button);
            connectionPort = (TextView) itemView.findViewById(R.id.connection_port);
            connectionUser = (TextView) itemView.findViewById(R.id.connection_user);

        }
    }


    private List<Connection> mConnections;

    public ConnectionAdapter(List<Connection> conn){
        mConnections = conn;
    }


    @Override
    public ConnectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ConnectionAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Connection connection = mConnections.get(position);

        // Set item views based on your views and data model

        viewHolder.connectionIp.setText(connection.getIp());
        viewHolder.connectionPort.setText(String.valueOf(connection.getPort()));
        viewHolder.connectionUser.setText(connection.getUser());
        viewHolder.connectButton.setText("Connect");

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mConnections.size();
    }

}
