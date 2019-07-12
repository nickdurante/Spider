package org.kknickkk.spider;

import android.app.ProgressDialog;

import androidx.appcompat.widget.Toolbar;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Vector;

public class Globals {
    public static Session session;
    public static Channel channel;
    public static Vector<ChannelSftp.LsEntry> currentDir;
    public static FolderAdapter rvAdapter;
    public static ArrayList<DirectoryElement> elements;
    public static String currentPath;
    public static ProgressDialog mProgressDialogDownload;
    public static ProgressDialog mProgressDialogUpload;
    public static byte[] private_bytes;
    public static byte[] fileUpBytes;
    public static String fileUpName;
    public static Toolbar toolbar;
    public static String downloadFolder = "SFTP_downloads";

}
