/**
* PROJECT: CrowdOrganizer     CLASS: CrowdOrganizerApp
* AUTHOR: Benjamin Johnson
* FILES ACCESSED: Log.txt, Events.txt and passes in the name of LineAt6Am.csv
*               
* DESCRIPTION: Uses sequential stream processing, reading in one event from
* 	Events.txt then calling the appropriate method from the CustomerPrQ class
***********************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class CrowdOrganizerApp {

	public static void main(String[] args) throws IOException 
	{		
		String logName = "Log.txt";
		String fileName = "LineAt6Am.csv";
		
		BufferedWriter log = new BufferedWriter(new FileWriter(logName));
		log.write("**Opened file "+ logName +"\n");
		
		File events = new File("Events.txt");		
		Scanner input = new Scanner(events);
		log.write("**Opened file "+events.getName() + "\n");
		
		String[] line;
		CustomerPrQ prq = new CustomerPrQ(log);
				
		while(input.hasNextLine())
		{
			
			String hold = input.nextLine();
			
			if(!hold.startsWith("//")){
				line = hold.split(",");			
				switch (line[0].toLowerCase()) 
				{
			
					case "openstore":
						log.write("STORE OPENING \n");
						prq.setupPrQ(fileName);
						break;
					case "serveacustomer":
						log.write("SERVE CUSTOMER: ");
						prq.serveAcustomer();
					
						break;
					case "newcustomer":
						log.write("NEW CUSTOMER: ");
						prq.insertNewCustomer(line);
						break;
					case "closestore":
						log.write("STORE CLOSING------------------------ \n");
						prq.emptyPrq();
						break;
				}
			}
		}
		log.write("**Crowd Organizer App Stopping \n");
		log.write("**Closing file "+ events.getName() +"\n");
		input.close();
		
		log.write("**Closing file "+ logName +"\n");
		log.close();
	}

}
