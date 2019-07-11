package org.kknickkk.spider;


public class PathHandler {
    String currentPath;

    public PathHandler(){
        currentPath = Globals.currentPath;
    }

    public String updatePath(String dir){

        // clean current path from last / but not root
        if(currentPath.substring(currentPath.length() - 1).equals("/") && currentPath.length() != 1){
            currentPath = currentPath.substring(0, currentPath.length() - 2);
        }
        // clean dir from first /
        if(dir.substring(dir.length() - 1).equals("/")){
            dir = dir.substring(0, dir.length() - 2);
        }
        // clean dir from first /
        if(dir.substring(0,0).equals("/")){
            dir = dir.substring(1);
        }

        // check if going up
        if(dir.equals("..")){
            // check if going on root
            if(currentPath.lastIndexOf("/") == 0){
                currentPath = "/";
            }else {
                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
            }
        }else{
            if(currentPath.length() != 1) {
                currentPath = currentPath + "/" + dir;
            }else{
                currentPath = currentPath + dir;
            }
        }

        Globals.currentPath = currentPath;
        return currentPath;

    }

    public String getCurrentPath(){
        return this.currentPath;
    }
}
