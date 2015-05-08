/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lime
 */
public class FTPConnection
{
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;
    
    public BufferedReader connect(String host, String user, String code) throws IOException
    {
        socket = new Socket(host,21);
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        readReply();
        sendCommand("USER "+user);
        BufferedReader replyFromServer = sendCommand("PASS "+code);
        return replyFromServer;
    }
    
    public BufferedReader readReply() throws IOException
    {
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
    
    public BufferedReader sendCommand(String command) throws IOException
    {
        System.out.println("Sent: "+command);
        out.println(command);
        out.flush();         // sørg for at data sendes til værten før vi læser svar
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return readReply();
    }
    
    private Socket getDataConnection() throws IOException
    {
        BufferedReader placeholder = sendCommand("PASV");
        String addrAndPort = "";
        
        while(placeholder.ready())
        {
            addrAndPort = in.readLine();
        }

        StringTokenizer st = new StringTokenizer(addrAndPort, "(,)");
        
        if (st.countTokens() < 7)
        {
            throw new IOException("Not logged in");    
        }

        st.nextToken(); st.nextToken(); st.nextToken(); st.nextToken(); st.nextToken();
        
        int portNr = 256*Integer.parseInt(st.nextToken()) + Integer.parseInt(st.nextToken());
        
        return new Socket(socket.getInetAddress(), portNr);
    }
    
    public void sendData(String command, String data) throws IOException
	{
		Socket dc = getDataConnection();
		PrintStream dataOut = new PrintStream( dc.getOutputStream() );
		sendCommand(command);
		dataOut.print(data);
		dataOut.close();
		dc.close();
		readReply();
	}

	public String receiveData(String command) throws IOException
	{
		Socket dc = getDataConnection();
		BufferedReader dataInd = new BufferedReader(new InputStreamReader(
		dc.getInputStream()));
		sendCommand(command);
		StringBuilder sb = new StringBuilder();
		String s = dataInd.readLine();
                
		while (s != null) {
			System.out.println("data: "+s);
			sb.append(s+"\n");
			s = dataInd.readLine();
		}
		dataInd.close();
		dc.close();
		readReply();
		return sb.toString();
	}
}
