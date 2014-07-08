/* PROJECT: WorldDataProject (Java)         PROGRAM: PrettyPrintUtility
 * AUTHOR:Benjamin Johnson
 * OOP CLASSES USED:  none (this does not use the OOP paradigm)
 * FILES ACCESSED:  (all files handled by DIRECTLY by THIS program)
 *      INPUT:   NameIndexBackup.bin 
 *      OUTPUT:  LOG.txt
 * DESCRIPTION:  This is a utility program for the developer.  As such, it's
 *      just a quick non-OOP program which accesses the files DIRECTLY.
 *      It pretty-prints (SEE SPECS) the NameIndexBackup file to the Log file.
 * CONTROLLER ALGORITHM:  Traditional sequential file processing . . .
 ******************************************************************************/
package PrettyPrintUtility;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class PrettyPrintUtility 
{
    public static void main(String[] args) throws IOException
    {
    	String fileName="IndexBackup.bin";;
        String nameOfLog = "LOG.txt";
         
        short rootPtr,n,lcp,drp,rcp;
    	String kv;

        File file = new File(nameOfLog);
    	FileWriter outFile = new FileWriter(file,true);
        outFile.write("> Opened "+nameOfLog+" file \r\n");
                
        InputStream fileB = new FileInputStream(fileName);
        DataInputStream indexBackup = new DataInputStream(fileB);
		outFile.write("> Opened "+fileName+" file \r\n");
		
    	rootPtr = indexBackup.readShort();
    	n = indexBackup.readShort();

    	outFile.write(String.format("RootPtr is %d, N is %d \n", rootPtr,n));
    	outFile.write("[SUB]  LCH  NAME--------------  DRP  RCH \n");
    	
    	for(int i =0; i < n ;i++)
    	{
    		lcp = indexBackup.readShort();
    		kv = indexBackup.readUTF();
    		drp = indexBackup.readShort();
    		rcp =indexBackup.readShort();
    		outFile.write(String.format("[%03d]  %03d  %-18s  %03d  %03d \n",i, lcp,kv,drp,rcp));
    	}
    	outFile.write("END ********************************************************* \r\n");
    	
    	outFile.write("> Closed "+fileName+" file \r\n");
    	indexBackup.close();
    	
    	outFile.write("> Closed "+nameOfLog+" file \r\n");
    	outFile.close();
    }
}