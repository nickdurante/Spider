package org.kknickkk.spider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_connection);


    }

    Uri uri;

    /** Called when the user taps the Send button */
    public void saveConnection(View view) {
        // Do something in response to button
        EditText eIP = (EditText) findViewById(R.id.register_ip);
        EditText ePort = (EditText) findViewById(R.id.register_port);
        EditText eUser = (EditText) findViewById(R.id.register_user);
        EditText ePassword = (EditText) findViewById(R.id.register_password);
        Switch sID = findViewById(R.id.register_switch_id);


        String text = eIP.getText().toString() + "\n" + ePort.getText().toString() + "\n" + eUser.getText().toString() + "\n" + ePassword.getText().toString() + "\n" + sID.isChecked() + "\n" + uri.toString();
        //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        Connection connectionToAdd = new Connection(eIP.getText().toString(), Integer.valueOf(ePort.getText().toString()), eUser.getText().toString(), uri.toString());

        //save connection
        Intent resultIntent = new Intent(RegisterActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("connectiontoadd", connectionToAdd);
        resultIntent.putExtras(bundle);

        //close activity
        finish();
    }

    private static final int READ_REQUEST_CODE = 42;
    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch(View view) {

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

        startActivityForResult(intent, READ_REQUEST_CODE);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            //Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String filecontent;
                try {
                    filecontent = readTextFromUri(uri);
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
        //fileInputStream.close();
        //parcelFileDescriptor.close();
        return stringBuilder.toString();
    }



}
