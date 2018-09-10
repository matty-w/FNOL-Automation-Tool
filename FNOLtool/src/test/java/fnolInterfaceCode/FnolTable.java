package fnolInterfaceCode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Cell;

public class FnolTable 
{
	public FnolTable(final JButton startButton, final JTextField fnolLocationTextField, final JTextField resultingFileTextField, 
			final JTextField deviceReportTextField, final JTextField claimsFileCopyTextField, final JCheckBox claimsFileCheckbox, 
			final JLabel progressBarText, final JComboBox<String> comboBox, final JCheckBox deviceReportCheckbox,final JTextArea area)
	{
		final JFrame fnolFrame = new JFrame("Select FNOL Rows");
		Object columnNames[] = {"FNOL Row Title", "Example Data", ""};
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		final Object test[][] = getDefaultFnol();	
		
		//List<String> duplicateValues = composeDuplicateList(test);
		
		determineWhichCheckboxesSelected(test);
		
		
		
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		final JTable rowTable = new JTable(test, columnNames);
		
		DefaultTableModel model = new DefaultTableModel(test, columnNames)
		{
			private static final long serialVersionUID = 1L;

			@Override
            public Class<?> getColumnClass(int column) 
	        {
                switch (column) 
                {
                    case 0:
                    {
                    	return String.class;
                    }
                    case 1:
                    {
                    	return String.class;
                    }
                    case 2:
                    {
                    	return Boolean.class;
                    }
                }
				return null;
            }
		};
		
		
		rowTable.setModel(model);
		rowTable.setDefaultEditor(Object.class, null);
		JButton exit = new JButton("Exit");
		JButton save = new JButton("Confirm");
		JButton clear = new JButton("Clear");
		JButton saveOptions = new JButton("Save Options");
		JScrollPane scrollPane = new JScrollPane(rowTable);
		
		rowTable.setRowSelectionInterval(0, 0);
		rowTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		rowTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
		rowTable.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer());
		
