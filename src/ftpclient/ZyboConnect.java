package ftpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emil Granberg && Nanna Dohn
 */
public class ZyboConnect {

    private Socket socket;
    private PrintStream outStream;
    private BufferedReader inStream;
    
    /**
     * Connects to the sensor server.
     * @param host
     * @param port
     * @throws IOException 
     */
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        outStream = new PrintStream(socket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Closes the connection and releases the threads on the server.
     * @throws IOException 
     */
    public void closeConnection() throws IOException
    {
        socket.close();
    }
    
    /**
     * Read server replies.
     * @return String[]
     * @throws IOException 
     */
    public String[] readReply() throws IOException {
        
        String[] finalMessage;
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

    /**
     * Send sensor server commands to the sensor server.
     * @param command
     * @return String[]
     * @throws IOException 
     */
    public String[] sendCommand(String command) throws IOException {
        System.out.println("Sent: " + command);
        outStream.print(command);
        outStream.flush();
        return readReply();
    }
}
