/* PROJECT: WorldDataProject (Java)         CLASS: UserInterface
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED: LOG.txt and TransData*.txt
 * DESCRIPTION: TransData*csv is to be used in a sequential stream algorithm 
 * 		with a boolean method that returns when EOF has occurred 
 * 		the constructor accepts a boolean parameter to tell if the files it 
 * 		will open are to be used only for logging or for accepting user input 
 * 		requests also.  
 ******************************************************************************/

package SharedClassLibrary;

import java.io.*;

public class UserInterface {
    //**************************** PRIVATE DECLARATIONS ************************
	private FileWriter outFile;
	private BufferedReader transData;
	private String nameOfFile,nameOfLog;
	private String request;
	private boolean transDataOpen;
	private int transActionsHandeled;
	
    //**************************** PUBLIC GET/SET METHODS **********************
    
    
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public UserInterface(boolean onlyForLogging,String fileSuffix) throws IOException {
    	       
    	nameOfLog = "LOG.txt";
        
    	File file = new File(nameOfLog);
    
    	outFile = new FileWriter(file,true);
                   
    	displayThis(String.format("> Opened %s file", nameOfLog));
                
        if(!onlyForLogging)
        {
        	transDataOpen = true;
        	if (fileSuffix.length()>0)
            {
            	nameOfFile = "TransData" + fileSuffix +".txt";
            }
            else
            {
            	nameOfFile = "TransData.txt";
            }
            
            
            transData = new BufferedReader(new FileReader(nameOfFile));
            displayThis(String.format("> Opened %s file",nameOfFile));
        }                 
    }
    //**************************** PUBLIC SERVICE METHODS **********************
    
    /***************************** displayThis **********************************/
    public void displayThis(String lineToAdd) throws IOException
    {
    	
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
