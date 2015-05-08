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

    public void readReply() throws IOException {
        try {
         //   char rxBuffer[] = new char[1400];
           
            Thread.sleep(500);
           while(inStream.ready())
           {
               String s = inStream.readLine();
               System.out.println("Reply: "+s);
           }
           
        } catch (InterruptedException ex) {
            Logger.getLogger(ZyboConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendCommand(String command) throws IOException {
        System.out.println("Sent: " + command);
        outStream.print(command);
        outStream.flush();
        readReply();
    }
}
