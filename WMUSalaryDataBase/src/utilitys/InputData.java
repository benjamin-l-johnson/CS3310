

package utilitys;
import java.io.*;

public class InputData {

	private String oneLine;
	private String fileName;
	private String[] splitLine;
	private BufferedReader inFile;
	private FileReader file;
	private int idCount=0;
	private int recRead=0;
	private Log log;


	//**************************** PRIVATE DECLARATIONS ************************

	//**************************** PUBLIC GET/SET METHODS **********************
	public int getID()
	{
		return idCount;
	}
	public String getName()
	{
		return splitLine[0];
	}
	public String getTitle()
	{
		return splitLine[1];
	}
	public String getLocation()
	{
		return splitLine[2];
	}
	public String getSalary()
	{
		return splitLine[3];
	}
	public String getPeriod()
	{
		return splitLine[4];
	}

	//**************************** PUBLIC CONSTRUCTOR(S) ***********************
	public InputData(Log logObj) throws IOException
	{
		log = logObj;

		//log.displayThis("OK, RawData object created");
		fileName="lines.txt";

		file = new FileReader(fileName);
		log.displayThis("> Opened "+fileName+" file");


		inFile = new BufferedReader(file);
	}








	//**************************** PUBLIC SERVICE METHODS **********************

	/***************************** noMoreCountries ******************************/
	public boolean noMoreNames() throws IOException
	{

		if((oneLine = inFile.readLine()) == null)
		{
			
			return true;
		}
		else
		{
			idCount++;
			return false;
		}
	}
	/****************************** readOneRecord ********************************/
	public void readOneRecord() throws IOException
	{
		splitLine = oneLine.split("--");
		recRead++;



	}
	/****************************** finishUp *************************************/
	public void finishUp() throws IOException
	{
		log.displayThis(String.format("> RawData completed: %d records processed", recRead));

		log.displayThis("> Closed "+fileName+" file");

		file.close();
	}
}


