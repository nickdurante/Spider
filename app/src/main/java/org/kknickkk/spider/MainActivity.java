package org.kknickkk.spider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Connection> connections = new ArrayList<Connection>();
    ConnectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));

                Snackbar.make(view, "Here I'll put the connection added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView rvConnections = (RecyclerView) findViewById(R.id.rvConnections);

        // create fake initialize connections
        for(int i=0; i<2; i++){
            String si = String.valueOf((i*1111)%256);
            String t_ip = si + '.' + si + '.' + si + '.' + si;
            connections.add(new Connection(t_ip, i*1234%100, "nick", "key.rsa"));
        }



        // link all
        adapter = new ConnectionAdapter(connections);
        rvConnections.setAdapter(adapter);
        rvConnections.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onResume(){
        super.onResume();

        if(getIntent().getExtras() != null) {
            Connection connectionToAdd = (Connection) getIntent().getExtras().getSerializable("connectiontoadd");
            String text = "Received: " + connectionToAdd.getIp() + " " + connectionToAdd.getKey_path();
            Toast.makeText(getApplicationContext(), "Adding new connection", Toast.LENGTH_LONG).show();
            Log.d("resume", "should be adding new connection: " + connectionToAdd.toString());
            connections.add(0, connectionToAdd);
            adapter.notifyItemInserted(0);

        }



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
