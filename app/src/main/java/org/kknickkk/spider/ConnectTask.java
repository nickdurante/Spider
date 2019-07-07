package org.kknickkk.spider;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.nio.charset.StandardCharsets;


public class ConnectTask extends AsyncTask<String, Integer, Session> {


    Session session;

    protected Session doInBackground(String...params) {

        Log.d("CONNECT TASK", "Started do on background");

        JSch jsch = new JSch();
        //Session session;
        String user = params[0];
        String IP = params[1];
        int port = Integer.valueOf(params[2]);

        boolean usingKey = Boolean.valueOf(params[4]);
        Log.e("CONNECT", "Boolean is: " + usingKey);
        /*
        System.out.println("user ===> " + user);
        System.out.println("IP ===> " + IP);
        System.out.println("port ===> " + port);
        System.out.println("priv ===> " + priv);
        */

        try {
            if(!usingKey){
                session = jsch.getSession(user, IP, port);
                session.setPassword(params[3]);

            }else {
                jsch.addIdentity("connection", readKey(params[3]), null, null);
                session = jsch.getSession(user, IP, port);
                session.setConfig("PreferredAuthentications", "publickey");
            }

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Log.d("CONNECT TASK", "connected, returning session");

            return session;

        }catch (JSchException e){
            e.printStackTrace();
            return null;
        }

    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Session result) {
        //...
    }


    @Override
    protected void onCancelled(){
        return;
    }


    private static byte[] readKey(String keyFileContent){

        keyFileContent = keyFileContent.replace("-----BEGIN RSA PRIVATE KEY-----", "-----BEGIN RSA PRIVATE KEY-----\r\n").replace("-----END RSA PRIVATE KEY-----", "\r\n-----END RSA PRIVATE KEY-----");
        //System.out.println("keycontent ===> " + keyFileContent);
        return keyFileContent.getBytes(StandardCharsets.US_ASCII);
    }

}
