package com.fessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 @author Jaysh Khan
 */
public class Myfile {
    private int id;
    private String sentBy;
    private String name;
    private byte[] data;
    private String fileDirectory;
    private File fileToStore;

    public Myfile(int id, String name,String sentBy, byte[] data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.sentBy=sentBy;
    }
    public void downloadFile()
    {
        //creating an empty directory with fileName
        fileToStore = new File(name);
        //creating an output stream to write file into
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileToStore);
        } catch (FileNotFoundException ex) {
           JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        //writing file
        try {
            fileOutputStream.write(data);
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String clientName) {
        this.sentBy = clientName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }
    public Object[] toRowTable()
    {
        return new Object[]{data,0,name,sentBy,"Download"};
    }


}
