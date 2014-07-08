/* PROJECT: WorldDataProject (Java)         PROGRAM: PrettyPrintBinUtility
 * AUTHOR: Andrew Brower 9-25-2013
 * OOP CLASSES USED:  none (this does not use the OOP paradigm)
 * FILES ACCESSED:  (all files handled by DIRECTLY by THIS program)
 *      INPUT:   MainData*.bin
 *      OUTPUT:  log.txt
 *      where * is the appropriate fileNameSuffix.
 * DESCRIPTION:  This is a utility program for the developer.  As such, it's
 *      just a quick non-OOP program which accesses the files DIRECTLY.
 *      It pretty-prints (SEE SPECS) the MainData file to the Log file.
 * CONTROLLER ALGORITHM:  Traditional sequential file processing . . .
 ******************************************************************************/

package PrettyPrintUtility;

import java.text.DecimalFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.PrintWriter;

import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;


public class PrettyPrintUtility 
{
    private static RandomAccessFile mainData = null;
    private static FileWriter log = null;
    private static int rrnCounter = 0;
    
    /*
    Declare size of the header record.
    nHomeRec (short int) 2 bytes
    nCollRec (short int) 2 bytes
    MAX_N_HOME_LOC (short int) 2 bytes
    
    //NULL TERMINATOR WARNING//
    collisionFileName (char array) "MainDataCollisions.bin"
    Is 22 bytes, unless if it is null terminated. Then it will be 23.
    We will assume it is a 22 character string, according to 
    the A2Specs. This could potentially be modified for variable-length
    null terminated strings at runtime.
    */
    private static int HEADERSIZE = 2 + 2 + 2 + 22;
            
            
    /*
    declare the offset for each county.
    countryCode (3 char) 3 bytes
    name    (17 char) 17 bytes
    continent (12 char) 12 bytes
    surfaceArea (integer) 4 bytes
    independance (short) 2 bytes
    population (long) 8 bytes
    lifeExp (float) 4 bytes
    gnp (float) 4 bytes
    */
    private static final int COUNTRYSIZE = 3 + 17 + 12 + 4 + 2 + 8 + 4 + 4+2;
    private static String fromHead;        
    private static boolean readFromHead=false;            
    
    public static void main(String[] args) throws IOException{
        System.out.println("OK, starting PrettyPrintUtility");
             readFromHead = false;   
        // Detect whether this program is being run by AutoTesterUtility,
        //     or manually by developer & fix fileNameSuffix & N accordingly.
        // <WRITE APPROPRIATE CODE HERE>
        
        String fileNameSuffix;
        if (args.length > 0) {
            fileNameSuffix = args[0];
        } else {
            fileNameSuffix = "A2";
        }
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        try {
        
        	File logFile = new File("LOG.txt");
            log = new FileWriter(logFile,true);
        
        } catch (Exception e) {
            System.out.println("Error opening log.txt for writing");
            System.exit(1);
        
        }
        System.out.println("Opened log.txt");
        
        //open log.txt and MainData.bin
        openFile("MainData" + fileNameSuffix + ".bin");
        
        //display header record
        //if(!readFromHead){
        	
        
        	try {
        		displayHeader();
        	} catch (IOException e) {
        		System.out.println("Error reading header from MainData");
        		System.exit(1);
        	}
        
        //else
        //{
        //display table columns
        log.write("[RRN]CODE NAME------------- CONTINENT--- ---AREA--- " + 
		"INDEP --POPULATION- L.EX ---GNP--- LINK\r\n");
        
        //declare variables for loop
        String country;
        
        //main display loop
        while (true) {
            
            try {
                /*
                fetch the string representation of the
                next country and print it to the log file
                */
                country = readOneCountry();
                //System.out.println(country);
                log.write(country + "\n");
                
            } catch (EOFException e) {
                System.out.println("DoneReading MainDAta");//we're done reading MainData
                break;
                
            } catch (IOException e) {
                System.out.println("Error reading from MainData");
                System.exit(1);
            
            }
        }
        closeFile(0);
        
        
        
        openFile("MainData" + "Collisions" + ".bin");
        HEADERSIZE = 0;
        rrnCounter=0;
        log.write("[RRN]CODE NAME------------- CONTINENT--- ---AREA--- " + 
        		"INDEP --POPULATION- L.EX ---GNP--- LINK\n");
                
                //declare variables for loop

                
                //main display loop
                while (true) {
                    
                    try {
                        /*
                        fetch the string representation of the
                        next country and print it to the log file
                        */
                        country = readOneCountry();
                        //System.out.println(country);
                        log.write(country +"\n");
                        
                    } catch (EOFException e) {
                        System.out.println("Done Reading Collisons");//we're done reading MainData
                        break;
                        
                    }
                    catch (IOException e) {
                        System.out.println(e);
                        break;
                    
                    }
                }
        closeFile(0);       
        log.write("> Closed log.txt");
        log.close();System.out.println("closed log");
       // }
        
    }
    
