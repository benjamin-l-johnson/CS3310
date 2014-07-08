package setup;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import utilitys.*;

public class Setup {

	public static void main(String[] args) throws IOException, ParseException {
		
		DeleteFile("Log.txt");
			Log log = new Log(true);
			
			InputData inFile = new InputData(log);
			
			DataBase mainBase = new DataBase(log, true);
			
			while(!inFile.noMoreNames())
			{
				inFile.readOneRecord();
				
				mainBase.store1Rec(inFile.getID(), inFile.getName(), inFile.getTitle(), inFile.getLocation()
						,inFile.getPeriod(),inFile.getSalary());
				
			}
			mainBase.listByLastName();
			inFile.finishUp();
			mainBase.FinishUp();
			log.finishUp();
	}
	
	private static boolean DeleteFile(String fileName) {
        boolean delete = false;
        File f = new File(fileName);
        if (f.exists()) {
            delete = f.delete();
        }
        return delete;
    }  

}
