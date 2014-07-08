/* PROJECT: WorldDataProject (Java)         PROGRAM: ShowFilesUtility
 * AUTHOR: Benjamin Johnson
 * OOP CLASSES USED:  none (this does not use the OOP paradigm)
 * FILES ACCESSED:  (all files handled by DIRECTLY by THIS program)
 *      INPUT:   MainData*.txt
 *      OUTPUT:  LOG.txt
 *      where * is the appropriate fileNameSuffix.
 * DESCRIPTION:  This is a utility program for the developer.  As such, it's
 *      just a quick non-OOP program which accesses the files DIRECTLY.
 *      It pretty-prints (SEE SPECS) the NameIndexBackup file to the Log file.
 ******************************************************************************/

package ShowFilesUtility;
import java.io.*;

import SharedClassLibrary.MainData;
public class ShowFilesUtility 
{
	
	private String fileName;
	
	
	
    public static void main(String[] args) throws IOException{

        String nameOfLog = "LOG.txt";
        File file = new File(nameOfLog);
    
    	FileWriter outFile = new FileWriter(file,true);
        outFile.write("> Opened "+nameOfLog+" file \r\n");
       
    	String fileName = "MainData.txt";
    	
    	RandomAccessFile mainData = new RandomAccessFile(fileName,"rw");
		outFile.write("> Opened "+fileName+" file \r\n");
    	 	   	
    	byte[] headRec = new byte[3];
    	
		//reading in the header Record and saving it to a variable
    	String line,rrnLine;
    	mainData.seek(0);
    	mainData.read(headRec);
    	rrnLine = new String(headRec);
    	
		
		int rrn = 0;
    	outFile.write("MAIN DATA ******************************************************************************************** \r\n");
    	outFile.write("[RRN]  ID  CODE NAME               CONTINENT     REGION        AREA       INDEP  POPULATION      L.EXP \r\n");
    	int bytesIn;
    	int sizeOfDataRec = 71;
    	
    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	
    	//Set bytesIn to a record until it equals -1
    	while(!((bytesIn = mainData.read(fixedLenRec,0 , sizeOfDataRec))==-1))
    	{   		
    		line = new String(fixedLenRec);
    		
    		rrn++;
    		outFile.write(String.format("[%03d]  ", rrn));
 
    		if(!line.substring(0,2).equals("\0\0"))
    		{
    			outFile.write(splitAndPrint(line)+"\r\n");
    		}
    		else
    		{
    			outFile.write("EMPTY \r\n");
    		}
    	}
    	outFile.write("END OF MAIN DATA \r\n****************************************************************************************************** \r\n");
    	outFile.write("N is "+rrnLine+"\r\n");
    	
    	outFile.write("> Closed "+fileName+" file \r\n");
    	mainData.close();
    	
    	outFile.write("> Closed "+nameOfLog+" file \r\n");
    	outFile.close();
        
        
    
    	}
    	//**************************** PRIVATE METHOD ******************************
    	
		/**********************************************************************************
		*Method splitAndPrint takes in a string, parses out important numbers then returns*
		*a string printed in a proper format.                                             *
		***********************************************************************************/

		private static String splitAndPrint(String line)
    	{
    		int area = Integer.parseInt(line.substring(44,52));
        	int Indep = Integer.parseInt(line.substring(52,57).trim());
        	String indepStr = String.format("%04d", Indep);
        	float lifeExp = Float.parseFloat(line.substring(67,71));

        	int pop = Integer.parseInt(line.substring(57,67));

        	String newLine = String.format("%s %s  %s  %s   %s   %,10d  %5s  %,13d   %.1f",
        			
        			line.substring(0, 3),line.substring(3,6),line.substring(6,23),
        			
        			line.substring(23,34),line.substring(34,44),area,
        			
        			indepStr,pop,lifeExp);

    		return newLine;
    	 }
    	
    	}
