package fnolInterfaceCode;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import fnolMethods.CollectFnolData;
import logging.Logger;


public class GuiCode 
{
	static String defaultFnolLocation = "";
	static String defaultResultingFolderLocation = "";
	
	static String fnolLocationSaved = "";
	static String resultingFolderSaved = "";
	
	static String deviceReportSaved = "";
	static String defaultDeviceReportLocation = "";
	
	public static void createGui()
	{
		try
		{
			final int PROGRESS_MIN = 0;
			final int PROGRESS_MAX = 6500;
			String f = new File("").getAbsolutePath();
			String fnolLocation = f+"\\fnolFolder";
			String resultingFolder = f+"\\resultingFolder";
			String deviceReportFolder = f+"\\deviceReportFolder";
			
			defaultFnolLocation = fnolLocation;
			defaultResultingFolderLocation = resultingFolder;
			defaultDeviceReportLocation = deviceReportFolder;
			
			BufferedImage cmsLogo = ImageIO.read(new File(f+"\\appFiles\\images\\cmsLogoNew.png"));
			JLabel picLabel = new JLabel(new ImageIcon(cmsLogo));
			final JPanel panel = new JPanel();
			
			final JFrame frame = new JFrame("FNOL Application - (v1.3)");
			
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JLabel fnolLocationTitle = new JLabel("FNOL Folder Location:");
			fnolLocationTitle.setBounds(24, 120, 150, 20);
			final JTextField fnolLocationTextField = new JTextField();
			fnolLocationTextField.setBounds(24, 140, 550, 30);
			fnolLocationTextField.setEditable(false);
			JButton fnolBrowse = new JButton("Browse");
			fnolBrowse.setBounds(580, 140, 95, 30);
			
			JLabel resultingFileTitle = new JLabel("Resulting File Location:");
			resultingFileTitle.setBounds(24, 200, 150, 20);
			final JTextField resultingFileTextField = new JTextField();
			resultingFileTextField.setBounds(24, 220, 550, 30);
			resultingFileTextField.setEditable(false);
			JButton resultingFileBrowse = new JButton("Browse");
			resultingFileBrowse.setBounds(580, 220, 95, 30);
			
			JLabel deviceReportLabel = new JLabel("Device Report Location:");
			deviceReportLabel.setBounds(24, 280, 150, 20);
			final JTextField deviceReportTextField = new JTextField();
			deviceReportTextField.setBounds(24, 300, 550, 30);
			deviceReportTextField.setEditable(false);
			JButton deviceReportBrowse = new JButton("Browse");
			deviceReportBrowse.setBounds(580, 300, 95, 30);
			
			final JButton startButton = new JButton("Start");
			startButton.setEnabled(false);
			startButton.setVisible(true);
			startButton.setBounds(580, 370, 95, 40);
			
			final JButton exitButton = new JButton("Exit");
			exitButton.setEnabled(true);
			exitButton.setVisible(true);
			exitButton.setBounds(480, 370, 95, 40);
			
			final JProgressBar pBar = new JProgressBar();
			pBar.setMinimum(PROGRESS_MIN);
			pBar.setMaximum(PROGRESS_MAX);
			pBar.setBounds(24, 370, 450, 40);
			
			final JLabel progressBarText = new JLabel("Test Unable to Start");
			progressBarText.setBounds(24, 350, 440, 20);
			progressBarText.setFont(new Font(progressBarText.getName(), Font.PLAIN, 10));
			
			
			panel.setLayout(null);			
			picLabel.setBounds(-65, -10, 300, 130);
			panel.add(picLabel);
			panel.add(fnolLocationTitle);
			panel.add(fnolLocationTextField);
			panel.add(resultingFileTitle);
			panel.add(resultingFileTextField);
			panel.add(fnolBrowse);
			panel.add(resultingFileBrowse);
			panel.add(deviceReportLabel);
			panel.add(deviceReportTextField);
			panel.add(deviceReportBrowse);
			panel.add(startButton);
			panel.add(exitButton);
			panel.add(pBar);
			panel.add(progressBarText);
			
			frame.setSize(700, 470);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
			
			fnolLocationTextField.setText(defaultFnolLocation);
			resultingFileTextField.setText(defaultResultingFolderLocation);
			deviceReportTextField.setText(defaultDeviceReportLocation);
			
			
			startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, progressBarText));
			
			
			
			fnolBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser fileChooser = new JFileChooser(defaultFnolLocation);
					
					fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					FileNameExtensionFilter filter = new FileNameExtensionFilter(".xls", "(*.xls)","xls");
					fileChooser.setFileFilter(filter);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						String selectedPath = fileChooser.getSelectedFile().toString();
						if(selectedPath.endsWith(".xls"))
							selectedPath = selectedPath.substring(0 ,selectedPath.lastIndexOf("\\"));
						fnolLocationTextField.setText(selectedPath);
						fnolLocationSaved = selectedPath;
						defaultFnolLocation = selectedPath;
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, progressBarText));
					}
				}
			});
			
			resultingFileBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser fileChooser = new JFileChooser(defaultFnolLocation);
					
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						resultingFileTextField.setText(fileChooser.getSelectedFile().toString());
						resultingFolderSaved = fileChooser.getSelectedFile().toString();
						defaultResultingFolderLocation = fileChooser.getSelectedFile().toString();
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, progressBarText));
					}
				}
			});
			
			deviceReportBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser fileChooser = new JFileChooser(defaultDeviceReportLocation);
					FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "(*.csv)","csv");
					fileChooser.setFileFilter(filter);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						String selectedPath = fileChooser.getSelectedFile().getAbsolutePath().toString();
						deviceReportTextField.setText(selectedPath);
						deviceReportSaved = selectedPath;
						defaultDeviceReportLocation = selectedPath;
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, progressBarText));
					}
				}
			});
			
			startButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					updateProgressText("Test Start", progressBarText, panel);
					updateProgressBar(pBar, 100);
					String fnolLocation = fnolLocationSaved;
					updateProgressText("Setting FNOL Location", progressBarText, panel);
					updateProgressBar(pBar, 200);
					if(fnolLocationSaved.equals(""))
						fnolLocation = defaultFnolLocation;
					updateProgressBar(pBar, 400);
					updateProgressText("Setting Resulting File Location", progressBarText, panel);
					String rfLocation = resultingFolderSaved;
					updateProgressText("Resulting File Location Saved", progressBarText, panel);
					updateProgressBar(pBar, 500);
					if(resultingFolderSaved.equals(""))
						rfLocation = defaultResultingFolderLocation;
					updateProgressBar(pBar, 600);
					File[] fnolFiles = CollectFnolData.getAllFnolFiles(fnolLocation, pBar, progressBarText, panel);
					updateProgressBar(pBar, 1100);
					CollectFnolData.convertDeviceReportToXlsx(deviceReportSaved, pBar, progressBarText, panel);
					updateProgressBar(pBar, 2100);
					CollectFnolData.createResultingFile(rfLocation, pBar, progressBarText, panel);
					updateProgressBar(pBar, 3000);
					CollectFnolData.copyFnolDataIntoSpreadsheet(fnolFiles, deviceReportTextField.getText(), pBar, progressBarText, panel);
					updateProgressBar(pBar, 6200);
					updateProgressText("Delete Converted Device Report", progressBarText, panel);
					CollectFnolData.deleteDeviceReportXlsx(deviceReportSaved);
					updateProgressText("Converted Device Report Deleted", progressBarText, panel);
					updateProgressBar(pBar, 6400);
					updateProgressText("Renaming Resulting File", progressBarText, panel);
					CollectFnolData.renameResultingFile(rfLocation);
					updateProgressText("Renaming Completed", progressBarText, panel);
					updateProgressBar(pBar, PROGRESS_MAX);
					updateProgressText("Test Completed. Cleaning up.", progressBarText, panel);
					updateProgressText("All Options Valid, Test May Now Begin", progressBarText, panel);
					try 
					{
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e1) 
					{
						int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
						Logger.recordError(e1, lineNum);
						String f = new File("").getAbsolutePath();
						String loggingDirectory = f+"\\errorLogging";
						ErrorGui.openErrorGui(loggingDirectory);
					}
					updateProgressBar(pBar, PROGRESS_MIN);
				}
			});
			
			exitButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{	
					frame.dispose();
				}
			});
			
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
		}
	}
	
	public static void updateProgressBar(JProgressBar bar, int progressAmount)
	{
		bar.setValue(progressAmount);
		bar.update(bar.getGraphics());
	}
	
	public static void updateProgressText(String text ,JLabel progressLabel, JPanel panel)
	{
		progressLabel.setOpaque(true);
		progressLabel.setText(text);
		progressLabel.update(progressLabel.getGraphics());
		progressLabel.setOpaque(true);
		panel.repaint();
	}
	
	public static void setLogFileLocationSaved(String fnolLocation) 
	{
		GuiCode.fnolLocationSaved = fnolLocation;
	}
	
	public static boolean enableStart(JTextField fnol, JTextField resultingFile, JTextField deviceReport, JLabel progressBarText)
	{
		boolean enableStartButton = false;
		
		if(!(fnol.getText().isEmpty()))
			if(!(resultingFile.getText().isEmpty()))
				if(!(deviceReport.getText().isEmpty()))
					if(deviceReport.getText().endsWith(".csv"))
					{
						progressBarText.setText("All Options Valid, Test May Now Begin");
						enableStartButton = true;
					}
						
		return enableStartButton;
	}
}
