package utilitys;

import java.io.*;

public class Log {

	private FileWriter outFile;
	private BufferedReader transData;
	private String nameOfFile,nameOfLog;
	private String request;
	private boolean transDataOpen;
	private int transActionsHandeled;

	public Log(boolean onlyForLogging) throws IOException {

		nameOfLog = "Log.txt";

		File file = new File(nameOfLog);

		outFile = new FileWriter(file,true);

		displayThis(String.format("> Opened %s file", nameOfLog));

		if(!onlyForLogging)
		{
			transDataOpen = true;



			transData = new BufferedReader(new FileReader(nameOfFile));
			displayThis(String.format("> Opened %s file",nameOfFile));
		}
	}
	//**************************** PUBLIC SERVICE METHODS **********************

	/***************************** displayThis **********************************/
	public void displayThis(String lineToAdd) throws IOException
	{
		System.out.println(lineToAdd);
		outFile.write(lineToAdd+"\r\n");
	}
	/***************************** noMoreRequests *********************************/
	public boolean noMoreRequests() throws IOException
	{
		if((request = transData.readLine())== null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public String inputOneTransaction()
	{

		transActionsHandeled++;
		request = request.trim();

		return request;
	}
	public void finishUp() throws IOException
	{
		if(transDataOpen)
		{
			displayThis("> Closed "+nameOfFile + " file");
			transData.close();

			displayThis(String.format("> UserInterface completed: %d transactions handled", transActionsHandeled));
		}

		displayThis("> Closed "+nameOfLog + " file");
		outFile.close();
	}

}


