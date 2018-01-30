package fnolInterfaceCode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ErrorGui 
{
	public static void openErrorGui(String loggingDirectory)
	{
		try
		{
			JButton exit = new JButton("OK");
			JLabel label = new JLabel("<html>An Error has occurred preventing the Application from continuing,"
					+ " please navigate to the Application Logging Directory ("+loggingDirectory+") and send the File relating to the time of the run."+
					" Please click the 'OK' button to close the Application.</html>");
			final JPanel panel = new JPanel();
			
			label.setBounds(40, 20, 515, 100);
			exit.setBounds(265, 120, 70, 30);
			
			final JFrame frame = new JFrame("FNOL Application - Error Has Occured, Shutting Down");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			panel.setLayout(null);
			panel.add(label);
			panel.add(exit);
			frame.add(panel);
			frame.setSize(600, 200);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			
			exit.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.dispose();
				}
			});
		}
		catch(Exception e)
		{
		}
	}
}
