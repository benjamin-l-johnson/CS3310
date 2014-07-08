/* PROJECT: WorldDataProject (Java)         PROGRAM: UserApp
 * AUTHOR: Benjamin Johnson
 * OOP CLASSES USED (for Asgn 1): DataStorage, UserInterface
 * FILES ACCESSED: (only INDIRECTLY through the OOP classes)
 *      INPUT:   TransData*.txt         (handled by UserInterface class)
 *      OUTPUT:  Log.txt                (handled by UserIterface class)
 *      OUTPUT:  MainData.txt          (handled by DataStorage class)
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

import SharedClassLibrary.DataStorage;
import SharedClassLibrary.UserInterface;

public class UserApp {
    public static void main(String[] args) throws IOException {
        //System.out.println("OK, starting UserApp");
        
        // Detect whether this program is being run by AutoTesterUtility,
        //      or manually by developer & fix fileNameSuffix accordingly.
        // 
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        String mainFileSuffix,transFileSuffix;
        if (args.length > 0) {
            
        	mainFileSuffix = args[0];
        	transFileSuffix = args[1];
        	
        } else {
            mainFileSuffix = "Just26";
            transFileSuffix = "Empty";
        }

        UserInterface log = new UserInterface(false,transFileSuffix);
        
        DataStorage mainFile = new DataStorage(log,false);

    	while(log.noMoreRequests() != true)
    	{

    		String[] request = log.inputOneTransaction().split(" ");
    		int rrn;
    		switch (request[0])
    		{
    			case "QI":	
    				rrn = Integer.parseInt(request[1]);
    				log.displayThis(request[0]+" "+request[1]);
    				mainFile.queryByID(rrn);
    				break;
    			
    			case "LI":
    				log.displayThis(request[0]);
    				mainFile.listByID();
    				break;
    			
    			case "DI":
    				rrn = Integer.parseInt(request[1]);
    				log.displayThis(request[0]+" "+request[1]);
    				mainFile.deleteOneRecord(rrn);
    				break;
    			
    			case "IN":
    				String[] split = request[1].split(",");
    				log.displayThis(request[0]+" "+request[1]);
    				mainFile.insertOneRecord();
    				break;
	
    		}
    		
    	}
    	log.displayThis("> UserApp completed");
        mainFile.FinishUp();
    	log.finishUp();
    
    }
    //*********************** PRIVATE METHODS ********************************  
}
