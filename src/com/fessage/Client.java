package com.fessage;

import com.mycompany.loginform.MainForm;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
/**
 @author Jaysh Khan
 */
public class Client {
    private static String serverAddress="";
    private static int serverPort=5002;
    private static String Name="";
    private static Socket server;
    static File filetosend;
    private static double percentage=0;
    static JFileChooser jfc = new JFileChooser();
    private static FileInputStream  fileInputStream=null;
    private static DataOutputStream dataOutputStream=null;
    private static final FileOutputStream  ileOutputStream=null;
    private static String selectedFilePath;
    public Client(String address,int port,String clientName)
    {
        serverAddress = address;
        serverPort=port;
        Name=clientName;
    }
    public static String ChooseFile() throws IOException {

        if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
        {
            filetosend =jfc.getSelectedFile();
            return filetosend.getName();
        }
        else{
            return "File cannot be selected";
        }
    }
    public boolean connectToServer()throws IOException
    {
        boolean connected=false;
        System.out.println("conneting at: "+serverAddress+" and "+serverPort);
        server = new Socket(serverAddress,serverPort);
        connected=server.isConnected();
        dataOutputStream = new DataOutputStream(server.getOutputStream());
        byte[] name = Name.getBytes();  
        dataOutputStream.writeInt(name.length); //1 int
        dataOutputStream.write(name);           //2 byte
//        dataOutputStream.close();
        //server = new Socket("192.168.10.13",5002);
        return connected;
    }
    public boolean isConnected()
    {
        return server.isConnected();
    }
    public static boolean sendFile() throws IOException {
        if(jfc==null)
        {
            System.out.println("Please Choose a File");
            jfc.showOpenDialog(null);
            ChooseFile();
        }
        else{
            selectedFilePath=filetosend.getAbsolutePath();
            fileInputStream = new FileInputStream(selectedFilePath);
            
            dataOutputStream = new DataOutputStream(server.getOutputStream());
            String fileName =  jfc.getName(jfc.getSelectedFile());
            System.out.println(fileName);
            byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
            byte[] fileContentByte = new byte[(int)filetosend.length()];
            System.out.println("Total Bytes: "+fileContentByte.length);
            fileInputStream.read(fileContentByte);
            fileInputStream.close();
            //sending ClientName
//            byte[] clientName = Name.getBytes(StandardCharsets.UTF_8);
//            dataOutputStream.writeInt(clientName.length);              
//            dataOutputStream.write(clientName);
            //end sending name

            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);

            dataOutputStream.writeInt(fileContentByte.length);
            File outputFile = new File(selectedFilePath);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(fileContentByte);
            }
            if(fileContentByte.length>1000)
            {
                System.out.println("Sending :"+fileContentByte.length);
                int iterations = fileContentByte.length/1000;
                int sentData=0;
                for (int i=0;i<iterations;i++)
                {
                    dataOutputStream.writeInt(1000);
                    dataOutputStream.write(fileContentByte,sentData,1000);
                    sentData+=1000;
                    percentage=(double)sentData/fileContentByte.length*100;
                    
                    System.out.println("Percentage: "+(int)percentage);
                    MainForm.UpdateSendProgress((int)percentage);
//                    UpdateProgress(percentage);
                }
                int leftData = fileContentByte.length-sentData;
                System.out.println("Left Data: "+leftData);
                if(leftData>0)
                {
                    System.out.println("sending left data");
                dataOutputStream.writeInt(leftData);
                dataOutputStream.write(fileContentByte,sentData,leftData);
                }
                MainForm.UpdateSendProgress(100);
            }
            else
            {
                dataOutputStream.write(fileContentByte);
                System.out.println("From here");
                MainForm.UpdateSendProgress(100);
            }
            filetosend.delete();
//            dataOutputStream.flush();
            
            
        }
        return false;
    }

}
