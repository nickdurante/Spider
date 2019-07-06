package org.kknickkk.spider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;


import com.jcraft.jsch.*;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_connection);



        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 84);
        }

    }


    Uri uri;
    String filecontent_private;
    ConnectTask connectTask;
    Session session;

    /** Called when the user taps the Send button */
    public void saveConnection(View view) {
        // Do something in response to button
        EditText eIP = (EditText) findViewById(R.id.register_ip);
        EditText ePort = (EditText) findViewById(R.id.register_port);
        EditText eUser = (EditText) findViewById(R.id.register_user);
        EditText ePassword = (EditText) findViewById(R.id.register_password);
        Switch sID = findViewById(R.id.register_switch_id);

        String user = eUser.getText().toString();
        String IP = eIP.getText().toString();
        String port = ePort.getText().toString();

        //connection task
        connectTask = new ConnectTask();
        connectTask.execute(user, IP, port, filecontent_private);

        try {
            session = connectTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent myIntent = new Intent(RegisterActivity.this, FolderActivity.class);
        //myIntent.putExtra("session", session);
        Globals.session = session;
        connectTask.cancel(true);
        Log.d("REGISTER ACTIVITY", "launching Folder activity");
        RegisterActivity.this.startActivity(myIntent);
        //close activity
        //finish();
    }




    private static final int READ_REQUEST_CODE_PRIVATEKEY = 42;
    private static final int READ_REQUEST_CODE_PUBLICKEY = 43;

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearchPrivate(View view) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE_PRIVATEKEY);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE_PRIVATEKEY && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            //Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    filecontent_private = readTextFromUri(uri);
                    //Toast.makeText(getApplicationContext(), filecontent, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        reader.close();
        return stringBuilder.toString();
    }



}
