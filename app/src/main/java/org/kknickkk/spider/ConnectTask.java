package org.kknickkk.spider;

import android.os.AsyncTask;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.nio.charset.StandardCharsets;
import java.util.Vector;


public class ConnectTask extends AsyncTask<String, Integer, Session> {

    protected Session doInBackground(String...params) {
        JSch jsch = new JSch();
        Session session;
        String user = params[0];
        String IP = params[1];
        int port = Integer.valueOf(params[2]);
        String publ = params[3];
        String priv = params[4];


        System.out.println("user ===> " + user);
        System.out.println("IP ===> " + IP);
        System.out.println("port ===> " + port);
        System.out.println("publ ===> " + publ);
        System.out.println("priv ===> " + priv);


        byte[] privBytes = readKey(priv);

        try {

            jsch.addIdentity("connection", privBytes, null, null);
            session = jsch.getSession(user, IP, port);
            session.setConfig("PreferredAuthentications", "publickey");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();


            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channel.connect();
            channelSftp.cd("/home/pi/java/");
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*");
            System.out.println("Files are:");

            for(ChannelSftp.LsEntry entry : list) {
                System.out.println(entry.getFilename());
            }

            return session;

        }catch (JSchException | SftpException e){
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





    private static byte[] readKey(String keyFileContent){

        keyFileContent = keyFileContent.replace("-----BEGIN RSA PRIVATE KEY-----", "-----BEGIN RSA PRIVATE KEY-----\r\n").replace("-----END RSA PRIVATE KEY-----", "\r\n-----END RSA PRIVATE KEY-----");
        System.out.println("keycontent ===> " + keyFileContent);

        return keyFileContent.getBytes(StandardCharsets.US_ASCII);

    }

}
