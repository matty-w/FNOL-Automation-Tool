package logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class Logger 
{
	public static void recordError(Exception e, int lineNum)
	{
		String error = e.getLocalizedMessage();
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String errorLog = "Recorded Error: "+error;
		String fullClassNameLog = "Full Class Name: "+fullClassName;
		String classNameLog = "Class Name: "+className;
		String methodNameLog = "Method: "+methodName;
		String lineNumLog = "Line Number: "+lineNum;
		String f = new File("").getAbsolutePath();
		String logFile = f+"\\errorLogging\\log.txt";
		File log = new File(logFile);
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(log, true);
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
			Date date = new Date();
			String dateString = dateFormat.format(date).toString();
			String dateStringTrim = dateString.replaceAll("\\s", "");
			String dateFinal = dateStringTrim.substring(0, 2)+"-"+dateStringTrim.substring(2, 4)+
					"-"+dateStringTrim.substring(4, 8)+"_"+dateStringTrim.substring(8, dateStringTrim.length());
			
			fw.write("["+dateFinal+"] - "+errorLog);
			fw.write(System.lineSeparator());
			fw.write("["+dateFinal+"] - "+fullClassNameLog);
			fw.write(System.lineSeparator());
			fw.write("["+dateFinal+"] - "+classNameLog);
			fw.write(System.lineSeparator());
			fw.write("["+dateFinal+"] - "+methodNameLog);
			fw.write(System.lineSeparator());
			fw.write("["+dateFinal+"] - "+lineNumLog);
			fw.write(System.lineSeparator());
			fw.close();
			renameFile(dateFinal);
		}
		catch(Exception k)
		{
		}
	}
	
	public static void renameFile(String dateFinal)
	{
		String s = nameErrorReport();
		replaceLogFile(s);
	}
	
	private static void replaceLogFile(String logNameNoColons)
	{
		String f = new File("").getAbsolutePath();
		String path = f+"//errorLogging";
		String logFilePath = f+"//errorLogging//log.txt";
		File logFile = new File(logFilePath);
		File newLogFile = new File(path+"//"+logNameNoColons+".txt");
		try 
		{
		    FileUtils.copyFile(logFile, newLogFile);
		    FileUtils.forceDelete(logFile);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static String nameErrorReport()
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
		Date date = new Date();
		String dateString = dateFormat.format(date).toString();
		String dateStringTrim = dateString.replaceAll("\\s", "");
		String dateFinal = dateStringTrim.substring(0, 2)+"-"+dateStringTrim.substring(2, 4)+
				"-"+dateStringTrim.substring(4, 8)+"_"+dateStringTrim.substring(8, dateStringTrim.length());
		String logName = "ErrorReport_"+dateFinal;
		String logNameNoColons = logName.replaceAll(":", "-");
		return logNameNoColons;
	}

}
