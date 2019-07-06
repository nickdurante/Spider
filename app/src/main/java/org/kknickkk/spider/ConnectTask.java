package org.kknickkk.spider;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.nio.charset.StandardCharsets;
import java.util.Vector;


public class ConnectTask extends AsyncTask<String, Integer, Session> {


    Session session;

    protected Session doInBackground(String...params) {

        Log.d("CONNECT TASK", "Started do on background");

        JSch jsch = new JSch();
        //Session session;
        String user = params[0];
        String IP = params[1];
        int port = Integer.valueOf(params[2]);
        String privateKeyText = params[3];

        /*
        System.out.println("user ===> " + user);
        System.out.println("IP ===> " + IP);
        System.out.println("port ===> " + port);
        System.out.println("priv ===> " + priv);
        */

        try {

            jsch.addIdentity("connection", readKey(privateKeyText), null, null);
            session = jsch.getSession(user, IP, port);
            session.setConfig("PreferredAuthentications", "publickey");
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
