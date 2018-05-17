package fnolMethods;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fnolInterfaceCode.ErrorGui;
import fnolInterfaceCode.GuiCode;
import logging.Logger;

public class CollectFnolData 
{
	static File resultingFile = null;
	static XSSFWorkbook deviceReportSaved = null;
	static List<Object> fnolReferencesListSaved = new ArrayList<Object>();
	
	public static File[] extractFnolsFromEmails(String emailDirectory, JProgressBar bar, final JLabel progressBarText, final JPanel panel)
	{
		try
		{
			final String f = new File("").getAbsolutePath(); 
			List<String> fileNames = new ArrayList<String>();
			File emailFolder = new File(emailDirectory);
			File[] listEmailFiles = emailFolder.listFiles(new FilenameFilter() 
			{
				public boolean accept(File dir, String name) 
				{
					return name.endsWith(".msg");
				}
			});
			
			
			for(int i = 0; i < listEmailFiles.length; i++)
			{
				if(listEmailFiles[i].isFile())
				{
					String name = listEmailFiles[i].getPath();
					fileNames.add(name);
				}
			}
			
			for(int i = 0; i < fileNames.size(); i++)
			{
				String msgFileString = fileNames.get(i);
				MAPIMessage msg = new MAPIMessage(msgFileString);
				
				AttachmentChunks[] attachments = msg.getAttachmentFiles();
				if(attachments.length > 0) 
				{
		            for (AttachmentChunks a  : attachments) 
		            {
		                ByteArrayInputStream fileIn = new ByteArrayInputStream(a.attachData.getValue());
		                File msgFile = new File(f+"/appFiles/tempFnols", a.attachLongFileName.toString()); // output
		                OutputStream fileOut = null;
		                try 
		                {
		                    fileOut = new FileOutputStream(msgFile);
		                    byte[] buffer = new byte[2048];
		                    int bNum = fileIn.read(buffer);
		                    while(bNum > 0) 
		                    {
		                        fileOut.write(buffer);
		                        bNum = fileIn.read(buffer);
		                    }
		                }
		                finally 
		                {
		                    try 
		                    {
		                        if(fileIn != null) 
		                        {
		                            fileIn.close();
		                        }
		                    }
		                    finally 
		                    {
		                        if(fileOut != null) 
		                        {
		                            fileOut.close();
		                        }
		                    }
		                }
		            }
		        }
		        else
		        {
		        }
			}
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
		}
		
		final String f = new File("").getAbsolutePath(); 
		File fnolFolder = new File(f+"/appFiles/tempFnols");
		File[] listFnols = fnolFolder.listFiles(new FilenameFilter() 
		{
			public boolean accept(File dir, String name) 
			{
				if(name.contains("ref") || name.contains("Ref"))
					return name.endsWith(".xls");
				return false;
			}
		});
		
		return listFnols;
	}
	
