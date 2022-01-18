package com.fessage;

import com.mycompany.loginform.MainForm;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Jaysh Khan
 */
public class ClientHandler implements Runnable{
    private Socket client;
    private DataInputStream incomingFile;

    private String ClientIP;
    private String clientName;
    private int fileNameLength;
    private String fileNameData;
    private int incomingFileLength;
    private byte[] incomingFileData;
    private int fileId=0;
    private static ArrayList<Myfile> files = new ArrayList<>();

    public ClientHandler(Socket clientSocket) throws IOException {
        //creating connection
        this.client=clientSocket;
        ClientIP=this.client.getInetAddress().getHostAddress();
        //Getting File from Client
        this.incomingFile = new DataInputStream(client.getInputStream());
        readClientName();
       
    }
    public Socket getClient()
    {
        return client;
    }
    public String getClientName()
    {
        return this.clientName;
    }
    public Object[] toRowTable(int id)
    {
        return new Object[]{client,id,clientName,ClientIP};
    }
    public void readClientName()
    {

        int clientNameLength = 0;
        try {
            clientNameLength = incomingFile.readInt();
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        byte[] recievedClientName = new byte[clientNameLength];
        try {
            incomingFile.readFully(recievedClientName);
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        this.clientName=new String(recievedClientName);
        System.out.println(clientName);

        //Getting fileName length from Client
        
    }
    public boolean readFileName()
    {
        try {
            this.fileNameLength=incomingFile.readInt();
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        if(fileNameLength>0) {
            //creating byte array of filename length
            byte[] fileName = new byte[fileNameLength];
            //reading fileNameByte
            try {
                incomingFile.readFully(fileName, 0, fileName.length);
            } catch (IOException ex) {
                JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
            }
            //converting byte[] to String
            this.fileNameData = new String(fileName);
            return true;
        }
        else return false;
    }
    public String getFileName()
    {
        return fileNameData;
    }
//    public void downloadFile(int id)
//    {
//        for(Myfile file:files)
//        {
//            if (file.getId()==id) {
//                file.downloadFile();
//                JOptionPane.showMessageDialog(null, "Downloaded", "InfoBox: Download", JOptionPane.INFORMATION_MESSAGE);
//            }
//        }
//    }
    public void readFile() throws IOException
    {
        //receiving incomingFile Length
        try {
            this.incomingFileLength = incomingFile.readInt();
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        //checking if we have received the fileLength
        if (incomingFileLength > 0) {
            MainForm.incomingFileName(fileNameData);
            this.incomingFileData = new byte[incomingFileLength];
            System.out.println("FFile: "+incomingFileLength);
            if(incomingFileLength>1000)
            {
                int iterations = incomingFileLength/1000;
                System.out.println("iteration: "+iterations);
                System.out.println("DSBS: "+(iterations*1000));
                int receivedData=0;
                for (int i=0;i<iterations;i++)
                {
                    int incomingDataBytes = 0;
                    try {
                        incomingDataBytes = incomingFile.readInt();
                    } catch (IOException ex) {
                        JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
                    }
                    try {
                        incomingFile.readFully(incomingFileData,receivedData,incomingDataBytes);
                    } catch (IOException ex) {
                        JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
                    }
                    receivedData+=incomingDataBytes;
                    //set Progress
                    double percentage =(double) receivedData/incomingFileLength*100;
                    //System.out.println("Receieved: "+percentage+"%");
                    MainForm.UpdateReceievedProgress((int)percentage);

                }
                int leftd = receivedData-incomingFileLength;
                System.out.println("Left Serv dat: "+leftd);
                if(receivedData!=incomingFileLength)
                {
                    System.out.println("retriving left data");
                     int incomingDataBytes = incomingFile.readInt();
                     incomingFile.readFully(incomingFileData,receivedData,incomingDataBytes);
                     receivedData+=incomingDataBytes;
                }
                System.out.println("recievec Data:"+ receivedData);
                MainForm.UpdateReceievedProgress(100);
                //Storing file Logic

            }
            else {
                //reading file into byte[]
                try {
                    incomingFile.readFully(incomingFileData, 0, incomingFileLength);
                    MainForm.UpdateReceievedProgress(100);
                } catch (IOException ex) {
                    JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
                }
            }

            Myfile file = new Myfile(fileId,fileNameData,clientName,incomingFileData); 
            files.add(file);
            MainForm.UpdateFileTable(file.toRowTable());

            //close the connection
//                fileOutputStream.close();
//                incomingFile.close();
//                client.close();
        }
    }
    @Override
    public void run() {

        //Getting Client Name

            //checking if we have received the file
            while (true) {            
            
        
            if (readFileName()) {
                try {
                    readFile();
                } catch (IOException ex) {
                    JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
                }
            }
           }

    }

   
}
