package org.kknickkk.spider.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.kknickkk.spider.DirectoryElement;
import org.kknickkk.spider.FolderAdapter;
import org.kknickkk.spider.Globals;

import java.util.ArrayList;
import java.util.Vector;

public class GetFilesTask extends AsyncTask<String, Integer, Vector<ChannelSftp.LsEntry>> {

    FolderAdapter adapter = Globals.rvAdapter;

    protected Vector<ChannelSftp.LsEntry> doInBackground(String...params) {
        Log.d("GETFILELIST TASK", "Started do on background");
        String path = params[0];
        try {

            Session session = Globals.session;
            Log.d("GETFILELIST TASK", "session is: " + session.getHost());


            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channel.connect();


                channelSftp.cd(path);
                Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*");
                Log.d("GETFILELIST TASK", "Files are:");

                for (ChannelSftp.LsEntry entry : list) {
                    Log.d("GETFILELIST TASK", entry.getFilename());

                }
                Globals.channel = channel;
                Globals.currentDir = list;

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

        ArrayList<DirectoryElement> elements = Globals.elements;
        Vector<ChannelSftp.LsEntry> fileArray = Globals.currentDir;
        elements.clear();
        Globals.elements = elements;
        elements.add(new DirectoryElement("..", true, Long.parseLong("0"), null));

        if(fileArray != null) {
            for (ChannelSftp.LsEntry entry : fileArray) {
                Log.d("FOLDER ACTIVITY", "adding to elements:" + entry.getFilename());
                elements.add(new DirectoryElement(entry.getFilename(), entry.getAttrs().isDir(), entry.getAttrs().getSize(), entry));
            }
        }
        adapter.notifyDataSetChanged();
        Globals.toolbar.setTitle(Globals.currentPath);
    }

    @Override
    protected void onCancelled(){
        return;
    }

}
