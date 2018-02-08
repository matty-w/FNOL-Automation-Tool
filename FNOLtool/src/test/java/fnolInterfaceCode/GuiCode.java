package fnolInterfaceCode;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;
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
			
			String openMfiles = "M:\\Cloud Vault";
			final String f = new File("").getAbsolutePath();
			
			final File configFile = new File(f+"\\appFiles\\configFolder\\savedConfig.txt");
			if(!(configFile.exists()))
			{
				String fnolLocation = f+"\\fnolFolder";
				String resultingFolder = f+"\\resultingFolder";
				String deviceReportFolder = f+"\\deviceReportFolder";
				PrintWriter writer = new PrintWriter(configFile);
				writer.println("COMBO_CHOICE=0");
				writer.println("FNOL_LOCATION="+fnolLocation);
				writer.println("RESULTING_FOLDER_LOCATION="+resultingFolder);
				writer.println("DEVICE_REPORT_FOLDER="+deviceReportFolder);
				writer.println("FNOL_ROWS_SELECTED=");
				writer.println("");
				writer.close();
			}
			
			
			String path = "M:\\Cloud Vault\\ID2\\D943E0A6-C9D3-4573-BDD0-DE54DBA3ED92\\0\\14000-14999\\14807\\L\\L\\Claim Flat File (ID 14807).xlsx";
			String claimsFileCopyPath = f+"\\appFiles\\claimsFileCopy\\flatFile.xlsx";
			Desktop d = null;
			File mfiles = new File(openMfiles);
			if(Desktop.isDesktopSupported())
			{
				if(mfiles.exists())
				{
					d = Desktop.getDesktop();
					d.open(mfiles);
				}
			}
			File file = new File(path);
			final File copyFile = new File(claimsFileCopyPath);
			Files.deleteIfExists(copyFile.toPath());
			FileUtils.copyFile(file, copyFile);

				
			final int PROGRESS_MIN = 0;
			final int PROGRESS_MAX = 6500;
			
			String comboOption = getConfigDetails(configFile, 0);
			String fnolLocation = getConfigDetails(configFile, 1);
			String resultingFolder = getConfigDetails(configFile, 2);
			String deviceReportFolder = getConfigDetails(configFile, 3);
			
			defaultFnolLocation = fnolLocation;
			defaultResultingFolderLocation = resultingFolder;
			defaultDeviceReportLocation = deviceReportFolder;
			
			BufferedImage cmsLogo = ImageIO.read(new File(f+"\\appFiles\\images\\cmsLogoNew.png"));
			JLabel picLabel = new JLabel(new ImageIcon(cmsLogo));
			final JPanel panel = new JPanel();
			
			final JFrame frame = new JFrame("FNOL Application - (v1.5)");
			ImageIcon icon;
			icon = new ImageIcon(f+"\\appFiles\\images\\fnol.png");
			frame.setIconImage(icon.getImage());
			
			
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
			
			String[] comboBoxOptions = {"Tesco FNOL Default", "Custom FNOL Run"};
			final JComboBox<String> comboBox = new JComboBox<String>(comboBoxOptions);
			comboBox.setSelectedIndex(1);
			comboBox.setBounds(320, 70, 254, 30);
			final JButton customiseRows = new JButton("Rows");
			customiseRows.setBounds(580, 70, 95, 30);
			if(comboOption.equals("0"))
				comboBox.setSelectedItem(comboBox.getItemAt(0));
			else
				comboBox.setSelectedItem(comboBox.getItemAt(1));
			
			
			JButton saveAsDefaultButton = new JButton("Save Updated Settings");
			saveAsDefaultButton.setBounds(500, 10, 175, 30);
			
			JButton restoreDefaultSettings = new JButton("Restore Default Settings");
			restoreDefaultSettings.setBounds(320, 10, 175, 30);
			
			
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
			panel.add(comboBox);
			panel.add(customiseRows);
			panel.add(saveAsDefaultButton);
			panel.add(restoreDefaultSettings);
			
			frame.setSize(700, 470);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
			frame.setResizable(false);
			
			fnolLocationTextField.setText(defaultFnolLocation);
			resultingFileTextField.setText(defaultResultingFolderLocation);
			deviceReportTextField.setText(defaultDeviceReportLocation);
			
			customiseRows.setEnabled(enableRowsButton(comboOption));
			startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, progressBarText));
			
			
			
			comboBox.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(comboBox.getSelectedItem().toString().equals("Custom FNOL Run"))
						customiseRows.setEnabled(true);
					else
						customiseRows.setEnabled(false);
				}
			});
			
			restoreDefaultSettings.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					List<String> values = new ArrayList<String>();
					String comboOption = "0";
					String fnolLocation = f+"\\fnolFolder";
					String resultingFolder = f+"\\resultingFolder";
					String deviceReportFolder = f+"\\deviceReportFolder";
					String fnolRows = "";
					
					comboBox.setSelectedItem(comboBox.getItemAt(0));
					fnolLocationTextField.setText(fnolLocation);
					resultingFileTextField.setText(resultingFolder);
					deviceReportTextField.setText(deviceReportFolder);
					
					values.add(comboOption);
					values.add(fnolLocation);
					values.add(resultingFolder);
					values.add(deviceReportFolder);
					values.add(fnolRows);
					
					replaceConfigSettings(configFile, values);
					
				}
			});
			
			saveAsDefaultButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					List<String> values = new ArrayList<String>();
					String comboBoxSetting;
					
					if(comboBox.getSelectedItem().toString().equals("Tesco FNOL Default"))
						comboBoxSetting = "0";
					else
						comboBoxSetting = "1";
					
					
					String newFnolLocation = fnolLocationTextField.getText();
					String newResultingFileLocation = resultingFileTextField.getText();
					String newDeviceReportLocation = deviceReportTextField.getText();
					
					values.add(comboBoxSetting);
					values.add(newFnolLocation);
					values.add(newResultingFileLocation);
					values.add(newDeviceReportLocation);
					values.add("");
					
					replaceConfigSettings(configFile, values);
				}
			});
			
			
			fnolBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.setAlwaysOnTop(false);
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
					frame.setAlwaysOnTop(false);
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
					frame.setAlwaysOnTop(false);
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
					CollectFnolData.copyFnolDataIntoSpreadsheet(fnolFiles, deviceReportTextField.getText(), pBar, progressBarText, panel, copyFile);
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
					try 
					{
						TimeUnit.SECONDS.sleep(3);
						updateProgressBar(pBar, PROGRESS_MIN);
						String path = rfLocation;
						Desktop d = null;
						File file = new File(path);
						if(Desktop.isDesktopSupported())
						{
							d = Desktop.getDesktop();
							d.open(file);
						}
					}
					catch (Exception e1) 
					{
						int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
						Logger.recordError(e1, lineNum);
						String f = new File("").getAbsolutePath();
						String loggingDirectory = f+"\\errorLogging";
						ErrorGui.openErrorGui(loggingDirectory);
					}
					updateProgressText("All Options Valid, Test May Now Begin", progressBarText, panel);
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
	
	public static boolean enableRowsButton(String comboOption)
	{
		if(comboOption.equals("0"))
			return false;
		else
			return true;
	}
	
	private static String getConfigDetails(File configFile, int row)
	{
		try 
		{
			String line;
			FileInputStream fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			int i = 0;
			while((line = br.readLine()) != null)
			{
				if(i == row)
				{
					String lineComplete = line.substring(line.indexOf("=")+1);
					br.close();
					return lineComplete;
				}
				else
				{
					i++;
				}
			}
			br.close();
		}
		catch (Exception e) 
		{
		}
		return "";
	}
	
	
	private static void replaceConfigSettings(File file, List<String> newValues)
	{
		try 
		{
			String comboBox = "COMBO_CHOICE="+newValues.get(0);
			String fnol = "FNOL_LOCATION="+newValues.get(1);
			String resultingFolder = "RESULTING_FOLDER_LOCATION="+newValues.get(2);
			String deviceReport = "DEVICE_REPORT_FOLDER="+newValues.get(3);
			String rowSettings = "FNOL_ROWS_SELECTED="+newValues.get(4);
			PrintWriter writer = new PrintWriter(file);
			writer.println(comboBox);
			writer.println(fnol);
			writer.println(resultingFolder);
			writer.println(deviceReport);
			writer.println(rowSettings);
			writer.close();
		} 
		catch (Exception e) 
		{
		}
	}
}
