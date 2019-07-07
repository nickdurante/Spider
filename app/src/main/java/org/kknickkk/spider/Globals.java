package org.kknickkk.spider;

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

}
