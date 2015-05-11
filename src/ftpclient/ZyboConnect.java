/*
 * To change this license header, choose License Headers inStream Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inStream the editor.
 */
package ftpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lime
 */
public class ZyboConnect {

    private Socket socket;
    private PrintStream outStream;
    private BufferedReader inStream;
    
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        outStream = new PrintStream(socket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //readReply();
        //sendCommand("-ip "+host+" -port "+port);
    }

    public void closeConnection() throws IOException
    {
        socket.close();
    }
    
    public String[] readReply() throws IOException {
        String[] finalMessage;
//        
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ZyboConnect.class.getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();
//        }
           List<String> messageHolder = new ArrayList<String>();
           
           messageHolder.add(inStream.readLine());
           
           while(inStream.ready())
           {
               messageHolder.add(inStream.readLine());
           }
           
           finalMessage = new String[messageHolder.size()];
           
           finalMessage = messageHolder.toArray(finalMessage);
        
        return finalMessage;
    }

    public String[] sendCommand(String command) throws IOException {
        System.out.println("Sent: " + command);
        outStream.print(command);
        outStream.flush();
        return readReply();
    }
}