		rowTable.getTableHeader().setReorderingAllowed(false);
		rowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		rowTable.getColumnModel().getColumn(0).setPreferredWidth(400);
		rowTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		rowTable.getColumnModel().getColumn(2).setPreferredWidth(40);
		rowTable.getColumnModel().getColumn(0).setResizable(false);
		rowTable.getColumnModel().getColumn(1).setResizable(false);
		rowTable.getColumnModel().getColumn(2).setResizable(false);
		rowTable.setColumnSelectionAllowed(false);
		
		
		panel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(panel);
		mainPanel.add(buttonPanel);
		buttonPanel.add(exit);
		buttonPanel.add(clear);
		buttonPanel.add(saveOptions);
		buttonPanel.add(save);
		buttonPanel.setPreferredSize(new Dimension(650,50));
		panel.setPreferredSize(new Dimension(650, 850));
		fnolFrame.setContentPane(mainPanel);
		fnolFrame.pack();
		fnolFrame.setVisible(true);
		fnolFrame.setLocationRelativeTo(null);
		fnolFrame.setResizable(false);
		fnolFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				fnolFrame.dispose();
			}
		});
		
		clear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				int totalRows = rowTable.getRowCount();
				for(int i = 0; i < totalRows; i++)
				{
					System.out.println(i);
					Boolean isChecked = Boolean.valueOf(rowTable.getValueAt(i, 2).toString());
					if(isChecked.equals(true))
						rowTable.setValueAt(false, i, 2);
				}
				startButton.setEnabled(GuiCode.enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
						claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, area));
			}
		});
		
		saveOptions.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				String totalSelectedLong = "";
				String finalCut = "";
				final String f = new File("").getAbsolutePath();
				
				int totalRows = rowTable.getRowCount();
				
				for(int i = 0; i < totalRows; i++)
				{
					String value = rowTable.getValueAt(i, 0).toString();
					Boolean isChecked = Boolean.valueOf(rowTable.getValueAt(i, 2).toString());
					if(isChecked.equals(true))
					{
						int rowNum = i;
						totalSelectedLong = totalSelectedLong+value+"||"+rowNum+"$$$$";
					}
				}
				
				if(totalSelectedLong.equals(""))
					finalCut = "";
				else
					finalCut = totalSelectedLong.substring(0, totalSelectedLong.length()-4);
				
				Path path = Paths.get(f+"\\appFiles\\configFolder\\savedConfig.txt");
				updateConfigForRows(path, finalCut);
				String[] items = finalCut.split(Pattern.quote("$$$$"));
				List<String> options = Arrays.asList(items);
				updateRowTextbox(area, options);
			}
		});
		
		save.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				String totalSelectedLong = "";
				String finalCut = "";
				final String f = new File("").getAbsolutePath();
				
				int totalRows = rowTable.getRowCount();
				
				for(int i = 0; i < totalRows; i++)
				{
					String value = rowTable.getValueAt(i, 0).toString();
					Boolean isChecked = Boolean.valueOf(rowTable.getValueAt(i, 2).toString());
					if(isChecked.equals(true))
					{
						totalSelectedLong = totalSelectedLong+value+"||"+i+"$$$$";
					}
				}
				
				if(totalSelectedLong.equals(""))
					finalCut = "";
				else
					finalCut = totalSelectedLong.substring(0, totalSelectedLong.length()-4);
				
				Path path = Paths.get(f+"\\appFiles\\configFolder\\savedConfig.txt");
				updateConfigForRows(path, finalCut);
				String[] items = finalCut.split(Pattern.quote("$$$$"));
				List<String> options = Arrays.asList(items);
				updateRowTextbox(area, options);
				fnolFrame.dispose();
				//TODO
				startButton.setEnabled(GuiCode.enableStart(fnolLocationTextField, resultingFileTextField, deviceReportTextField, 
						claimsFileCopyTextField, claimsFileCheckbox, progressBarText, comboBox, deviceReportCheckbox, area));
			}
		});
	}
	
	private void determineWhichCheckboxesSelected(Object[][] object)
	{
		final String f = new File("").getAbsolutePath();
		final File configFile = new File(f+"\\appFiles\\configFolder\\savedConfig.txt");
		String checkedOptions = GuiCode.getConfigDetails(configFile, 5);
		String[] items = checkedOptions.split(Pattern.quote("$$$$"));
		
		if(!(checkedOptions.equals("")))
		{
			int objectLength = object.length;
			
			for(int i = 0; i < objectLength; i++)
			{
				String title = (String) object[i][0];
				
				//TODO
				for(String item : items)
				{
					String[] options = item.split(Pattern.quote("||"));
					String savedTitle = options[0];
					String line = options[1];
					int lineNum = Integer.parseInt(line);
					
					if(savedTitle.equals(title))
					{
						if(lineNum == i)
						{
							object[i][2] = true;
						}
					}
				}
			}
		}
	}
	
	private void updateRowTextbox(JTextArea rowList, List<String> options) 
	{
		rowList.setText("");
		//TODO
		for(String row : options)
		{
			rowList.append(" - "+row+"\n");
		}
	}
	
	
	
	private Object[][] getDefaultFnol()
	{
		//TODO
		String f = new File("").getAbsolutePath();
		String path = f+"\\appFiles\\defaultFnol\\defaultFnol.xls";
		File defaultFnol = new File(path);
		
		try 
		{
			List<Object[]> objectList = new ArrayList<Object[]>();
			Object[] title = {"Tesco DotCom Incident Report Form","",null};
			objectList.add(title);
			FileInputStream fis = new FileInputStream(defaultFnol);
			HSSFWorkbook fnolWorkbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = fnolWorkbook.getSheetAt(0);
			int rows = 102;
			System.out.println(rows);
			System.out.println(sheet.getLastRowNum());
			
			for(int i = 0; i < rows; i++)
			{
				Row row = sheet.getRow(i);
				if(row.getRowNum() != 1 && row.getRowNum() != 2)
				{
					Cell rowTitle = row.getCell(0);
					Cell exampleData = row.getCell(1);
					String rowTitleString;
					String exampleDataString = null;
					
					rowTitle.setCellType(Cell.CELL_TYPE_STRING);
					exampleData.setCellType(Cell.CELL_TYPE_STRING);
					rowTitleString = rowTitle.getStringCellValue();
					exampleDataString = exampleData.getStringCellValue();
					Object[] tableRow = {rowTitleString, exampleDataString, false}; 
					objectList.add(tableRow);
				}
			}
			
			Object objects[][] = new Object[objectList.size()][3];
			objects = objectList.toArray(objects);
			return objects;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/*private List<String> composeDuplicateList(Object[][] object)
	{
		Set<String> setToReturn = new HashSet<String>();
		Set<String> set1 = new HashSet<String>();
		int length = object.length;
		List<String> duplicateList = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		for(int i = 0; i < length; i++)
		{
			String s = (String) object[i][0];
			titles.add(s);
		}
		
		for(String stringValue : titles)
		{
			if(!(set1.add(stringValue)))
			{
				setToReturn.add(stringValue);
			}
		}
		
		duplicateList.addAll(setToReturn);
		return duplicateList;
	}
	
	private boolean checkIfChecked(String rowTitleString)
	{
		boolean isSelected = false;
		final String f = new File("").getAbsolutePath();
		final File configFile = new File(f+"\\appFiles\\configFolder\\savedConfig.txt");
		String selectedValuesList = GuiCode.getConfigDetails(configFile, 5);
		if(selectedValuesList.equals(""))
			return false;
		
		
		//TODO
		String[] items = selectedValuesList.split(Pattern.quote("$$$$"));
		final List<String> selectedValues = Arrays.asList(items);
		
		for(String value : selectedValues)
			if(rowTitleString.equals(value))
				isSelected = true;
		
		return isSelected;
	}
	

	
	private List<String> selectedRowData(JTable table)
	{
		int rows = table.getRowCount();
		List<String> optionsSelect = new ArrayList<String>();
		
		for(int i = 0; i < rows; i++)
		{
			Boolean isChecked = Boolean.valueOf(table.getValueAt(i, 2).toString());
			
			if(isChecked)
			{
				String option = table.getValueAt(i, 0).toString();
				optionsSelect.add(option);
			}
		}
		return optionsSelect;
	}*/
	
	private void updateConfigForRows(Path file, String valueLong)
	{
		try 
		{
			List<String> fileContent = new ArrayList<String>(Files.readAllLines(file, StandardCharsets.UTF_8));
			
			for (int i = 0; i < fileContent.size(); i++) {
			    if (fileContent.get(i).contains("FNOL_ROWS_SELECTED=")) 
			    {
			        fileContent.set(i, "FNOL_ROWS_SELECTED="+valueLong);
			        break;
			    }
			}
			Files.write(file, fileContent, StandardCharsets.UTF_8);
		} 
		catch (Exception e) 
		{
		}
	}

	static class CustomRenderer extends DefaultTableCellRenderer 
	{
		private static final long serialVersionUID = 1L;
		

		public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
	        if(row == 0 ||row == 8 || row == 27 || row == 55 || row == 76 || row == 86 || row == 93)
	        {
	        	cellComponent.setBackground(Color.BLUE);
	        	cellComponent.setForeground(Color.WHITE);
	        	cellComponent.setEnabled(false);
	        	table.setValueAt(false, row, 2);
	        } 
	        else
	        {
	        	if(isSelected)
	        	{
	        		table.changeSelection(row, column, true, true);
	        		if(hasFocus)
	        		{
			        	cellComponent.setEnabled(true);
			        	Boolean isChecked = Boolean.valueOf(table.getValueAt(row, 2).toString());
			        	if(isChecked.equals(false))
			        		table.setValueAt(true, row, 2);
			        	else
			        		table.setValueAt(false, row, 2);
	        		}
	        	}
	        	else
	        	{
		        	cellComponent.setBackground(Color.WHITE);
		        	cellComponent.setForeground(Color.BLACK);
		        	cellComponent.setEnabled(true);
	        	}
	        }
	        return cellComponent;
	    }
	}
}
