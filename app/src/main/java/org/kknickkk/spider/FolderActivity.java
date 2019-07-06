package org.kknickkk.spider;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class FolderActivity extends AppCompatActivity {

    ArrayList<DirectoryElement> elements = new ArrayList<DirectoryElement>();


    FolderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FOLDER ACTIVITY", "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Globals.elements = elements;

        RecyclerView rvConnections = findViewById(R.id.rvConnections);


        //Intent intent = getIntent();
        //Session session = (Session) intent.getCharSequenceExtra("session");
        Session session = Globals.session;
        Log.d("FOLDER ACTIVITY", "got session: " + session.getHost());
        // set the adapter
        adapter = new FolderAdapter(elements);
        Globals.rvAdapter = adapter;

        //get file list in task
        GetFilesTask getFilesTask = new GetFilesTask();
        String path = "/home/pi/java";
        getFilesTask.execute(path);




        // link all

        rvConnections.setAdapter(adapter);
        rvConnections.setLayoutManager(new LinearLayoutManager(this));


    }




    @Override
    public void onResume(){
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
