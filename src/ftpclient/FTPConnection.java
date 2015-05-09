/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lime
 */
public class FTPConnection {
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;

    public String[] connect(String host, String user, String code) throws IOException {

        socket = new Socket(host, 21);
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        readReply();

        sendCommand("USER " + user);

        String[] replyFromServer = sendCommand("PASS " + code);

        for (String aReplyFromServer : replyFromServer) {
            System.out.println("Reply on pass request: " + aReplyFromServer);
        }

        return replyFromServer;
    }

    public BufferedReader readReply() throws IOException {
        return in;

//        while (true) 
//        {
//            String msg = in.readLine();
//            System.out.println("Reply: "+msg);
//            if (msg.length()>=3 && msg.charAt(3)!='-' && Character.isDigit(msg.charAt(0)) && Character.isDigit(msg.charAt(1)) && Character.isDigit(msg.charAt(2)))
//            {
//                return msg;
//            }
//        }
    }

    public String[] sendCommand(String command) throws IOException {
        System.out.println("Sent: " + command);
        out.println(command);
        out.flush();         // sørg for at data sendes til værten før vi læser svar
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        List<String> aList = new ArrayList<>();
        String s;

        while (in.ready()) {
            s = in.readLine();
            System.out.println("S :" + s);
            aList.add(s);
        }


        String[] sArray = new String[aList.size()];
        sArray = aList.toArray(sArray);

        return sArray;
    }

    private Socket getDataConnection() throws IOException {
        String[] placeholder = sendCommand("PASV");
        String addrAndPort = "";

        for (String aPlaceholder : placeholder) {
            addrAndPort = aPlaceholder;
            System.out.println("Address and port: " + aPlaceholder);
        }

        StringTokenizer st = new StringTokenizer(addrAndPort, "(,)");

        if (st.countTokens() < 7) {
            throw new IOException("Not logged in");
        }

        st.nextToken();
        st.nextToken();
        st.nextToken();
        st.nextToken();
        st.nextToken();

        int portNr = 256 * Integer.parseInt(st.nextToken()) + Integer.parseInt(st.nextToken());

        return new Socket(socket.getInetAddress(), portNr);
    }

    public void sendData(String command, String data) throws IOException {
        Socket dc = getDataConnection();
        PrintStream dataOut = new PrintStream(dc.getOutputStream());
        sendCommand(command);
        dataOut.print(data);
        dataOut.close();
        dc.close();
        readReply();
    }

    public String[] receiveData(String command) throws IOException {
        Socket dc = getDataConnection();
        BufferedReader dataInd = new BufferedReader(new InputStreamReader(dc.getInputStream()));
        String[] commandMsg = sendCommand(command);

        List<String> sa = new ArrayList<>();
        String s;

        for(String k : commandMsg)
        {
            sa.add(k);
        }

        while ((s = dataInd.readLine()) != null) {
            sa.add(s);
        }

        String[] recievedData = new String[sa.size()];
        recievedData = sa.toArray(recievedData);

        dataInd.close();
        dc.close();

        return recievedData;
    }
}
