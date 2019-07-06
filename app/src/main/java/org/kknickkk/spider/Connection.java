package org.kknickkk.spider;



public class Connection{
    private String ip;
    private int port;
    private String user;
    private String key_path;

    public Connection(String ip, int port, String user, String key_path){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.key_path = key_path;
    }


    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getKey_path() {
        return key_path;
    }
}
