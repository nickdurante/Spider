package org.kknickkk.spider;

import com.jcraft.jsch.ChannelSftp;

public class DirectoryElement implements Comparable<DirectoryElement> {
    String name;
    boolean isDirectory;
    String shortname;
    Long size;
    ChannelSftp.LsEntry sftpInfo;
    long sizeMB;

    public DirectoryElement(String name, boolean isDirectory, Long size, ChannelSftp.LsEntry sftpInfo) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.sftpInfo = sftpInfo;
        this.shortname = name.substring(name.lastIndexOf("/") + 1);
        this.sizeMB = size / (1024 * 1024);
    }

    public long getSizeMB() {
        return sizeMB;
    }

    @Override
    public int compareTo(DirectoryElement o) {

        return name.compareTo(o.name);
    }

    public String getShortname() {
        return shortname;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Long getSize() {
        return size;
    }

    public ChannelSftp.LsEntry getSftpInfo() {
        return sftpInfo;
    }
}
