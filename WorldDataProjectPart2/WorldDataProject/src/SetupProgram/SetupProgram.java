/* PROJECT:  WorldDataProject (Java)            PROGRAM: SetupProgram
 * AUTHOR: Benjamin Johnson
 * OOP CLASSES USED :  inputFileData, UserInterface, NameIndex
 *      PLUS FOR FUTURE ASGN:  DataStorage, CodeIndex
 * FILES ACCESSED: (only INDIRECTLY through the OOP classes)
 *      INPUT:   RawData*.csv           (handled by RawData class)
 *      OUTPUT:  Log.txt                (handled by UserInterface class)
 *      OUTPUT:  MainData*.bin (& "INPUT" to check for "empty locations")
 *                                      ( handled by DataStorage class)
 * DESCRIPTION:  The program itself is just the CONTROLLER which UTILIZES
 *      the SERVICES (public methods) of various OOP classes.
 *          Creates a random access MainData file while taking records from
 *          the input file.
 * CONTROLLER ALGORITHM:  Traditional sequential-stream processing - i.e., 
 *      loop til done with inputFileData
 *      {   input 1 data set from inputFileData
 *          use that data to construct an entry for NameIndex (calling
 *                  appropriate service method in that class)
 *      }
 *      finish up with RawData
 *      finish up with MainData
 *      finish up with Userinterface
 ******************************************************************************/
package SetupProgram;

import java.io.*;

import SharedClassLibrary.MainData;
import SharedClassLibrary.RawData;
import SharedClassLibrary.UserInterface;



public class SetupProgram {
    public static void main(String[] args) throws IOException {
//    	 Detect whether this program is being run by AutoTesterUtility, assignment 
//              or manually by developer & fix mainFileSuffixes accordingly.

        String mainFileSuffix;
        if (args.length > 0) {
            mainFileSuffix = args[0];
        } else {
            mainFileSuffix = "A2";
        }        
    	UserInterface log = new UserInterface(true,"");
    	
    	RawData inputFile = new RawData(mainFileSuffix,log);
    	
    	MainData mainData = new MainData(log,true);
    	
    	while(inputFile.noMoreCountries() != true)
    	{
    		inputFile.readOneRecord();

    		mainData.store1Country(inputFile.getCode(),inputFile.getName(),inputFile.getContinent(),
    				inputFile.getSurfaceArea(),inputFile.getYearOfIndep(),inputFile.getPopulation(),
    				inputFile.getLifeExp(),inputFile.getGNP());
    	}

    	log.displayThis("> Setup completed");
    	inputFile.finishUp();
    	mainData.FinishUp();
    	log.finishUp();
    }
    //*********************** PRIVATE METHODS ********************************
}
