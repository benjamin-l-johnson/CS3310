/* PROJECT: WorldDataProject (Java)         PROGRAM: UserApp
 * AUTHOR: Benjamin Johnson
 * OOP CLASSES USED (for Asgn 1): DataStorage, UserInterface
 * FILES ACCESSED: (only INDIRECTLY through the OOP classes)
 *      INPUT:   TransDataA3.txt         (handled by UserInterface class)
 *      OUTPUT:  LOG.txt                (handled by UserIterface class)
 *      OUTPUT:  MainData.txt           (handled by NameIndex class depending 
 *      									on boolean passed to constructor)
 * DESCRIPTION:  The program itself is just the CONTROLLER which UTILIZES
 *      the SERVICES (public methods) of various OOP classes.
 *      It processes the transaction requests in TransData file, DataStorage
 *      processes the result publishes it to the Log file. 
 * CONTROLLER ALGORITHM:  Traditional sequential-stream processing - i.e., 
 *      loop til done with TransData
 *      {   input 1 transaction request from TransData
 *          switch to use that data to call appropriate service in DataStorage
 *                  class to handle request
 *      }
 *      finish up with UserInterFace
 *      finish up with DataStorage
 ******************************************************************************/

package UserApp;

import java.io.IOException;

import SharedClassLibrary.MainData;
import SharedClassLibrary.NameIndex;
import SharedClassLibrary.UserInterface;

public class UserApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {       
        // Detect whether this program is being run by AutoTesterUtility,
        //      or manually by developer & fix fileNameSuffix accordingly.
        // 
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        String mainFileSuffix,transFileSuffix;
        if (args.length > 0) {
            
        	mainFileSuffix = args[0];
        	transFileSuffix = args[1];
        	
        } else {
            mainFileSuffix = "";
            transFileSuffix = "A3";
        }

        UserInterface log = new UserInterface(false,transFileSuffix);
        NameIndex nameFile = new NameIndex(log, false);

    	while(log.noMoreRequests() != true)
    	{
    		//splitting transaction request at the first space only
    		String[] request = log.inputOneTransaction().split(" ",2);
    		
    		switch (request[0])
    		{
    			case "QN":	
    				log.displayThis(request[0]+" "+request[1]);
    				nameFile.queryByName(request[1]);
    				break;
    			
    			case "LN":
    				log.displayThis(request[0]);
    				nameFile.listAllByName();
    				break;
    			default:
    				log.displayThis("Sorry that is not option try agian");
    		}
    	}
    	nameFile.finishUp();
    	log.displayThis("> UserApp completed");
    	log.finishUp();
    } 
}
