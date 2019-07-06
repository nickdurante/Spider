package org.kknickkk.spider;

public class DirectoryElement {
    String name;
    boolean isDirectory;
    String size;

    public DirectoryElement(String name, boolean isDirectory, String size){
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
    }


}
