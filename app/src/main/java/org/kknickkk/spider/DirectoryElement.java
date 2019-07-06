package org.kknickkk.spider;

public class DirectoryElement {
    String name;
    boolean isDirectory;
    Long size;

    public DirectoryElement(String name, boolean isDirectory, Long size){
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
    }


}
