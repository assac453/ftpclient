package com.example.ftpclient;

import org.apache.commons.net.ftp.FTPFile;

import java.time.Instant;
import java.util.Calendar;

public class FTPFileExtended extends FTPFile{
    private FTPFile file;
    public FTPFileExtended(FTPFile file){
        this.file = file;
    }
    private int typeStr = 0;
    private String name;
    private long size = -1L;
    private Instant TimeStampInstant;
    public String getTypeStr(){
        switch (file.getType()) {
            case 0:
                return "-";
            case 1:
                return "d";
            case 2:
                return "l";
            default:
                return "?";
        }
    }
    public void setTypeStr(int type){file.setType(type);}
    public boolean isDirectory() {
        return file.isDirectory();
    }
    public String getName() {
        return file.getName();
    }
    public long getSize() {
        return file.getSize();
    }
    public Instant getTimestampInstant() {
        return file.getTimestampInstant();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public void setName(String name) {
        file.setName(name);
    }

    public void setSize(long size) {
        file.setSize(size);
    }

    public void setTimestamp(Calendar date) {
        file.setTimestamp(date);
    }
}
