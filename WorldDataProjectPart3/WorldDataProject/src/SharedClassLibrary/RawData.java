/* PROJECT: WorldDataProject (Java)         CLASS: RawData
 * AUTHOR: Benjamin Johnson
 * FILES ACCESSED: RawData*.csv  is the file that input is read in from. 
 * 			       
 * FILE STRUCTURE: RawData*.csv is a serial file with commas and lines 
 * 			separating variables and rows. 
 * DESCRIPTION: A shared class that is to be used in a sequential stream
 * 			 processing algorithm. It has a boolean flag for when RawData*.csv 
 * 			 has reached EOF.
 ******************************************************************************/
package SharedClassLibrary;
import java.io.*;
public class RawData {
    //**************************** PRIVATE DECLARATIONS ************************
	private String oneLine;
    private String fileName;
    private String[] splitLine;
    private BufferedReader inFile;
    private FileReader file;
    private int recRead=0;
    private UserInterface log;
    //**************************** PUBLIC GET/SET METHODS **********************  
    public String getID()
    {
    	return splitLine[0];
    }
    /**************************************************************************/
    public String getName()
    {
    	return splitLine[2];
    }
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public RawData(String suffix,UserInterface logObj) throws IOException
    {
       log = logObj;
           	
       //log.displayThis("OK, RawData object created");
       fileName="RawData"+suffix+".csv"; 
       
       file = new FileReader(fileName);
       log.displayThis("> Opened "+fileName+" file");
       
       
       inFile = new BufferedReader(file);
    }
       
    
    //**************************** PUBLIC SERVICE METHODS **********************
    
    /***************************** noMoreCountries ******************************/
    public boolean noMoreCountries() throws IOException
    {
    	
    	if(( oneLine = inFile.readLine()) == null)
    	{
    		return true;
    	} 
    	else
    	{
    		return false;
    	}
    }
    /****************************** readOneRecord ********************************/
    public void readOneRecord() throws IOException
    {	
		splitLine = oneLine.split(",");
    	recRead++;	
    }
    /****************************** finishUp *************************************/
    public void finishUp() throws IOException
    {    		
    	log.displayThis(String.format("> RawData completed: %d records processed", recRead));
    	
    	log.displayThis("> Closed "+fileName+" file");
    	
    	file.close();
    }
    
    //**************************** PRIVATE METHODS *****************************
}
