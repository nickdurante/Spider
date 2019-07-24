package org.kknickkk.spider.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.kknickkk.spider.Globals;



public class ConnectTask extends AsyncTask<String, Integer, Session> {


    Session session;

    protected Session doInBackground(String...params) {

        Log.d("CONNECT TASK", "Started do on background");

        JSch jsch = new JSch();
        //Session session;
        String user = params[0];
        String IP = params[1];
        int port = Integer.valueOf(params[2]);

        boolean usingKey = Boolean.valueOf(params[3]);
        Log.e("CONNECT", "Boolean is: " + usingKey);


        try {
            if(!usingKey){
                session = jsch.getSession(user, IP, port);
                session.setPassword(params[4]);

            }else {
                jsch.addIdentity("connection", Globals.private_bytes, null, null);
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

}
