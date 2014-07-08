/**
 * PROJECT: PureMichigan         CLASS: MapData.java
 * AUTHOR:Ben Johnson
 * FILES ACCESSED: MichiganRoads.txt (input file), and indirectly accesses the log 
 * 				file through a bufferedWriter object that is passed into the 
 * 				constructor. 
 * INTERNAL INDEX STRUCTURE: Adjacency matrix   
 * FILE STRUCTURE: 
 * DESCRIPTION: Gets 2 city names from a input file, then using direct address 
 * 			stores the names in a name array then stores the edge weight (distance) 
 * 			in a adjacency matrix where the two intersect. 
 ***********************************************************************************/
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MapData {
	//**************************** PRIVATE DECLARATIONS ************************
	private int N;
	private int MAX_N=200;
	private int infinity=2147483647; 
	private String[] cityNameList;
	private int[][] map;
	private String[] upCityList;
	private BufferedWriter log;
	//**************************** PUBLIC CONSTRUCTOR **************************
	public MapData(BufferedWriter log)
	{
		this.log = log;
		cityNameList =  new String[MAX_N];
		N=0;
		
		map = new int[MAX_N][MAX_N];
		for(int row=0;row<map.length;row++)
		{
			for(int col = 0 ;col<map.length;col++)
			{
				if(row==col)
				{
					map[row][col]=0;
				}
				else
				{
					map[row][col]=infinity;
				}
			}
		}
	}
	//**************************** PUBLIC GETTERS AND SETTERS ******************
	
	/********************************* getCityNumber ***************************/
	public int getCityNumber(String cityName)
	{
		int cityNum = N-1;
		boolean found = false;
		
		while(cityNum>=0 && !found)
		{
			if(cityNameList[cityNum].equalsIgnoreCase(cityName))
			{
				found=true;
			}
			else
			{
				cityNum--;
			}
		}
		if(found)
		{
			return cityNum;
		}
		else
		{
			return -1;
		}	
	}
	/****************************** getCityPeninsula ***************************/
	public String getCityPeninsula(String cityName)
	{
		int temp = upCityList.length-1;
		boolean found = false;
		
		while(temp>=0 && !found)
		{
			if(upCityList[temp].equalsIgnoreCase(cityName))
			{
				found=true;
			}
			else
			{
				temp--;
			}
		}
		if(found)
		{
			return "UP";
		}
		else
		{
			return "LP";
		}	
	}
	/****************************** getCityName ***************************/
	public String getCityName(int cityNum)
	{
		return cityNameList[cityNum];
	}
	/****************************** getRoadDistance ***************************/
	public int getRoadDistance(int row, int col)
	{
		return map[row][col];
	}
	//**************************** PUBLIC SERVICE METHODS **********************
	
	/******************************** loadMapData ******************************/
	public int loadMapData(String mapFile) throws IOException
	{
		String line;
		String[] values;
		
		log.write("**Opened file "+mapFile+"\n");
		File inputFile = new File(mapFile);
		int x,y,position;
		
		Scanner input = new Scanner(inputFile);
		
		while(input.hasNextLine())
		{
			line = input.nextLine();
			if(line.startsWith("up(["))
			{
				line = line.substring(4, line.length()-3);
				upCityList = line.split(", ");
				
			}
			else if(line.startsWith("dist("))
			{
				line = line.substring(5, line.length()-2);
				values = line.split(", ");
				
				x = storeCityName(values[0].trim());
				y = storeCityName(values[1].trim());
				
				position = new Integer(values[2]);
				map[x][y] = position;
				map[y][x] = position;	
			}	
		}
		
		log.write("**Closed file "+mapFile+"\n");
		input.close();
		return N;
	}

	//**************************** PRIVATE METHODS *****************************
	
	/****************************** storeCityName ******************************/
	private int storeCityName(String cityName)
	{
		int nextOpen = N;
		boolean found = false;
		
		if(N==0)
		{
			found=true;
			cityNameList[N]= cityName;
			N++;
		}
		else
		{
			nextOpen--;
			while(!found && nextOpen >=0 )
			{
				if(cityNameList[nextOpen].equalsIgnoreCase(cityName))
				{
					found = true;				
				}
				else
				{
					nextOpen--;
				}
			}
		}
		if(found)
		{
			return nextOpen;
		}
		else
		{
			nextOpen = N;
			cityNameList[nextOpen]= cityName;
			N++;
			
			return nextOpen;
		}	
	} 
}
