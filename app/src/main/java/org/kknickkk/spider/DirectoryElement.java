package org.kknickkk.spider;

import com.jcraft.jsch.ChannelSftp;

public class DirectoryElement {
    String name;
    boolean isDirectory;
    Long size;
    ChannelSftp.LsEntry sftpInfo;

    public DirectoryElement(String name, boolean isDirectory, Long size, ChannelSftp.LsEntry sftpInfo){
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.sftpInfo = sftpInfo;
    }


}
