package com.example.ftpclient.Global;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class Connection {
    public static FTPClient ftpClient = new FTPClient();
    public static void CloseAndDisconnect(){
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            }catch (IOException a){
                System.out.println(a.getMessage());
                a.printStackTrace();
            }
        }
    }
}
