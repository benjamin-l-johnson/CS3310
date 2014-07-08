
/* PROJECT: WorldDataProject (Java)         CLASS: MainData
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED: 
 * 	input:MainData.txt 
 * 	output: UserInterFace object 
 * FILE STRUCTURE: See specs
 * DESCRIPTION: Constructor takes a parameter of UserInterFace for outputting 
 * 	to the logFile.
*******************************************************************************/

package SharedClassLibrary;
import java.io.*;
public class MainData 
{
    //**************************** PRIVATE DECLARATIONS ************************
    private String fileName; 
    private RandomAccessFile mainData;
    private String recordInMem;
    private int sizeOfDataRec=49;
    private UserInterface log;
    
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public MainData(UserInterface log_) throws IOException 
    {
      	log = log_;
    	fileName = "MainDataA3.txt";
        mainData = new RandomAccessFile(fileName,"rw");
        log.displayThis("> Opened " + fileName + " file");
    }
    //**************************** PUBLIC SERVICE METHODS **********************

     /****************************** getThisData ******************************/
    public void getThisData(short rrn) throws IOException
    {
    	read1Rec(rrn);
    	log.displayThis(prettyPrintRecordInMem());
    }
    /****************************** finishUp **********************************/
    public void FinishUp() throws IOException
    {    		
    	log.displayThis("> Closed "+fileName +" file");
    	mainData.close();    	
    }  
    //**************************** PRIVATE METHODS *****************************

    /************************** prettyPrintRecordInMem ***************************
	* Takes the record currently stored in memory, and returns a string that     *
	* is split up with a proper neatly format                                    *
	******************************************************************************/
    private String prettyPrintRecordInMem()
    {
    	int pop = Integer.parseInt(recordInMem.substring(34,44).trim());
    	
    	float indep = Float.parseFloat(recordInMem.substring(44,49));
    	
    	String line = String.format("%s  %s  %s  %,13d  %.1f",
    	    			recordInMem.substring(0, 3),recordInMem.substring(3,21),recordInMem.substring(21,34),pop,indep);
    	
    	return line;
    }
    /**************************** read1Rec(int rrn) ********************************/   
    private void read1Rec(int rrn) throws IOException
    {
    	int byteOffSet = calculateByteOffset(rrn);
    	
    	mainData.seek(byteOffSet);
    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	    	
    	mainData.read(fixedLenRec,0 , sizeOfDataRec);
    	
    	recordInMem = new String(fixedLenRec);	    	
    }
    /**************************** calculateByteOffset ******************************/
    private int calculateByteOffset(int rrn)
    {
    	return ((rrn-1)*sizeOfDataRec);
    } 
}