    private static void displayHeader() throws IOException {
        seek(0); //just to be sure
        StringBuilder header = new StringBuilder();
        short tmp;
        
        //for each element, read it into a byte[] buffer
        //and cast it to the appropriate type.
        //keep your fingers crossed.
        
        //read nHomeRec
        tmp = mainData.readShort();
        header.append("nHomeRec: " + tmp + "\r\n");
        
        //read nCollRec
        tmp = mainData.readShort();
        header.append("nCollRec: " + tmp + "\r\n");
        
        //read MAX_N_HOME_LOC
        tmp = mainData.readShort();
        header.append("MAX_N_HOME_LOC: " + tmp + "\r\n");
        
        //read what should be "MainDataCollisions.bin"
        //assuming 22 character long file name
        //see comments at declaration of HEADERSIZE
        byte[] byteBuffer = new byte[22];
        mainData.read(byteBuffer);
        header.append(new String(byteBuffer));
        
        log.write(header.toString() + "\r\n");
    }
    
    /**
     * format and fetch the next country
     * to display. This method could
     * have been overloaded, however pretty print
     * isn't doing anything other than sequential reading.
     * @return
     * @throws EOFException Fell off the end of file. throw a rope!
     * @throws IOException Error reading file
     */
    private static String readOneCountry() throws EOFException, IOException{
    	DecimalFormat format;
        StringBuilder country = new StringBuilder();
        //increment the rrn counter for each index
        rrnCounter++;
        
        //declare temp variables we will use and re-use
        
        byte[] byteBuffer;
        
        //specify format of the RRN and append it
        format = new DecimalFormat("[000] ");
        country.append(format.format(rrnCounter));
        
        //seek to the index we wish to read
        seekIndex(rrnCounter);
        
        //check if index exists. We expect a char for
        //the country code. If the first byte is null, assume empty.
        if (mainData.readByte() == 0) {
            //return empty
            country.append("EMPTY");
            return country.toString();
        }
        
        //seek back to beginning of this index
        //and read for real, since we know it is valid.
        seekIndex(rrnCounter);
        
        /*
         *for each entry, read in and format
         *data for displaying.
         */
        
        //RawData contains NULL for several values.
        //I am assuming these are entered as zeros in
        //the MainData.bin file.
        
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
        
        
        //yay, we're done!
        return country.toString();
    }
    
    /**
     * seek to a specific index in the file
     * @param index
     * @throws IOException error seeking index
     */
    private static void seekIndex(int index) throws IOException{
        //direct address
    	
        seek(HEADERSIZE + (COUNTRYSIZE * (index)));
    }
    
    /**
     * seek to a specific byte in the file
     * @param byteLength
     * @throws IOException error seeking to location
     */
    private static void seek(int byteLength) throws IOException {
        mainData.seek(byteLength);
    }
    
    /**
     * Add spaces after a string until it is long enough
     * if string already exceeds size, return it without changes.
     * TODO:
     * I'm sure this could be handled better with String.format()
     * @param name
     * @param length
     * @return 
     */
      private static String trailSpaces(String name, int length) {
          while (name.length() < length) {
              name = name.concat(" ");
          }
          return name;
      }
    
    /**
     * Add spaces before a string until it is long enough
     * if string already exceeds size, return it without changes.
     * TODO:
     * I'm sure this could be handled better with String.format()
     * @param name
     * @param length
     * @return 
     */
    private static String leadingSpaces(String name, int length) {
        while (name.length() < length) {
              name = " ".concat(name);
          }
          return name;
    }
    
    /**
     * Opens the files we will be using. 
     * Files are Log.txt and MainData*.bin
     * Displays error and exits if files can't be opened.
     * @param mainName      name of the MainData*.bin file
     */
    private static void openFile(String mainName) {
        
        //open log

        
        //open MainData
        try {
            File mainFile = new File(mainName);
            mainData = new RandomAccessFile(mainFile,"rw");
            mainData.seek(0);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find " + mainName);
            System.exit(1);
        } catch (IOException e) {
        	 System.out.println("Unable to find " + mainName);
             System.exit(1);
		}
        
        System.out.println("Opened " + mainName);
    }
    
    /**
     * Tidy up our files and exit the program
     * @param status    return status of the program
     */
    private static void closeFile(int status) {
        
        try {
            
            	mainData.close(); //System.out.println("closed mainDAta");
            
        } catch (IOException e) {
            System.out.println("error closing MainData");
            System.exit(status);
        }
        
        //System.exit(status);
    }
}