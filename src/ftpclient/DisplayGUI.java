package ftpclient;

import javax.swing.JFrame;

public class DisplayGUI {

	public static void main(String[] arg) {
                
		TabHolder panel = new TabHolder();        // opret panelet
		JFrame vindue = new JFrame("FTP/Sensor Client");    // opret et vindue på skærmen
		vindue.add(panel);                          // vis panelet i vinduet

		vindue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // reagér på luk
//		vindue.setSize(350, 70);                       // sæt vinduets størrelse
		vindue.pack();
		vindue.setVisible(true);
	}   
} 
