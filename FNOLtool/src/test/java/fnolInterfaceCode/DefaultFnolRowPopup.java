package fnolInterfaceCode;

import javax.swing.JFrame;

public class DefaultFnolRowPopup 
{
	public DefaultFnolRowPopup()
	{
		final JFrame frame = new JFrame("Please Select Row Numbers For Default Run");
		
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
