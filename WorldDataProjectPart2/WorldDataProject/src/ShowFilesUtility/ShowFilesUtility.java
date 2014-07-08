/* PROJECT: WorldDataProject (Java)         PROGRAM: PrettyPrintBinUtility
 * AUTHOR: Andrew Brower 9-25-2013, Benjamin Johnson
 * OOP CLASSES USED:  none (this does not use the OOP paradigm)
 * FILES ACCESSED:  (all files handled by DIRECTLY by THIS program)
 *      INPUT:   MainData*.bin
 *      OUTPUT:  log.txt
 *      where * is the appropriate fileNameSuffix.
 * DESCRIPTION:  This is a utility program for the developer.  As such, it's
 *      just a quick non-OOP program which accesses the files DIRECTLY.
 *      It pretty-prints (SEE SPECS) the MainData file to the Log file.
 *      I took some of my old code and some of Andrews code to make sure that 
 *      each file was opened and to account for future files if need be
 * CONTROLLER ALGORITHM:  Traditional sequential file processing . . .
 ******************************************************************************/

package ShowFilesUtility;
import java.io.*;
import java.text.DecimalFormat;
import SharedClassLibrary.MainData;
public class ShowFilesUtility 
{
	private static String fileName,extraFileName;
	private static RandomAccessFile mainData;
		
