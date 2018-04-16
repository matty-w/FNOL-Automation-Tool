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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Cell;

public class FnolTable 
{
	public FnolTable()
	{
		final JFrame fnolFrame = new JFrame("Select FNOL Rows");
		Object columnNames[] = {"FNOL Row Title", "Example Data", ""};
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		final Object test[][] = getDefaultFnol();
		
		final Object rowData[][] = {{"Tesco DotCom Incident Report Form","",null},{"Date Of Call", "11/01/18",false},{"Start Time Of Call","12:00:00",false},
				{"Finish Time Of Call","12:30:00",false},{"Has Anyone Sustained Any Injuries","NO",false},{"Have The Emergency Services Attended","NO",false},
				{"Is The Tesco Vehicle Drivable","YES",false},{"Is Vehicle Recovery Required", "NO",false},{"TESCO DETAILS","",false},{"Tesco Driver's Name","Mr F Name",false},
				{"Tesco Driver's Date Of Birth","01/01/1990",false},{"Tesco Driver's Employee Number","UK123456789",false},{"Has This Tesco Driver Sustained Injuries","N/A",false},
				{"If Injured Type Of Injury","N/A",false},{"Tesco Drivers Contact Number","123456789",false},{"Tesco / Agency Driver","Own",false},
				{"If Agency, Which Agency","N/A",false},{"Store Address","Some Address",false},{"Postcode","MM11MM",false},{"Store Telephone Number","123456789",false},
				{"Tesco Vehicle Registration Number","DD1X12",false},{"Vehicle Make / Model","IVECO VAN",false},{"Van Number","00001",false},{"Is Vehicle Loaded","YES",false},
				{"Start Time Of Driver's Shift","18:00",false},{"End Time Of Driver's Shift","22:00",false},{"Tesco Business Division","Tesco Dotcom",false},
				{"THIRD PARTY DETAILS","",false},{"Driver / Property Occupant Name","S NAME",false},{"Gender Of Third Party","MALE",false},{"Occupation","N/K",false},
				{"Address","21 Some Address, Somewhere",false},{"Postcode","FF11FF",false},{"Email Address","N/K",false},{"Contact Telephone Number - Home","123456789",false},
				{"Contact Telephone Number - Mobile","987654321",false},{"Type Of Third Party","Property Owner",false},{"Has This Third Party Sustained Injuries","NO",false},
				{"If Injured Type Of Injury","N/A",false},{"Was The Third Party Driver In The Vehicle At The Time Of The Incident","N/A",false},
				{"Third Party Vehicle Registration Number","N/A",false},{"Vehicle Make / Model","N/A",false},{"Number Of People In Third Party Vehicle (Including Driver","1",false},
				{"Position Of Third Party Vehicle","N/A",false},{"Type of Third Party Property","Wall (Domestic)",false},{"Third party insurer","N/K",false},
				{"Has BUMP CARD been given to Third Party / Property Owner","YES",false},{"Did the Third Party admit Liability at the scene of the collision","NO",false},
				{"Description of damage to Third Party vehicle / Property","ENDS BRICKS LOOSE",false},{"Was there any pre-existing damage to the Third Party Vehicle","NO",false},
				{"Vehicle / Property owner name (If different to Driver / Property occupant name)","N/A",false},{"Owner Contact Number","N/A",false},{"Owner Email Address","N/A",false},
				{"Owner Address","N/A",false},{"Owner Postcode","N/A",false},{"COLLISION CIRCUMSTANCES","",false},{"Collision time","21:00",false},{"Collision date","01/01/18",false},
				{"Collision location","SOME ADDRESS, ADDRESS",false},{"Collision sub location","On The Road",false},{"Collision causation code","HIT TP WALL/BUILDING/GATE/OBJECT",false},
				{"Type of road","Single carriageway C road",false},{"Position Of Tesco Vehicle on road","TRAVELLING FORWARDS",false},{"Road Conditions","Wet",false},
				{"Weather Conditions","Dry Overcast",false},{"Description of damage to Tesco vehicle","NEARSID SIDE STEP BENT.",false},
				{"Was another Third Party involved causing damage to the Tesco vehicle","YES",false},{"If No Cause of damage ","N/A",false},
				{"Did the collision happen in daylight","NO",false},{"Did Tesco driver admit Liability at the scene of the collision","NO",false},
				{"Have you completed a sketch of the collision scene","NO",false},{"Please give a brief description of the collision circumstances ","Some description",false},
				{"In Tesco’s Opinion do you believe the third party property / vehicle is owned by Tesco.","NO",false},{"Have photographs been taken","YES",false},
				{"Was driver aware incident occurred","YES",false},{"Was the driver in the area at the time of incident","YES",false},{"WITNESS DETAILS","",false},
				{"Were there any witnesses","NO",false},{"Witness 1 name","N/A",false},{"Witness 1 address","N/A",false},{"Witness 1 postcode","N/A",false},
				{"Witness 1 telephone number","N/A",false},{"Witness 2 name","N/A",false},{"Witness 2 address","N/A",false},{"Witness 2 postcode","N/A",false},
				{"Witness 2 telephone number","N/A",false},{"POLICE DETAILS","",false},{"Did the police attend the scene","NO",false},{"Police Officer name ","N/A",false},
				{"Police Officer badge number","N/A",false},{"Police station","N/A",false},{"Police station telephone number","N/A",false},{"Police Reference","N/A",false},
				{"FURTHER INFORMATION","",false},{"sopp+sopp further comments","NONE",false},{"sopp+sopp Reference number","000000",false},{"Plexus Reference Number","N/A",false},
				{"Who took original call","sopp+sopp",false},{"Full Name and Position of person reporting incident","SOME NAME - CDD",false},{"Driver Trainer Name","N/A",false},
				{"Driver Trainer Email Address","N/A",false}};
		
		
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		final JTable rowTable = new JTable(rowData, columnNames);
		
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames)
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
			}
		});
		
		saveOptions.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		save.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				List<String> rowData = selectedRowData(rowTable);
				fnolFrame.dispose();
				
			}
		});
	}
	
	private void updateRowTextbox(JTextArea rowBox, List<String> options) 
	{
		
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
			
			for(int i = 0; i < rows; i++)
			{
				Row row = sheet.getRow(i);
				if(row.getRowNum() != 1 && row.getRowNum() != 2)
				{
					Cell rowTitle = row.getCell(0);
					Cell exampleData = row.getCell(1);
					String rowTitleString;
					String exampleDataString = null;
					
					if(rowTitle.getCellType() != Cell.CELL_TYPE_STRING)
					{
						Double cellValue = rowTitle.getNumericCellValue();
						rowTitleString = String.valueOf(cellValue);
					}
					else
					{
						rowTitleString = rowTitle.getStringCellValue();
					}
					
					if(exampleData.getCellType() != Cell.CELL_TYPE_STRING)
					{
						switch(exampleData.getCellType())
						{
							case Cell.CELL_TYPE_NUMERIC:
							{
								Double cellValue = exampleData.getNumericCellValue();
								exampleDataString = String.valueOf(cellValue);
							}
						}
					}
					else
					{
						exampleDataString = exampleData.getStringCellValue();
					}
					
					
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
