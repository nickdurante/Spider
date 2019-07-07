package org.kknickkk.spider;


public class PathHandler {
    String currentPath;

    public PathHandler(){
        currentPath = Globals.currentPath;
    }
    public String updatePath(String dir){
        if(dir.equals("..")){

            currentPath = currentPath.substring(0,currentPath.lastIndexOf("/"));
        }else{
            currentPath = currentPath + "/" + dir;
        }
        Globals.currentPath = currentPath;
        return currentPath;

    }

    public String getCurrentPath(){
        return this.currentPath;
    }
}
