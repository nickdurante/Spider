package org.kknickkk.spider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_connection);


    }



    /** Called when the user taps the Send button */
    public void saveConnection(View view) {
        // Do something in response to button
        EditText eIP = (EditText) findViewById(R.id.register_ip);
        EditText ePort = (EditText) findViewById(R.id.register_port);
        EditText eUser = (EditText) findViewById(R.id.register_user);
        EditText ePassword = (EditText) findViewById(R.id.register_password);
        Switch sID = findViewById(R.id.register_switch_id);


        String text = eIP.getText().toString() + "\n" + ePort.getText().toString() + "\n" + eUser.getText().toString() + "\n" + ePassword.getText().toString() + "\n" + sID.isChecked();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();



    }

}
