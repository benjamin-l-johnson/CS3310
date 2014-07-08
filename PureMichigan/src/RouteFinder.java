/**
 * PROJECT: PureMichigan         CLASS: RouteFinder.java
 * AUTHOR:Ben Johnson
 * FILES ACCESSED: Indirectly accesses the log file through a bufferedWriter
 * 				object that is passed into the constructor. 
 * INTERNAL INDEX STRUCTURE: 3 working arrays, 2 of all ints and one of booleans. All
 * 				size N      
 * FILE STRUCTURE: 
 * DESCRIPTION: given two numbers from an adjacency matrix it will find the shortest
 * 			path between the two, 
 ***********************************************************************************/

import java.io.BufferedWriter;
import java.io.IOException;

public class RouteFinder {
	//**************************** PRIVATE DECLARATIONS ************************
	private boolean[] include;
	private int[] distance;
	private int[] predecessor;
	private int N;
	private int infinity=2147483647;
	private int edgeWeight;
	private MapData map;
	private BufferedWriter log;
	int shortLen=0;
	
	//**************************** PUBLIC CONSTRUCTOR **************************
	public RouteFinder(BufferedWriter log, MapData map,int N)
	{
		this.log = log;
		this.map = map;
		this.N = N;
		
		include = new boolean[N];
		distance = new int[N];
		predecessor= new int[N];
	}
	
	//**************************** PUBLIC SERVICE METHODS **********************
	public	void findShortestRoute(int start, int end) throws IOException
	{
		initialiazeArrays(start);
		searchForPath(start, end);
		
		log.write("\n\n");
		
		String list="";
		
		shortLen = distance[end];
		list=reportAnswers(list,end);
		
		log.write("TOTAL DISTANCE:  "+shortLen+" miles\n");
		log.write("SHORTEST ROUTE:  "+list+"\n");
	}
	
	//**************************** PRIVATE METHODS *****************************
	
	/****************************** initialiazeArrays ***************************/
	private void initialiazeArrays(int start)
	{
		for(int i=0; i<N;i++)
		{
			distance[i]=map.getRoadDistance(start, i);
			if(distance[i] == 0 || distance[i] == infinity)
			{
				predecessor[i]=-1;
			}
			else
			{
				predecessor[i]=start;
			}
			include[i]=false;
		}
		include[start]=true;
	}
	
	/****************************** searchForPath ***************************/
	private void searchForPath(int start, int end) throws IOException
	{
		log.write(String.format("%s ",map.getCityName(start)));
		while(!include[end])
		{
			int target = getMinDistance();
			log.write(String.format("%s ",map.getCityName(target)));
			include[target]=true;
			for(int i=0;i<N;i++)
			{
				if(!include[i] && vaildEdgeWeight(target, i))
				{
					if((distance[target]+edgeWeight) < distance[i])
					{
						distance[i] = distance[target] + edgeWeight;
						predecessor[i] = target;
					}	
				}
			}
		}
	}
	
	/****************************** reportAnswers ***************************/
	private String reportAnswers(String list, int dest) throws IOException
	{
		if(predecessor[dest]==-1)
		{
			list = list+map.getCityName(dest);
		}
		else
		{
			list = reportAnswers(list,predecessor[dest]);
			list = list + (" > "+map.getCityName(dest));
		}
		return list;
	}
	
	/****************************** vaildEdgeWeight ***************************/
	private boolean vaildEdgeWeight(int target, int i)
	{
		edgeWeight = map.getRoadDistance(target, i);
		
		if(edgeWeight ==0 || edgeWeight == infinity)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/****************************** getMinDistance ***************************/
	private int getMinDistance()
	{
		int min=infinity;
		int subScript = 0;
		for(int i=0;i<N;i++)
		{
			if((distance[i]!=0 || distance[i]!=infinity)&& !include[i])
			{
				if(distance[i]<min)
				{
					min=distance[i];
					subScript=i;
				}
			}
		}
		return subScript;
	}
}
