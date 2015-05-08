/*
. * To change this license header, choose License Headers in Project Properties...
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Lime
 */
public class FTPClient {

    private static Scanner scanner = new Scanner(System.in); // create scanner object.
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        int Input = 0;
        
        while(true)
        {
            System.out.println("Choose application:");
            System.out.println("1. FTP");
            System.out.println("2. Sensors");

            Input = scanner.nextInt();
            scanner.nextLine();
        
            if (Input == 1)
            {
                FTPRun();
                Input = 0;
            }
            if( Input == 2)
            {
                ZyboClientRun();
                Input = 0;
            }
        }
    }
    
    private static void ZyboClientRun() throws IOException
    {
        ZyboConnect zybo = new ZyboConnect();
        
        System.out.println("Host:");
        String host = scanner.nextLine();
        System.out.println("Port:");
        int port = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        
        zybo.connect(host, port);
        
        while (true)
        {   
            System.out.println("1. Get Sensor amount");
            System.out.println("2. Get Sensor info");
            System.out.println("3. ECHO Service");
            System.out.println("4. Set sensor samplerate");
            System.out.println("5. Stop Sensor");
            System.out.println("6. Start Sensor");
            System.out.println("7. Status");
            System.out.println("8. Quit");
            
            int opt = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            if(opt == 1)
            {
                zybo.sendCommand("TM20 GSA");
                opt = 0;
            }
            else if(opt == 2)
            {
                System.out.println("Select Sensor:");
                int sensor = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                zybo.sendCommand("TM20 GET_S "+sensor);
                opt = 0;
            }
            else if(opt == 3)
            {
                System.out.println("Type msg:");
                String msg1 = scanner.nextLine();

                zybo.sendCommand("TM20 ECHO "+msg1);
                opt = 0;
            }
            else if(opt == 4)
            {
                System.out.println("Select Sensor:");
                int sensor = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                System.out.println("Set Sample rate:");
                int sampleRate = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                zybo.sendCommand("TM20 SEN_SET "+sensor+" "+sampleRate);
                opt = 0;
            }
            else if(opt == 5)
            {
                System.out.println("Select Sensor:");
                int sensor = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                zybo.sendCommand("TM20 STOP_S "+sensor);
                opt = 0;
            }
            else if(opt == 6)
            {
                System.out.println("Select Sensor:");
                int sensor = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                zybo.sendCommand("TM20 START_S "+sensor);
                opt = 0;
            }
            else if(opt == 7)
            {
                zybo.sendCommand("TM20 STATUS");
                opt = 0;
            }
            else if(opt == 8)
            {
                zybo.sendCommand("TM20 KILL_C");
                opt = 0;
                break;
            }
            else
            {
                System.out.println(opt+" Is not an option.");
                opt = 0;
            }
        }
    }
    
    private static void FTPRun() throws IOException{
        FTPConnection ftp = new FTPConnection();
        
        System.out.println("Host:");
        String host = scanner.nextLine();
        
        System.out.println("User:");
        String user = scanner.nextLine();
        
        System.out.println("Password:");
        String password = scanner.nextLine();
        ftp.connect(host,user,password);
        
        while(true)
        {
            System.out.println("1. HELP");
            System.out.println("2. LIST");
            System.out.println("3. RETR");
            System.out.println("4. STOR");
            System.out.println("5. EXIT");
            int option = scanner.nextInt();
            scanner.nextLine();

            if(option == 1) // HELP
            {
                ftp.sendCommand("HELP");
                option = 0;
            }
            else if(option == 2) // LIST
            {
                ftp.receiveData("LIST");
                option = 0;
            }
            else if(option == 3) // RETR
            {
                String filename = scanner.nextLine();
                ftp.receiveData("RETR "+filename);
                option = 0;
            }
            else if(option == 4) // STOR (Send data, save as file)
            {
                System.out.println("Type filename:");
                String filename = scanner.nextLine();
                System.out.println("Type filecontent:");
                String filecontent = scanner.nextLine();
                ftp.sendData("STOR "+filename,filecontent);
                option = 0;
            }
            else if(option == 5)
            {
                option = 0;
                break;
            }
            else
            {
                System.out.println(option+" is not an option.");
                option = 0;
            }
        }
    }
}