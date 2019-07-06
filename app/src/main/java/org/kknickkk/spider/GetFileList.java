package org.kknickkk.spider;

import android.os.AsyncTask;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.util.Vector;

public class GetFileList extends AsyncTask<Session, Integer, Vector<ChannelSftp.LsEntry>> {

    protected Vector<ChannelSftp.LsEntry> doInBackground(Session...params) {

        try {
            Session session = params[0];

            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channel.connect();
            channelSftp.cd("/home/pi/java/");
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*");
            System.out.println("Files are:");

            for(ChannelSftp.LsEntry entry : list) {
                System.out.println(entry.getFilename());
            }
            return list;

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Vector<ChannelSftp.LsEntry> result) {

        //showDialog("Downloaded " + result + " bytes");
        /*
        for(ChannelSftp.LsEntry entry : result) {
            //channelSftp.get(entry.getFilename(), destinationPath + entry.getFilename());
            out += entry.getFilename() + "\n";
        }
        */
    }



}