	public static File[] getAllFnolFiles(String directory, JProgressBar bar, final JLabel progressBarText, final JPanel panel)
	{
		GuiCode.updateProgressBar(bar, 700);
		File f = new File(directory);
		GuiCode.updateProgressBar(bar, 800);
		
		File[] listFnolFiles = f.listFiles(new FilenameFilter() 
		{
			public boolean accept(File dir, String name) 
			{
				GuiCode.updateProgressText("Checking Directory For Files: "+name, progressBarText, panel);
				return name.endsWith(".xls");
			}
		});
		GuiCode.updateProgressBar(bar, 900);
		GuiCode.updateProgressText("Sorting FNOL File List", progressBarText, panel);
		Arrays.sort(listFnolFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		GuiCode.updateProgressBar(bar, 1000);
		GuiCode.updateProgressText("Returning List", progressBarText, panel);
		return listFnolFiles;
	}
	
	public static void copyFnolDataIntoSpreadsheet(File[] fnolFiles, String deviceReportLocation, JProgressBar bar, JLabel progressBarText, JPanel panel, File claimsFile)
	{
		try
		{
			GuiCode.updateProgressText("Copying FNOL data into Resulting file", progressBarText, panel);
			GuiCode.updateProgressBar(bar, 3100);
			List<Object> values = new ArrayList<Object>();
			List<String> fnolClaims = createFnolConflictList(claimsFile);
			final int fnolLength = 3000;
			int progressCurrent = 3100;
			
			
			for(int i = 0; i < fnolFiles.length; i++)
			{
				progressCurrent = progressCurrent+(fnolLength/fnolFiles.length);
				GuiCode.updateProgressBar(bar, progressCurrent);
				File fnolFile = fnolFiles[i];
				GuiCode.updateProgressText("Copy FNOL file '"+fnolFile.getName()+"' details into resulting file", progressBarText, panel);
				
				FileInputStream fis = new FileInputStream(fnolFile);
				
				HSSFWorkbook fnolWorkbook = new HSSFWorkbook(fis);
				
				HSSFSheet sheet = fnolWorkbook.getSheetAt(0);
				
				Iterator<Row> rowIterator = sheet.iterator();
				
				
				while(rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					Cell titleCell = row.getCell(0);
					String cellTitle = titleCell.getStringCellValue();
					
					if(cellTitle.equals("Store telephone number") || cellTitle.equals("Collision time")|| cellTitle.equals("Collision date")
							|| cellTitle.equals("Collision causation code")|| cellTitle.equals("sopp+sopp Reference number "))
					{
						Cell cell = row.getCell(1);
						
						if(cellTitle.equals("Store telephone number"))
						{
							if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
							{
								String noData = "No Data Provided";
								values.add(noData);
							}
							else
							{
								String reg = cell.getStringCellValue();
								String regFinal = reg.replaceAll(" ", "");
								values.add(regFinal);
							}
						}
						if(cellTitle.equals("Collision time"))
						{
							if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
							{
								String noData = "No Data Provided";
								values.add(noData);
							}
							else
							{
								switch(cell.getCellType())
								{
									case Cell.CELL_TYPE_STRING:
										SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
										String cv1 = sdf1.format(cell.getStringCellValue());
										Date time1 = sdf1.parse(cv1);
										values.add(time1);
										break;
									case Cell.CELL_TYPE_NUMERIC:
										SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
										String cv2 = sdf2.format(cell.getDateCellValue());
										Date time2 = sdf2.parse(cv2);
										values.add(time2);
										break;
								}
							}
						}
						if(cellTitle.equals("Collision date"))
						{
							if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
							{
								String noData = "No Data Provided";
								values.add(noData);
							}
							else
							{
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								String cv2 = sdf.format(cell.getDateCellValue());
								Date date = sdf.parse(cv2);
								
								values.add(date);
							}
						}
						if(cellTitle.equals("Collision causation code"))
						{
							if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
							{
								String noData = "No Data Provided";
								values.add(noData);
							}
							else
							{
								switch(cell.getCellType())
								{
									case Cell.CELL_TYPE_STRING:
										String causationCode = "";
										causationCode = cell.getStringCellValue();
										values.add(causationCode);
										break;
									case Cell.CELL_TYPE_NUMERIC:
										double cc = 0;
										cc = cell.getNumericCellValue();
										values.add(cc);
										break;
								}
							}
						}
						if(cellTitle.equals("sopp+sopp Reference number "))
						{
							if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
							{
								String noData = "No Data Provided";
								values.add(noData);
							}
							else
							{
								switch(cell.getCellType())
								{
									case Cell.CELL_TYPE_STRING:
										String ss = cell.getStringCellValue();
										values.add(ss);
										break;
									case Cell.CELL_TYPE_NUMERIC:
										double ssn = cell.getNumericCellValue();
										values.add(ssn);
										break;
								}
							}
						}
							
						if(values.size() == 1)
						{
							String deviceID = calculateDeviceId(cell, deviceReportLocation);
							values.add(deviceID);
						}
						if(values.size() == 2)
						{
							String blankSpace = "";
							values.add(blankSpace);
						}
						if(values.size() == 5)
						{
							String blankSpace = "";
							values.add(blankSpace);
						}
						if(values.size() == 8)
						{
							String fnolFileName = fnolFile.getName();
							values.add(fnolFileName);
						}
					}
				}
				pasteCellsIntoFile(values, fnolClaims);
				values.clear();
			}
		}
		catch(Exception e)
		{
		}

	}
	
	private static void pasteCellsIntoFile(List<Object> cellValues, List<String> refListConflicts)
	{
		Collections.swap(cellValues, 3, 4);
		Collections.swap(cellValues, 6, 7);
		try
		{
			FileInputStream fis = new FileInputStream(resultingFile);
			XSSFWorkbook fnolWorkbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = fnolWorkbook.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();
			Row row = sheet.createRow(lastRow+1);
			
			XSSFCellStyle cellStyleFail = createWorksheetCellStyle(fnolWorkbook, 197, 182, 159);
			
			
			
			boolean isConflict = checkIfConflictExists(cellValues.get(6), refListConflicts);
			
			for(int i = 0; i < cellValues.size(); i++)
			{
				Object cellValue = cellValues.get(i);
				
				if(i == 7)
				{
					Cell cell = row.createCell(i);
					String fileName = cellValue.toString();
					cell.setCellValue(fileName);
					if(isConflict == true)
						cell.setCellStyle(cellStyleFail);
				}
				
				if(cellValue instanceof String)
				{
					Cell cell = row.createCell(i);
					if(cell.getColumnIndex() == 1)
					{
						String v = cellValue.toString();
						if(v.equals("") || v.equals(null) || v.equals("No VRN Provided"))
						{
							cell.setCellValue(v);
							if(isConflict == true)
							  cell.setCellStyle(cellStyleFail);
						}
						else
						{
							int num = Integer.parseInt(v);
							cell.setCellValue(num);
							if(isConflict == true)
								cell.setCellStyle(cellStyleFail);
						}
					}
					else
					{
						String v = cellValue.toString();
						cell.setCellValue(v);
						if(isConflict == true)
							cell.setCellStyle(cellStyleFail);
					}
				}
				if(cellValue instanceof Double)
				{
					int v = ((Double) cellValue).intValue();
					Cell cell = row.createCell(i);
					cell.setCellValue(v);
					if(isConflict == true)
						cell.setCellStyle(cellStyleFail);
				}
				if(cellValue instanceof Date)
				{
					String dateString  = cellValue.toString();
					if(dateString.contains("00:00:00"))
					{
						Date dateValue = (Date) cellValue;
						Cell cell = row.createCell(i);
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						SimpleDateFormat writeFormat = new SimpleDateFormat("dd/MM/yyyy");
						String reportDate = writeFormat.format(dateValue);
						cell.setCellValue(reportDate);
						XSSFCreationHelper createHelper = fnolWorkbook.getCreationHelper();
						XSSFCellStyle cellStyle = fnolWorkbook.createCellStyle();
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
						cell.setCellStyle(cellStyle);
						if(isConflict == true)
							cell.setCellStyle(cellStyleFail);
					}
					else
					{
						Date dateValue = (Date) cellValue;
						Cell cell = row.createCell(i);
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						SimpleDateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
						String reportTime = writeFormat.format(dateValue);
						cell.setCellValue(reportTime);
						XSSFCreationHelper createHelper = fnolWorkbook.getCreationHelper();
						XSSFCellStyle cellStyle = fnolWorkbook.createCellStyle();
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("HH:mm:ss"));
						cell.setCellStyle(cellStyle);
						if(isConflict == true)
							cell.setCellStyle(cellStyleFail);
					}
					
					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
					sheet.autoSizeColumn(7);
					sheet.autoSizeColumn(8);
				}
			}
			FileOutputStream outputStream = new FileOutputStream(resultingFile);
			fnolWorkbook.write(outputStream);
	        outputStream.flush();
	        outputStream.close();
	        fis.close();
		}
		catch(Exception e)
		{
		}
	}
	