    public static void main(String[] args) throws IOException{
        String fileNameSuffix;
        if (args.length > 0) {
            fileNameSuffix = args[0];
        } else {
            fileNameSuffix = "A2";
        }
        
    	String line;
        String nameOfLog = "LOG.txt";
        File file = new File(nameOfLog);
    	FileWriter outFile = new FileWriter(file,true);
        outFile.write("> Opened "+nameOfLog+" file \r\n");
       
    	fileName = "MainDataA2.bin";
    	mainData = new RandomAccessFile(fileName,"rw");
		outFile.write("> Opened "+fileName+" file \r\n");
    	 	   	
		//reading in the header Record and storing the name of any "extra files"
    	String header = displayHeader();
		
		int rrn = 0;
    	outFile.write(fileName+" ******************************************************************************************** \r\n");
    	outFile.write("[RRN]CODE NAME------------- CONTINENT---- ---AREA---" + 
		" INDEP --POPULATION- L.EX ---GNP--- LINK\r\n");
    	int bytesIn;
    	int sizeOfDataRec = 56;
    	int sizeOfheaderRec=22+6;
    	
    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	mainData.seek(sizeOfDataRec + sizeOfheaderRec);
    	
    	//Set bytesIn to read a whole record until it equals -1
    	while(!((bytesIn = mainData.read(fixedLenRec,0 , sizeOfDataRec))==-1))
    	{   		
    		line = new String(fixedLenRec);
    		
    		rrn++;
    		outFile.write(String.format("[%03d] ", rrn));
    		
    		//if the line is not a null byte or space read the record
    		if(!line.substring(0,2).equals("\0\0")&& !line.substring(0,2).contentEquals("  "))
    		{
    			//if the line is not empty seek back to the the beginning of that record
    			mainData.seek(sizeOfheaderRec+(sizeOfDataRec*rrn));
    			outFile.write(readOneRec(rrn)+"\r\n");
    		}
    		else
    		{
    			outFile.write("EMPTY \r\n");
    		}
    	}
    	outFile.write("END ***************************************************************************************************** \r\n");
    	
    	outFile.write("> Closed "+fileName+" file \r\n");
    	mainData.close();
    	
		outFile.write("> Opened "+extraFileName+" file \r\n");
    	mainData= new RandomAccessFile(extraFileName,"rw");
    	
    	//resetting rrn to zero since it is a new file
    	rrn = 0;
    	
    	outFile.write(extraFileName+" *************************************************************************************** \r\n");
    	outFile.write("[RRN]CODE NAME------------- CONTINENT---- ---AREA---" + 
		" INDEP --POPULATION- L.EX ---GNP--- LINK\r\n");
    	
    	fixedLenRec = new byte[sizeOfDataRec];
    	mainData.seek(sizeOfDataRec);
    	
    	//Set bytesIn to read a whole record until it equals -1
    	while(!((bytesIn = mainData.read(fixedLenRec,0 , sizeOfDataRec))==-1))
    	{   		
    		line = new String(fixedLenRec);
    		
    		rrn++;
    		outFile.write(String.format("[%03d] ", rrn));
 
    		if(!line.substring(0,2).equals("\0\0") && !line.substring(0,2).contentEquals("  "))
    		{
    			mainData.seek(sizeOfDataRec*rrn);
    			outFile.write(readOneRec(rrn)+"\r\n");
    		}
    		else
    		{
    			outFile.write("EMPTY \r\n");
    		}
    	}
    	outFile.write("END ****************************************************************************************************** \r\n");
    	outFile.write(header +"\r\n");
    	    	
    	outFile.write("> Closed "+extraFileName+" file \r\n");
    	mainData.close();
    	outFile.write("> Closed "+nameOfLog+" file \r\n");
    	outFile.close();
    
    	}
    	//**************************** PRIVATE METHOD ******************************
      	//written by andrew
		private static String readOneRec(int rrn) throws IOException
		{
			byte[] byteBuffer;
			DecimalFormat format;
			StringBuilder country = new StringBuilder();
						
			//countryCode
	        byteBuffer = new byte[3];
	        mainData.read(byteBuffer);
	        country.append(new String(byteBuffer));
	        country.append(" ");
	        
	        //name
	        byteBuffer = new byte[17];
	        mainData.read(byteBuffer);
	        country.append(new String(byteBuffer));
	        country.append(" ");
	        
	        //continent
	        byteBuffer = new byte[12];
	        mainData.read(byteBuffer);
	        country.append(new String(byteBuffer));
	        country.append(" ");
	        
	        //surfaceArea
	        int area = mainData.readInt();
	        format = new DecimalFormat("#,###,###,###");
	        //right-justify and format the area
	        country.append(leadingSpaces(format.format(area),10));
	        country.append(" ");
	        
	        //indep
	        short indep = mainData.readShort();
	        format = new DecimalFormat("0000");
	        //add a space if it is a positive number
	        //so it lines up properly
	        if (indep >= 0) country.append(" " + format.format(indep));
	        else country.append(format.format(indep));
	        country.append(" ");
	        
	        //population
	        long pop = mainData.readLong();
	        format = new DecimalFormat("#,###,###,###");
	        //right-justify and format the area
	        country.append(leadingSpaces(format.format(pop),13));
	        country.append(" ");
	        
	        //lifeexp
	        float lifeExp = mainData.readFloat();
	        format = new DecimalFormat("##.#");
	        country.append(leadingSpaces(format.format(lifeExp),5));
	        
	        
	        //gnp
	        int gnp = mainData.readInt();
	        format = new DecimalFormat("#,###,###,###");
	        //right-justify and format the area
	        country.append(leadingSpaces(format.format(gnp),10));
	        
	        
	        short link = mainData.readShort();
	        country.append("  ");
	        format = new DecimalFormat("0");
	        country.append(leadingSpaces(format.format(link),3));
	        
	        return country.toString();

		}
		
		//written by andrew
		private static String leadingSpaces(String name, int length) 
		{
	        while (name.length() < length) {
	              name = " ".concat(name);
	          }
	          return name;
	    }
		
		private static String displayHeader() throws IOException
		{
			mainData.seek(0); //just to be sure
	        StringBuilder header = new StringBuilder();
	        short tmp;
	        
	        //for each element, read it into a byte[] buffer
	        //and cast it to the appropriate type.
	        //keep your fingers crossed.
	        
	        //read nHomeRec
	        tmp = mainData.readShort();
	        header.append("#Rec in Home Area: " + tmp);
	        
	        //read nCollRec
	        tmp = mainData.readShort();
	        header.append(", #Rec in Collision Area: " + tmp);
	        
	        //read MAX_N_HOME_LOC
	        tmp = mainData.readShort();
	        header.append(", #Rec Allowed in Home Area: " + tmp + "\r\n");
	        
	        //read what should be "MainDataCollisions.bin"
	        //assuming 22 character long file name
	        //see comments at declaration of HEADERSIZE
	        byte[] byteBuffer = new byte[22];
	        mainData.read(byteBuffer);
	        
	        //setting "extraFilename"
	        extraFileName = new String(byteBuffer);
	        return header.toString();
		}

	}
