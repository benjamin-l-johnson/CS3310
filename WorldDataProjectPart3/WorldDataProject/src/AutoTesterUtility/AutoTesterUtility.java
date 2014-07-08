


/* PROJECT:  WorldDataProject (Java)            PROGRAM: AutoTesterUtility
 * AUTHOR:  Benjamin Johnson
 * PROGRAMS ACCESSED:  SetupProgram, UserApp, PrettyPrintUtility
 * OOP CLASSES USED:  none - this program is just a developer utility program
 *      and doesn't use the OOP paradigm
 * FILES ACCESSED:  4 output files from this project (using suffix for *):
 *      LOG.txt, MainData*.txt, 
 * DESCRIPTION:  Utility program which aids developer to automate testing of the
 *      project with various test data sets.  It deletes output files from
 *      previous runs and executes the 3 programs with various test files
 *      (and N's) as parameters when calling those programs' Main methods.
 ******************************************************************************/
package AutoTesterUtility;

import java.io.*;

import PrettyPrintUtility.PrettyPrintUtility;
import SetupProgram.SetupProgram;
import ShowFilesUtility.ShowFilesUtility;
import UserApp.UserApp;

public class AutoTesterUtility {

    public static void main(String[] args) throws IOException, ClassNotFoundException {


        String dataFileSuffix[]  = {"A3"};
        String transFileSuffix[] = {"A3"};
        //String nRecToShow[]      = {"All",    "60"  };

        //Delete the SINGLE output Log.txt file (if it exists)
        DeleteFile("LOG.txt");

        for (int i = 0; i < dataFileSuffix.length; i++) {
            
        	//Delete 3 other output files (if they exist)
             //DeleteFile("MainData.txt");
             DeleteFile("LOG.txt");
             DeleteFile("IndexBackup.bin");
            //DeleteFile("NameIndexBackup" + dataFileSuffix[i] + ".bin");
            //DeleteFile("NameIndexBackup" + dataFileSuffix[i] + ".bin");
            
            //Run the other 3 programs
            SetupProgram.main(new String[]{dataFileSuffix[i]});
        	PrettyPrintUtility.main(new String[]{dataFileSuffix[i]});
        	UserApp.main(new String[]{dataFileSuffix[i],transFileSuffix[i]});
        	PrettyPrintUtility.main(new String[]{dataFileSuffix[i]});
           
		    //If dataFileSuffix = "" don't run the ShowFilesUtility
		    //if(!dataFileSuffix[i].equals(""))
            //{
            	
		    	//PrettyPrintUtility.main(new String[]{dataFileSuffix[i]});

             //}
             //else
             //{
            	//UserApp.main(new String[]{dataFileSuffix[i],transFileSuffix[i]});
            	//PrettyPrintUtility.main(new String[]{dataFileSuffix[i]});
             //}


        }
        
    }
    //**************************** PRIVATE METHOD ******************************
    private static boolean DeleteFile(String fileName) {
        boolean delete = false;
        File f = new File(fileName);
        if (f.exists()) {
            delete = f.delete();
        }
        return delete;
    }  
}
