package com.fessage;

import com.mycompany.loginform.MainForm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * @author Jaysh Khan
 * */
public class Server implements Runnable{
    private static final int PORT=5002;
    ServerSocket serverSocket ;
    int fileID=0;

    
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    //creating a thread pool of 4 means our server can accept only 4 clients
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    
    public static void clientToremove(Socket client)
    {
        for(ClientHandler c:clients)
        {
            if(c.getClient()==client)
            {
                clients.remove(c);
            }
        }
                   
                      
             
    }
    // TODO user can send multiple files
   

    @Override
    public void run() {
        int clientID = 0;
        fileID=0;
        System.out.println("Server..");
        try {
            serverSocket=new ServerSocket(PORT);
        } catch (IOException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        int noOfClients=0;
        
            
            System.out.println("Clients: "+noOfClients);
            MainForm.UpdateServer("waiting..");
            System.out.println("in loop of server");
            Socket client;
            try {
                while(noOfClients<=4)
                {
                client = serverSocket.accept();
                
                System.out.println("Client Entered");
                MainForm.UpdateServer("Accepted");
                //client is accepted
                //creating a client handler thread
                ClientHandler clientThread;
                try {
                    clientThread = new ClientHandler(client);
                    //checking CLient name
                    boolean alreadyExists=false;
                    for(ClientHandler c:clients)
                    {
                        if(client.isConnected())
                        {
                        System.out.println("cname: "+c.getClientName());
                        System.out.println("fname: "+clientThread.getClientName());
                        if (c.getClientName().equalsIgnoreCase(clientThread.getClientName())) {
                            alreadyExists=true;
                        }
                        }
                        else{
                            alreadyExists=true;
                        }
                    }
                    //adding client to the list
                    if(!alreadyExists)
                    {
                        MainForm.UpdateClientTable(clientThread.toRowTable(clientID));
                        clients.add(clientThread);
                        System.out.println("row updated");
                        clientID++;
                        //running the thread
                        pool.execute(clientThread);
                        
                        System.out.println("Pool executed");
                    }
                    
                
                    
            } catch (IOException ex) {
                JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
            }
        }
            } catch (IOException ex) {
                JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
            }
           
            noOfClients=clients.size();
            System.out.println("end loop");
            //all code is in CLienthandler
        }
    

}
