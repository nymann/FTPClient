package ftpclient;

import javax.swing.*;

public class DisplayGUI {

	public static void main(String[] arg) {
                
		TabHolder panel = new TabHolder();        // Create a new TabHolder.
		JFrame vindue = new JFrame("FTP/Sensor Client");    // Create new JFrame
		vindue.add(panel); // add TabHolder to JFrame

		vindue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // React to the close window button.
                vindue.setLocation(350, 200); // set starting location of the JFrame.
		vindue.pack(); // finalizes the window. Dimensions, displayable
		vindue.setVisible(true); // paints the window for the user.
	}   
} 
