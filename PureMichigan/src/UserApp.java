/**
 * PROJECT: PureMichigan         CLASS: UserApp.java
 * AUTHOR:Ben Johnson
 * FILES ACCESSED: log.txt, CityParis.txt(input file)
 * INTERNAL INDEX STRUCTURE:               
 * FILE STRUCTURE: 
 * DESCRIPTION: Main controller program, processes input using the sequential stream 
 * 			processing algorithm 
 ***********************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserApp {
	public static void main(String[] args) throws IOException {
		String logName = "log.txt";
		String fileName = "CityPairs.txt";
		File file = new File(fileName);
				
		BufferedWriter log = new BufferedWriter(new FileWriter(logName));
		log.write("**Opened file "+ logName +"\n");
				
		MapData map = new MapData(log);
		
		RouteFinder gps = new RouteFinder(log, map, map.loadMapData("MichiganRoads.txt"));
		Scanner input = new Scanner(file);
		log.write("**Opened file "+file.getName() + "\n\n");

		int start,dest;
		String startInUP,destInUP;
			
		while(input.hasNextLine())
		{
			String[] cityPair = input.nextLine().split(" ");
			
			if(!cityPair[0].startsWith("//"))
			{
				startInUP = map.getCityPeninsula(cityPair[0]);
				destInUP = map.getCityPeninsula(cityPair[1]);
				
				start = map.getCityNumber(cityPair[0]);
				dest = map.getCityNumber(cityPair[1]);
				
				//printing line of "%" 138 chars long
				log.write(String.format("%138s", "\n\n").replace(' ', '%'));
				log.write(String.format("START:  %s",cityPair[0]));
							
				if(start==-1)
				{
					log.write(String.format("\nERROR:  start city not in Michigan Map Data \n","%"));

				}
				else if(dest == -1)
				{
					log.write(String.format(" (%d) %s",start, startInUP+"\n"));									
					log.write(String.format("DESTINATION:  %s \n",cityPair[1]));
					log.write(String.format("ERROR:  destination city not in Michigan Map Data \n","%"));
				}
				else
				{
					if(startInUP.equals(destInUP))
					{
						log.write(String.format(" (%d) %s",start, startInUP+"\n" ));
						log.write(String.format("DESTINATION:  %s (%d) %s\n",cityPair[1],dest,destInUP+"\n"));
						log.write("TRACE OF TARGETS: ");
						gps.findShortestRoute(start, dest);							
					}
					else
					{
						log.write(String.format(" (%d) %s",start, startInUP+"\n" ));
						log.write(String.format("DESTINATION:  %s (%d) %s\n",cityPair[1],dest,destInUP));
						log.write("[***** 2 different peninsulas, so 2 partial routes *****]\n\n");
						
						log.write(String.format("TRACE OF TARGETS: "));
						gps.findShortestRoute(start, map.getCityNumber("theBridge"));
						
						log.write("\nTRACE OF TARGETS: ");
						gps.findShortestRoute(map.getCityNumber("theBridge"),dest);
						
					}	
				}
			}
		}
		log.write(String.format("%138s", "\n\n").replace(' ', '%'));
		log.write("**Closed file "+file.getName() + "\n");
		input.close();
		log.write("**Closed file "+ logName +"\n");
		log.close();
	}
}
