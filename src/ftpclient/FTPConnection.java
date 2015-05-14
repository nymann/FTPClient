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
 * @author Emil Granberg && Nanna Dohn
 * Inspiration taken from Jakob Nordfalks FTP Connection class.
 */
public class FTPConnection {
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;

    
    /**
     * This method ensures that the client releases the threads from the server
     * when the user is done.
     */
    public void closeConnection()
    {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * This method initializes and connects to an FTP server.
     * @param host
     * @param user
     * @param code
     * @return String[]
     * @throws IOException 
     */
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

    /**
     * This method returns the server responses.
     * @return BufferedReader
     * @throws IOException 
     */
    public BufferedReader readReply() throws IOException {
        return in;
    }

    /**
     * This method sends FTP commands to the FTP server.
     * @param command
     * @return String[]
     * @throws IOException 
     */
    public String[] sendCommand(String command) throws IOException {
        System.out.println("Sent: " + command);
        out.println(command);
        out.flush();         // sørg for at data sendes til værten før vi læser svar

        List<String> aList = new ArrayList<>();
        String s;
        s = in.readLine();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("S :" + s);
        aList.add(s);
        while (in.ready()) {
            s = in.readLine();
            System.out.println("S :" + s);
            aList.add(s);
        }


        String[] sArray = new String[aList.size()];
        sArray = aList.toArray(sArray);

        return sArray;
    }

    /**
     * This method asks the server for a passive port and calculates it.
     * @return Socket
     * @throws IOException 
     */
    private Socket getDataConnection() throws IOException {
        String[] placeholder = sendCommand("PASV");
        String addrAndPort = "";

        for (String aPlaceholder : placeholder) {
            addrAndPort = aPlaceholder;
            System.out.println("Address and port: " + aPlaceholder);
        }

        StringTokenizer st = new StringTokenizer(addrAndPort, "(,)");

        System.out.println("Addresandport: "+addrAndPort);
        
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

    /**
     * This method sends data over a passive connection.
     * @param command
     * @param data
     * @throws IOException 
     */
    public void sendData(String command, String data) throws IOException {
        Socket dc = getDataConnection();
        PrintStream dataOut = new PrintStream(dc.getOutputStream());
        sendCommand(command);
        dataOut.print(data);
        dataOut.close();
        dc.close();
        readReply();
    }

    /**
     * This method receives data over a passive connection.
     * @param command
     * @return
     * @throws IOException 
     */
    public String[] receiveData(String command) throws IOException {
        
        Socket dc = getDataConnection();
        BufferedReader dataInd = new BufferedReader(new InputStreamReader(dc.getInputStream()));
        sendCommand(command);

        List<String> sa = new ArrayList<>();
        String s;

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