	public static void createResultingFile(String directoryLocation, JProgressBar bar, JLabel progressBarText, JPanel panel)
	{
		try
		{
			GuiCode.updateProgressText("Create Resulting File", progressBarText, panel);
			GuiCode.updateProgressBar(bar, 2200);
			String fileLocation = directoryLocation+"\\test.xlsx";
			
			File f = new File(fileLocation);
			GuiCode.updateProgressBar(bar, 2300);
			
			if(f.exists())
			{
				GuiCode.updateProgressBar(bar, 2900);
				resultingFile = f;
				return;
			}
			else
			{
				GuiCode.updateProgressBar(bar, 2350);
				XSSFWorkbook resultingSpreadsheet = new XSSFWorkbook();
				XSSFSheet sheet = resultingSpreadsheet.createSheet("Results");
				GuiCode.updateProgressBar(bar, 2400);
				
				Row titleRow = sheet.createRow(0);
				Cell cell1 = titleRow.createCell(0);
				Cell cell2 = titleRow.createCell(1);
				Cell cell3 = titleRow.createCell(2);
				Cell cell4 = titleRow.createCell(3);
				Cell cell5 = titleRow.createCell(4);
				Cell cell6 = titleRow.createCell(5);
				Cell cell7 = titleRow.createCell(6);
				Cell cell8 = titleRow.createCell(7);
				Cell cell9 = titleRow.createCell(8);
				GuiCode.updateProgressBar(bar, 2500);
				
				cell1.setCellValue("VRN");
				cell2.setCellValue("Device ID");
				cell3.setCellValue("");
				cell4.setCellValue("Collision Date");
				cell5.setCellValue("Collision Time");
				cell6.setCellValue("");
				cell7.setCellValue("Sopp + Sopp Reference");
				cell8.setCellValue("Collision Causation Code");
				cell9.setCellValue("FNOL File Name");
				GuiCode.updateProgressBar(bar, 2600);
				
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				sheet.autoSizeColumn(6);
				sheet.autoSizeColumn(7);
				sheet.autoSizeColumn(8);
				GuiCode.updateProgressBar(bar, 2700);
				
				FileOutputStream outputStream = new FileOutputStream(fileLocation);
				resultingSpreadsheet.write(outputStream);
		        outputStream.flush();
		        outputStream.close();
		        GuiCode.updateProgressBar(bar, 2900);
				
			}
			GuiCode.updateProgressText("Resulting File Created", progressBarText, panel);
			resultingFile = f;
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
	
	private static String calculateDeviceId(Cell regCell, String deviceReportLocation)
	{
		try
		{
			if(regCell == null || regCell.getCellType() == Cell.CELL_TYPE_BLANK)
			{
				String noData = "No VRN Provided";
				return noData;
			}
			String carReg = regCell.getStringCellValue();
			String carRegFixed = carReg.replaceAll(" ", "");
			XSSFWorkbook deviceReportWorkbook = deviceReportSaved;
			XSSFSheet sheet = deviceReportWorkbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				Cell cell = row.getCell(3);
				if(!(cell == null))
				{
					String regBefore = cell.getStringCellValue();
					String regFixed = regBefore.replaceAll(" ", "");
					if(carRegFixed.equals(regFixed))
					{
						Cell cell2 = row.getCell(0);
						String deviceIdString = cell2.getStringCellValue();
						return deviceIdString;
					}
				}
			}
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
		}
		
		return "";
	}
	
	public static void convertDeviceReportToXlsx(String deviceReport, JProgressBar bar, JLabel progressBarText, JPanel panel) 
	{
		try
		{
			GuiCode.updateProgressText("Converting File '"+deviceReport+"' To xlsx File", progressBarText, panel);
			GuiCode.updateProgressBar(bar, 1200);
			File f = new File(deviceReport);
			String parent = f.getParent();
			String fileName = f.getName();
			int end = fileName.indexOf(".");
			String fileNameCut = fileName.substring(0, end);
			GuiCode.updateProgressBar(bar, 1400);
			FileInputStream fis = new FileInputStream(deviceReport);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
	        XSSFWorkbook deviceReportXlsx = new XSSFWorkbook();
	        GuiCode.updateProgressBar(bar, 1600);
	        FileOutputStream writer = new FileOutputStream(new File(parent+"\\"+fileNameCut+".xlsx") );
	        XSSFSheet mySheet = deviceReportXlsx.createSheet();
	        String line= "";
	        int rowNo=0;
	        GuiCode.updateProgressBar(bar, 1700);
	        int lines = 0;
	        
	        while ( (line=reader.readLine()) != null )
	        {
	        	lines++;
	        	GuiCode.updateProgressText("Copying Line "+lines+" From '"+deviceReport+"' into new File", progressBarText, panel);
	            String[] columns = line.split(",");
	            XSSFRow myRow =mySheet.createRow(rowNo);
	            for (int i=0;i<columns.length;i++)
	            {
	                XSSFCell myCell = myRow.createCell(i);
	                myCell.setCellValue(columns[i]);
	            }
	             rowNo++;
	        }
	        GuiCode.updateProgressBar(bar, 1900);
	        deviceReportXlsx.write(writer);
	        reader.close();
	        writer.close();
	        GuiCode.updateProgressText("Close File Writer", progressBarText, panel);
	        GuiCode.updateProgressBar(bar, 2000);
	        deviceReportSaved = deviceReportXlsx;
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
	
	public static void deleteDeviceReportXlsx(String deviceReport)
	{
		File f = new File(deviceReport);
		String name = f.getName();
		int end = name.indexOf(".");
		String nameCut = name.substring(0, end);
		String path = f.getParent();
		File d = new File(path);
		
		for(File file: d.listFiles())
		{
			if(!file.isDirectory())
				if(file.getName().equals(nameCut+".xlsx"))
					file.delete();
		}
	}
	
	public static List<String> createFnolReferenceList(String resultingFileLocation)
	{
		try
		{
			List<String> fnolReferences = new ArrayList<String>();
			String resultingFileString = resultingFileLocation+"\\test.xlsx";
			File f = new File(resultingFileString);
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook fnolWorkbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = fnolWorkbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				Cell cell = row.getCell(6);
				if(row.getRowNum() >= 1)
				{
					Double d = cell.getNumericCellValue();
					int i = d.intValue();
					String s = String.valueOf(i);
					fnolReferences.add(s);
				}
			}
			fis.close();
			return fnolReferences;
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
			return null;
		}
	}
	
	private static List<String> createFnolConflictList(File claimsFlatFile)
	{
		try
		{
			List<String> fnolReferences = new ArrayList<String>();
			XSSFSheet sheet = null;
			
			if(claimsFlatFile == null)
			{
				fnolReferences = null;
				return fnolReferences;
			}
			
			
			Set<String> fnolReferencesAtomic = new HashSet<String>();
			FileInputStream fis = new FileInputStream(claimsFlatFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); 
			
			for(int i = 0; i < workbook.getNumberOfSheets(); i++)
			{
				String sheetName = workbook.getSheetName(i);
				if(sheetName.equals("Tesco"))
					sheet = workbook.getSheetAt(i);
			}
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				if(row.getRowNum() >= 9)
				{
					Cell cell = row.getCell(7);
					
					if(!(cell == null))
					{
						switch(cell.getCellType())
						{
							case Cell.CELL_TYPE_STRING:
								String fnolRef1 = cell.getStringCellValue();
								if(!(fnolRef1.equals(null)) && fnolRef1.length() <= 7 && fnolRef1.length() > 7)
								{
									fnolReferences.add(fnolRef1);
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								Double d = cell.getNumericCellValue();
								int i = d.intValue();
								String fnolRef2 = String.valueOf(i);
								if(!(fnolRef2.equals(null)))
								{
									fnolReferences.add(fnolRef2);
								}
								break;
						}
					}
				}
			}
			
			fnolReferencesAtomic.addAll(fnolReferences);
			fnolReferences.clear();
			fnolReferences.addAll(fnolReferencesAtomic);
			fis.close();
			return fnolReferences;
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
			return null;
		}
	}
	
	public static void updateResultingSheetWithConflicts(List<String> fnolConflictsList, String resultingFileLocation)
	{
		try
		{
			String resultingFileString = resultingFileLocation+"\\test.xlsx";
			File f = new File(resultingFileString);
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook fnolWorkbook = new XSSFWorkbook(fis);
			XSSFCellStyle cellStyleTestTitle = createWorksheetCellStyle(fnolWorkbook, 197, 182, 159);
			XSSFSheet sheet = fnolWorkbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				Cell fnolRef = row.getCell(6);
				if(row.getRowNum() >= 1)
				{
					Double d = fnolRef.getNumericCellValue();
					int i = d.intValue();
					String s = String.valueOf(i);
					for(String conflict : fnolConflictsList)
					{
						if(s.equals(conflict))
						{
							Cell vrn = row.getCell(0);
							Cell deviceId = row.getCell(1);
							Cell blank1 = row.getCell(2);
							Cell date = row.getCell(3);
							Cell time = row.getCell(4);
							Cell blank2 = row.getCell(5);
							Cell causationCode = row.getCell(7);
							vrn.setCellStyle(cellStyleTestTitle);
							deviceId.setCellStyle(cellStyleTestTitle);
							blank1.setCellStyle(cellStyleTestTitle);
							fnolRef.setCellStyle(cellStyleTestTitle);
							date.setCellStyle(cellStyleTestTitle);
							time.setCellStyle(cellStyleTestTitle);
							blank2.setCellStyle(cellStyleTestTitle);
							causationCode.setCellStyle(cellStyleTestTitle);
							
						}
					}
				}
					
			}
			fis.close();
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
	
	private static XSSFCellStyle createWorksheetCellStyle(XSSFWorkbook workbook, int redHex, int greenHex, int blueHex) 
	{
		try
		{
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(redHex, greenHex, blueHex)));
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
			return cellStyle;
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
			return null;
		}
	}
	
	private static boolean checkIfConflictExists(Object fnolRef, List<String> fnolClaims)
	{
		if(fnolClaims == null)
			return false;
		
		if(fnolRef instanceof String)
		{
			String s = (String) fnolRef;
			for(String fnolClaimsRef : fnolClaims)
			{
				if(s.equals(fnolClaimsRef))
					return true;
			}
		}
		else if(fnolRef instanceof Double)
		{
			Double d = (Double) fnolRef;
			int i = d.intValue();
			String s2 = String.valueOf(i);
			for(String fnolClaimsRef : fnolClaims)
			{
				if(s2.equals(fnolClaimsRef))
					return true;
			}
		}
		
		return false;
	}
	
	public static void renameResultingFile(String resultingFileLocation)
	{
		String fileName = renameFile();
		replaceFile(resultingFileLocation, fileName);
	}
	
	private static String renameFile()
	{
		try
		{
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
			Date date = new Date();
			String dateString = dateFormat.format(date).toString();
			String dateStringTrim = dateString.replaceAll("\\s", "");
			String dateFinal = dateStringTrim.substring(0, 2)+"-"+dateStringTrim.substring(2, 4)+
					"-"+dateStringTrim.substring(4, 8)+"_"+dateStringTrim.substring(8, dateStringTrim.length());
			String logName = "FNOL_Run_"+dateFinal;
			String logNameNoColons = logName.replaceAll(":", "-");
			return logNameNoColons;
		}
		catch(Exception e)
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
			return "";
		}
	}
	
	private static void replaceFile(String workbookLocation, String logNameNoColons)
	{
		try 
		{
			File logFile = new File(workbookLocation, "test.xlsx");
			File newLogFile = new File(workbookLocation, logNameNoColons+".xlsx");
		    FileUtils.copyFile(logFile, newLogFile);
		    FileUtils.forceDelete(logFile);
		} 
		catch (IOException e) 
		{
			int lineNum = Thread.currentThread().getStackTrace()[2].getLineNumber();
			Logger.recordError(e, lineNum);
			String f = new File("").getAbsolutePath();
			String loggingDirectory = f+"\\errorLogging";
			ErrorGui.openErrorGui(loggingDirectory);
		}
	}
	
	public static void deleteTempFnolContents(String directory)
	{
		File f = new File(directory);
		File[] contents = f.listFiles();
		
		for(File file : contents)
			file.delete();
	}
}
