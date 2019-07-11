package org.kknickkk.spider.Tasks;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import org.kknickkk.spider.DirectoryElement;
import org.kknickkk.spider.Globals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelSftp;


public class UploadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressDialog mProgressDialog = Globals.mProgressDialogUpload;

    public UploadTask(Context context) {
        this.context = context;
    }

    //prova
    @Override
    protected String doInBackground(String... params) {
        ByteArrayInputStream bis = null;
        BufferedOutputStream bos = null;
        String destFolder = params[0];

        ChannelSftp channelSftp = (ChannelSftp) Globals.channel;
        long progress = 0;
        float percentage = 0;
        long size = 0;
        try {
            byte[] buffer = new byte[1024];
            //bis = new BufferedInputStream(channelSftp.get(toDownload.getName()));
            bis = new ByteArrayInputStream(Globals.fileUpBytes);

            File newFile = new File(destFolder);

            OutputStream os = channelSftp.put(destFolder + "/" + Globals.fileUpName);
            bos = new BufferedOutputStream(os);
            int readCount;
            size = Globals.fileUpBytes.length;

            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
                progress += buffer.length;
                percentage = progress*100 / size;
                Log.d("UPLOAD", "size: " + size);
                Log.d("UPLOAD", "progress: " + progress);
                Log.d("UPLOAD", "Writing: " + percentage + "%");

                publishProgress((int)percentage);
            }

        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException ignored) { }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context, "Upload error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File uploaded", Toast.LENGTH_SHORT).show();
    }
}