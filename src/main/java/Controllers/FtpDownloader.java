package Controllers;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

public class FtpDownloader {
    public static String template;
    public static void downloadDirectory(FTPClient ftpClient, String remoteDirPath, String localParentDirPath)
            throws IOException {
        FTPFile[] remoteFiles = ftpClient.listFiles(remoteDirPath);
        File localParentDir = new File(localParentDirPath + File.separator + new File(remoteDirPath).getName());
        if (!localParentDir.exists()) {
            localParentDir.mkdir();
        }
        for (FTPFile remoteFile : remoteFiles) {
            String remoteFilePath = remoteDirPath + "/" + remoteFile.getName();
            if (remoteFile.isFile() && remoteFile.getName().contains(template)) {
                File localFile = new File(localParentDir.getPath() + File.separator + remoteFile.getName());
                downloadFile(ftpClient, remoteFilePath, localFile);
            } else if (remoteFile.isDirectory()) {
                downloadDirectory(ftpClient, remoteFilePath, localParentDir.getPath());
            }
        }
    }

    private static void downloadFile(FTPClient ftpClient, String remoteFilePath, File localFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(localFile);
        InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            outputStream.write(bytesArray, 0, bytesRead);
        }
        boolean success = ftpClient.completePendingCommand();
        if (success) {
            System.out.println("File " + remoteFilePath + " download success");
        }
        outputStream.close();
        inputStream.close();
    }

}
