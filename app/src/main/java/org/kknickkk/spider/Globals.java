package org.kknickkk.spider;

import android.widget.Adapter;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Vector;

public class Globals {
    public static Session session;
    public static Vector<ChannelSftp.LsEntry> currentDir;
    public static FolderAdapter rvAdapter;
    public static ArrayList<DirectoryElement> elements;

}
