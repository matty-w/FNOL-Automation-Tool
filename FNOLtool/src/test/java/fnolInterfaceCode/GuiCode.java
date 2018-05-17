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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	static String claimsFileSaved = "";
	static String defaultDeviceReportLocation = "";
	static String defaultClaimsFileLocation = "";
	
	static boolean isSelectedAlready = false;
	
	public static void createGui()
	{
		try
		{
			
			final String f = new File("").getAbsolutePath();
			
			final File configFile = new File(f+"\\appFiles\\configFolder\\savedConfig.txt");
			if(!(configFile.exists()))
			{
				String emailLocation = f+"\\emailFolder";
				String resultingFolder = f+"\\resultingFolder";
				String deviceReportFolder = f+"\\deviceReportFolder";
				String claimsFlatFileFolder = f+"\\claimsFlatFileFolder";
				PrintWriter writer = new PrintWriter(configFile);
				writer.println("COMBO_CHOICE=0");
				writer.println("FNOL_LOCATION="+emailLocation);
				writer.println("RESULTING_FOLDER_LOCATION="+resultingFolder);
				writer.println("DEVICE_REPORT_FOLDER="+deviceReportFolder);
				writer.println("CLAIMS_FILE_FOLDER="+claimsFlatFileFolder);
				writer.println("FNOL_ROWS_SELECTED=");
				writer.println("CLAIMS_CHECKBOX=1");
				writer.println("DEVICE_CHECKBOX=1");
				writer.println("");
				writer.close();
			}
			
			final int PROGRESS_MIN = 0;
			final int PROGRESS_MAX = 6500;
			
			String comboOption = getConfigDetails(configFile, 0);
			String fnolLocation = getConfigDetails(configFile, 1);
			String resultingFolder = getConfigDetails(configFile, 2);
			String deviceReportFolder = getConfigDetails(configFile, 3);
			String claimsFolder = getConfigDetails(configFile, 4);
			String rowOptionsString = getConfigDetails(configFile, 5);
			
			String checkBoxOption = getConfigDetails(configFile, 6);
			final String deviceCheckboxOption = getConfigDetails(configFile, 7);
			
			defaultFnolLocation = fnolLocation;
			defaultResultingFolderLocation = resultingFolder;
			defaultDeviceReportLocation = deviceReportFolder;
			defaultClaimsFileLocation = claimsFolder;
			
			BufferedImage cmsLogo = ImageIO.read(new File(f+"\\appFiles\\images\\cmsLogoNew.png"));
			JLabel picLabel = new JLabel(new ImageIcon(cmsLogo));
			final JPanel panel = new JPanel();
			
			final JFrame frame = new JFrame("FNOL Application - (v1.6)");
			ImageIcon icon;
			icon = new ImageIcon(f+"\\appFiles\\images\\fnol.png");
			frame.setIconImage(icon.getImage());
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JLabel fnolLocationTitle = new JLabel("Email Folder Location:");
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
			final JButton deviceReportBrowse = new JButton("Browse");
			deviceReportBrowse.setBounds(580, 300, 95, 30);
			
			JLabel claimsFileCopyLabel = new JLabel("Claims Flat File Copy:");
			claimsFileCopyLabel.setBounds(24, 360, 150, 20);
			final JTextField claimsFileCopyTextField = new JTextField();
			claimsFileCopyTextField.setBounds(24, 380, 550, 30);
			claimsFileCopyTextField.setEditable(false);
			final JButton claimsFileCopyBrowse = new JButton("Browse");
			claimsFileCopyBrowse.setBounds(580, 380, 95, 30);
			
			final JButton startButton = new JButton("Start");
			startButton.setEnabled(false);
			startButton.setVisible(true);
			startButton.setBounds(580, 460, 95, 40);
			
			final JButton exitButton = new JButton("Exit");
			exitButton.setEnabled(true);
			exitButton.setVisible(true);
			exitButton.setBounds(480, 460, 95, 40);
			
			final JProgressBar pBar = new JProgressBar();
			pBar.setMinimum(PROGRESS_MIN);
			pBar.setMaximum(PROGRESS_MAX);
			pBar.setBounds(24, 460, 450, 40);
			
			final JLabel progressBarText = new JLabel("Test Unable to Start");
			progressBarText.setBounds(24, 440, 440, 20);
			progressBarText.setFont(new Font(progressBarText.getName(), Font.PLAIN, 10));
			
			final JLabel useClaimsFileLabel = new JLabel("Locate Duplicates Using Claims Flat File :");
			useClaimsFileLabel.setBounds(200, 70, 240, 20);
			
			final JCheckBox claimsFileCheckbox = new JCheckBox();
			claimsFileCheckbox.setEnabled(true);
			claimsFileCheckbox.setBounds(435, 70, 20, 20);

			
			
			final JLabel useDeviceReportLabel = new JLabel("Associate Device ID With Vehicle :");
			useDeviceReportLabel.setBounds(200, 95, 240, 20);
			
			final JCheckBox deviceReportCheckbox = new JCheckBox();
			deviceReportCheckbox.setEnabled(true);
			deviceReportCheckbox.setBounds(435, 95, 20, 20);
			
			if(checkBoxOption.equals("0"))
				claimsFileCheckbox.setSelected(false);
			else
				claimsFileCheckbox.setSelected(true);
			
			if(deviceCheckboxOption.equals("0"))
				deviceReportCheckbox.setSelected(false);
			else
				deviceReportCheckbox.setSelected(true);
			
			
			
			String[] comboBoxOptions = {"Tesco FNOL Default", "Custom FNOL Run"};
			final JComboBox<String> comboBox = new JComboBox<String>(comboBoxOptions);
			comboBox.setSelectedIndex(1);
			comboBox.setBounds(200, 20, 150, 30);
			final JButton customiseRows = new JButton("Rows");
			customiseRows.setBounds(360, 20, 95, 30);
			if(comboOption.equals("0"))
				comboBox.setSelectedItem(comboBox.getItemAt(0));
			else
				comboBox.setSelectedItem(comboBox.getItemAt(1));
			
			final JTextArea rowOptions = new JTextArea();
			rowOptions.setEditable(false);
			rowOptions.setVisible(true);
			JScrollPane scrollPane = new JScrollPane(rowOptions);
			scrollPane.setBounds(470,20, 205, 100);
			
			
			String[] items = rowOptionsString.split(Pattern.quote("$$$$"));
			final List<String> options = Arrays.asList(items);
			updateRowTextbox(rowOptions, options);
			
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
			panel.add(useClaimsFileLabel);
			panel.add(claimsFileCheckbox);
			panel.add(claimsFileCopyTextField);
			panel.add(claimsFileCopyBrowse);
			panel.add(claimsFileCopyLabel);
			panel.add(scrollPane);
			panel.add(useDeviceReportLabel);
			panel.add(deviceReportCheckbox);
			
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("Configuration Settings");
			JMenuItem restoreDefault = new JMenuItem(new AbstractAction("Restore Default Configurations")
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) 
				{
					List<String> values = new ArrayList<String>();
					String comboOption = "0";
					String fnolLocation = f+"\\emailFolder";
					String resultingFolder = f+"\\resultingFolder";
					String deviceReportFolder = f+"\\deviceReportFolder";
					String claimsFolder = f+"\\claimsFlatFileFolder";
					String fnolRows = "";
					String claimsCheckbox = "1";
					String deviceCheckbox = "1";
					
					comboBox.setSelectedItem(comboBox.getItemAt(0));
					fnolLocationTextField.setText(fnolLocation);
					resultingFileTextField.setText(resultingFolder);
					deviceReportTextField.setText(deviceReportFolder);
					claimsFileCopyTextField.setText(claimsFolder);
					claimsFileCheckbox.setSelected(true);
					claimsFileCopyBrowse.setEnabled(true);
					
					values.add(comboOption);
					values.add(fnolLocation);
					values.add(resultingFolder);
					values.add(deviceReportFolder);
					values.add(claimsFolder);
					values.add(fnolRows);
					values.add(claimsCheckbox);
					values.add(deviceCheckbox);
					
					startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
							claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					
					replaceConfigSettings(configFile, values);
				}
			});
			
			JMenuItem saveConfig = new JMenuItem(new AbstractAction("Save Current Configuration")
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) 
				{
					List<String> values = new ArrayList<String>();
					String comboBoxSetting;
					String claimsCheckbox;
					String deviceCheckbox;
					String rows = "";
					
					if(comboBox.getSelectedItem().toString().equals("Tesco FNOL Default"))
						comboBoxSetting = "0";
					else
						comboBoxSetting = "1";
					
					if(claimsFileCheckbox.isSelected())
						claimsCheckbox = "1";
					else
						claimsCheckbox = "0";
					
					if(deviceReportCheckbox.isSelected())
						deviceCheckbox = "1";
					else
						deviceCheckbox = "0";
					
					String newFnolLocation = fnolLocationTextField.getText();
					String newResultingFileLocation = resultingFileTextField.getText();
					String newDeviceReportLocation = deviceReportTextField.getText();
					String newClaimsFileLocation = claimsFileCopyTextField.getText();
					
					String selectedRows = rowOptions.getText();
					if(!(selectedRows.equals("")))
					{
					}
					
					
					//TODO
					values.add(comboBoxSetting);
					values.add(newFnolLocation);
					values.add(newResultingFileLocation);
					values.add(newDeviceReportLocation);
					values.add(newClaimsFileLocation);
					values.add("");
					values.add(claimsCheckbox);
					values.add(deviceCheckbox);
					
					
					replaceConfigSettings(configFile, values);
				}
			});
			menu.add(restoreDefault);
			menu.add(saveConfig);
			menuBar.add(menu);
			frame.setJMenuBar(menuBar);
			
			frame.setSize(700, 560);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
			frame.setResizable(false);
			
			fnolLocationTextField.setText(defaultFnolLocation);
			resultingFileTextField.setText(defaultResultingFolderLocation);
			deviceReportTextField.setText(defaultDeviceReportLocation);
			claimsFileCopyTextField.setText(defaultClaimsFileLocation);
			
			customiseRows.setEnabled(enableRowsButton(comboOption));
			//TODO
			
			if(comboBox.getSelectedItem().toString().equals("Custom FNOL Run"))
			{
				deviceReportCheckbox.setEnabled(true);
				updateRowTextbox(rowOptions, options);
				rowOptions.setEnabled(true);
				claimsFileCheckbox.setEnabled(false);
				claimsFileCheckbox.setSelected(false);
				claimsFileCopyTextField.setText("");
				claimsFileCopyTextField.setEnabled(false);
				claimsFileCopyBrowse.setEnabled(false);
				
			}
			else
			{
				//TODO
				rowOptions.setText(" - Store telephone number\n - Collision time\n - Collision date\n - Collision causation code\n - sopp+sopp Reference number ");
				deviceReportCheckbox.setSelected(true);
				deviceReportCheckbox.setEnabled(false);
				claimsFileCheckbox.setEnabled(true);
				claimsFileCopyTextField.setText(defaultClaimsFileLocation);
				claimsFileCopyTextField.setEnabled(true);
				claimsFileCopyBrowse.setEnabled(true);
				if(claimsFileCheckbox.isSelected())
					claimsFileCopyBrowse.setEnabled(true);
				else
					claimsFileCopyBrowse.setEnabled(false);
			}
			
			if(deviceReportCheckbox.isSelected())
			{
				deviceReportBrowse.setEnabled(true);
				deviceReportTextField.setText(defaultDeviceReportLocation);
			}
			else
			{
				deviceReportBrowse.setEnabled(false);
				deviceReportTextField.setText("");
			}
			
			//TODO
			deviceReportCheckbox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(deviceReportCheckbox.isSelected())
					{
						deviceReportBrowse.setEnabled(true);
						deviceReportTextField.setText(defaultDeviceReportLocation);
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
					else
					{
						deviceReportBrowse.setEnabled(false);
						deviceReportTextField.setText("");
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
				}
			});
			
			startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, claimsFileCopyTextField,
					claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
				
			
			customiseRows.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.setAlwaysOnTop(false);
					new FnolTable(startButton, fnolLocationTextField, resultingFileTextField, deviceReportTextField, claimsFileCopyTextField,
							claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions);
				}
			});
			
			comboBox.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(comboBox.getSelectedItem().toString().equals("Custom FNOL Run"))
					{
						deviceReportCheckbox.setEnabled(true);
						updateRowTextbox(rowOptions, options);
						rowOptions.setEnabled(true);
						customiseRows.setEnabled(true);
						claimsFileCheckbox.setSelected(false);
						claimsFileCheckbox.setEnabled(false);
						claimsFileCopyBrowse.setEnabled(false);
						claimsFileCopyTextField.setText("");
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, claimsFileCopyTextField,
								claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
					else
					{
						rowOptions.setText(" - Store telephone number\n - Collision time\n - Collision date\n - Collision causation code\n - sopp+sopp Reference number ");
						deviceReportCheckbox.setSelected(true);
						deviceReportCheckbox.setEnabled(false);
						customiseRows.setEnabled(false);
						claimsFileCheckbox.setEnabled(true);
						if(claimsFileCheckbox.isSelected())
						{
							claimsFileCopyBrowse.setEnabled(true);
							claimsFileCopyTextField.setEnabled(true);
							claimsFileCopyTextField.setEditable(false);
						}
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, claimsFileCopyTextField,
								claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
							
					}
				}
			});
			
			claimsFileCheckbox.addActionListener(new ActionListener() 
			{
				
				public void actionPerformed(ActionEvent e) 
				{
					if(claimsFileCheckbox.isSelected())
					{
						claimsFileCopyBrowse.setEnabled(true);
						claimsFileCopyTextField.setText(defaultClaimsFileLocation);
						claimsFileCopyTextField.setEnabled(true);
						claimsFileCopyTextField.setEditable(false);
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
					else
					{
						claimsFileCopyBrowse.setEnabled(false);
						claimsFileCopyTextField.setText("");
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
						
				}
			});
			
			fnolBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					String selectedFile = fnolLocationTextField.getText();
					if(selectedFile.equals(""))
						selectedFile = defaultFnolLocation;
					
					frame.setAlwaysOnTop(false);
					JFileChooser fileChooser = new JFileChooser(selectedFile);
					
					fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					FileNameExtensionFilter filter = new FileNameExtensionFilter(".msg", "(*.msg)","msg");
					fileChooser.setFileFilter(filter);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						String selectedPath = fileChooser.getSelectedFile().toString();
						if(selectedPath.endsWith(".msg"))
							selectedPath = selectedPath.substring(0 ,selectedPath.lastIndexOf("\\"));
						fnolLocationTextField.setText(selectedPath);
						fnolLocationSaved = selectedPath;
						defaultFnolLocation = selectedPath;
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
				}
			});
			
			resultingFileBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.setAlwaysOnTop(false);
					
					String selectedFile = resultingFileTextField.getText();
					if(selectedFile.equals(""))
						selectedFile = defaultResultingFolderLocation;
					
					JFileChooser fileChooser = new JFileChooser(selectedFile);
					
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						resultingFileTextField.setText(fileChooser.getSelectedFile().toString());
						resultingFolderSaved = fileChooser.getSelectedFile().toString();
						defaultResultingFolderLocation = fileChooser.getSelectedFile().toString();
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
				}
			});
			
			deviceReportBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.setAlwaysOnTop(false);
					
					String selectedFile = deviceReportTextField.getText();
					if(selectedFile.equals(""))
						selectedFile = defaultDeviceReportLocation;
					
					
					JFileChooser fileChooser = new JFileChooser(selectedFile);
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
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField,
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
				}
			});
			
			claimsFileCopyBrowse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					frame.setAlwaysOnTop(false);
					if(defaultClaimsFileLocation.equals(""))
						defaultClaimsFileLocation = f+"\\claimsFlatFileFolder";
					JFileChooser fileChooser = new JFileChooser(defaultClaimsFileLocation);
					FileNameExtensionFilter filter = new FileNameExtensionFilter(".xlsx", "(*.xlsx)","xlsx");
					fileChooser.setFileFilter(filter);
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					
					int rVal = fileChooser.showOpenDialog(null);
					if(rVal == JFileChooser.APPROVE_OPTION)
					{
						String selectedPath = fileChooser.getSelectedFile().getAbsolutePath().toString();
						claimsFileCopyTextField.setText(selectedPath);
						claimsFileSaved = selectedPath;
						defaultClaimsFileLocation = selectedPath;
						
						startButton.setEnabled(enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
								claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, rowOptions));
					}
				}
			});
			
			startButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					File copyFile = null;
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
					File[] fnolFiles = CollectFnolData.extractFnolsFromEmails(fnolLocation, pBar, progressBarText, panel);
					updateProgressBar(pBar, 1100);
					if(deviceReportSaved.equals(""))
						deviceReportSaved = deviceReportTextField.getText();
					CollectFnolData.convertDeviceReportToXlsx(deviceReportSaved, pBar, progressBarText, panel);
					updateProgressBar(pBar, 2100);
					CollectFnolData.createResultingFile(rfLocation, pBar, progressBarText, panel);
					updateProgressBar(pBar, 3000);
					if(claimsFileCheckbox.isSelected())
						copyFile = new File(claimsFileCopyTextField.getText());
					CollectFnolData.copyFnolDataIntoSpreadsheet(fnolFiles, deviceReportSaved, pBar, progressBarText, panel, copyFile);
					updateProgressBar(pBar, 6200);
					updateProgressText("Delete Converted Device Report", progressBarText, panel);
					CollectFnolData.deleteDeviceReportXlsx(deviceReportTextField.getText());
					updateProgressText("Converted Device Report Deleted", progressBarText, panel);
					updateProgressBar(pBar, 6400);
					updateProgressText("Renaming Resulting File", progressBarText, panel);
					CollectFnolData.renameResultingFile(rfLocation);
					CollectFnolData.deleteTempFnolContents(f+"/appFiles/tempFnols");
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
	
	public static boolean enableStart(JTextField fnol, JTextField resultingFile, JTextField deviceReport, JTextField claimsFile, 
			JCheckBox claimsBox,JLabel progressBarText, JComboBox<String> comboBox, JCheckBox deviceBox, JTextArea rowChoices)
	{
		boolean enableStartButton = false;
		
		String fnolPath = fnol.getText();
		
		File fnolDirectory = new File(fnolPath);
		
		if(comboBox.getSelectedItem().toString().equals("Tesco FNOL Default"))
		{
			if(claimsBox.isSelected())
			{
				if(deviceBox.isSelected())
					if(fnolDirectory.isDirectory())
					{
						if(!(fnolDirectory.list().length == 0))
						{
							if(!(resultingFile.getText().isEmpty()))
							{
								if(!(deviceReport.getText().isEmpty()))
								{
									if(deviceReport.getText().endsWith(".csv"))
									{
										if(!(claimsFile.getText().isEmpty()))
										{
											if(claimsFile.getText().endsWith(".xlsx"))
											{
												progressBarText.setText("All Options Valid, Test May Now Begin");
												enableStartButton = true;
											}
											else
												progressBarText.setText("Claims Flat File Not Correct Extension, Must Be '.XLSX'.");
										}
										else
											progressBarText.setText("No Claims Flat File Selected. Test Cannot Start.");
									}
									else
										progressBarText.setText("Device Report Selected Not Correct File Extentsion, Must Be '.CSV'");

								}
								else
									progressBarText.setText("No Device Report Selected. Test Cannot Start");

							}
							else
								progressBarText.setText("No Location Selected For Resulting File. Test Cannot Start");

						}
						else
							progressBarText.setText("No Files Located In Directory For Emails");

					}
					else
						progressBarText.setText("FNOL Location Is Not A Directory, Please Select A Directory/Folder.");

			}
			else
			{
				if(!(deviceBox.isSelected()))
				{
					if(fnolDirectory.isDirectory())
					{
						if(!(fnolDirectory.list().length == 0))
						{
							if(!(resultingFile.getText().isEmpty()))
							{
								if(!(deviceReport.getText().isEmpty()))
								{
									if(deviceReport.getText().endsWith(".csv"))
									{
										progressBarText.setText("All Options Valid, Test May Now Begin");
										enableStartButton = true;
									}
									else
										progressBarText.setText("Device Report Selected Not Correct File Extentsion, Must Be '.CSV'");
								}
								else
									progressBarText.setText("No Device Report Selected. Test Cannot Start");
							}	
							else
								progressBarText.setText("No Location Selected For Resulting File. Test Cannot Start");
						}
						else
							progressBarText.setText("No Files Located In Directory For Emails");
					}
					else
						progressBarText.setText("FNOL Location Is Not A Directory, Please Select A Directory/Folder.");

				}
				else
				{
					if(fnolDirectory.isDirectory())
					{
						if(!(fnolDirectory.list().length == 0))
						{
							if(!(resultingFile.getText().isEmpty()))
							{
								if(deviceReport.getText().endsWith(".csv"))
								{
									progressBarText.setText("All Options Valid, Test May Now Begin");
									enableStartButton = true;
								}
								else
									progressBarText.setText("Device Report Selected Not Correct File Extentsion, Must Be '.CSV'");
							}
							else
								progressBarText.setText("No Location Selected For Resulting File. Test Cannot Start");
						}
						else
							progressBarText.setText("No Files Located In Directory For Emails");
					}
					else
						progressBarText.setText("FNOL Location Is Not A Directory, Please Select A Directory/Folder.");
				}
			}
							
			return enableStartButton;
		}
		else
		{
			if(deviceBox.isSelected())
			{
				if(fnolDirectory.isDirectory())
				{
					if(!(fnolDirectory.list().length == 0))
					{
						if(!(resultingFile.getText().isEmpty()))
						{
							if(!(deviceReport.getText().isEmpty()))
							{
								if(deviceReport.getText().endsWith(".csv"))
								{
									if(!(rowChoices.getText().equals("")))
									{
										System.out.println(rowChoices.getText());
										progressBarText.setText("All Options Valid, Test May Now Begin");
										enableStartButton = true;
									}
									else
										progressBarText.setText("No Row Options Selected For Customised Run.");
								}
								else
									progressBarText.setText("Device Report Selected Not Correct File Extentsion, Must Be '.CSV'");
							}
							else
								progressBarText.setText("No Device Report Selected. Test Cannot Start");
						}
						else
							progressBarText.setText("No Location Selected For Resulting File. Test Cannot Start");
					}
					else
						progressBarText.setText("No Files Located In Directory For Emails");
				}
				else
					progressBarText.setText("FNOL Location Is Not A Directory, Please Select A Directory/Folder.");
			}
			else
			{
				
			}
			
			return enableStartButton;
		}
		
	}
	
	public static boolean enableRowsButton(String comboOption)
	{
		if(comboOption.equals("0"))
			return false;
		else
			return true;
	}
	
	public static String getConfigDetails(File configFile, int row)
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
			String claimsFolder = "CLAIMS_FILE_FOLDER="+newValues.get(4);
			String rowSettings = "FNOL_ROWS_SELECTED="+newValues.get(5);
			String claimsCheckbox = "CLAIMS_CHECKBOX="+newValues.get(6);
			String deviceCheckbox = "DEVICE_CHECKBOX="+newValues.get(7);
			PrintWriter writer = new PrintWriter(file);
			writer.println(comboBox);
			writer.println(fnol);
			writer.println(resultingFolder);
			writer.println(deviceReport);
			writer.println(claimsFolder);
			writer.println(rowSettings);
			writer.println(claimsCheckbox);
			writer.println(deviceCheckbox);
			writer.close();
		} 
		catch (Exception e) 
		{
		}
	}
	
	private static void updateRowTextbox(JTextArea rowList, List<String> options) 
	{
		rowList.setText("");
		//TODO
		if(!(options.get(0).equals("")))
		{
			for(String row : options)
			{
				rowList.append(" - "+row+"\n");
			}
		}
	}
}	
