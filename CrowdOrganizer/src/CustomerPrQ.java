/**
* PROJECT: CrowdOrganizer     CLASS: CustomerPrQ
* AUTHOR: Benjamin Johnson
* FILES ACCESSED: LineAt6Am.csv (or what ever file name is passed into the setupPrQ
* 	 method)
* INTERNAL INDEX STRUCTURE: A minimum heap stored in a array of heapNodes             
* FILE STRUCTURE: 
* DESCRIPTION: This Class will create a minimum heap using the heap insert method, 
* 	it also has the appropriate methods to insert and delete from the heap using walk
* 	up and walk down to correct issues.
***********************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CustomerPrQ
{
	//**************************** PRIVATE DECLARATIONS ************************
	private BufferedWriter log;
	private int N;
	private final int maxN=50;
	private HeapNode[] heap;
	private int initialPriorityValue = 100;
	private int priorityValue;
	
	//**************************** PUBLIC CONSTRUCTOR **************************
	public CustomerPrQ(BufferedWriter log)
	{
		this.log = log;
		heap = new HeapNode[maxN];
		N = 0;
		
	}
	//**************************** PUBLIC SERVICE METHODS **********************
	
	/****************************** emptyPrq ***************************/
	public void emptyPrq() throws IOException
	{
		log.write(String.format("**heap currently has %d nodes remaining \n", N));
		
		while(!heapEmpty())
		{
			log.write("SERVE CUSTOMER: ");
			heapDelete();
		}
		log.write("**heap now empty \n");
				
	}
	
	/****************************** insertNewCustomer ***************************/
	public void insertNewCustomer(String[] info) throws IOException
	{
		String[] points = new String[4];
		
		for(int i=0;i<4;i++)
		{
			points[i] = info[i+1];
		}
		
		determinePriorityValue(points);
		log.write(String.format("%s (%d) \n", points[0],priorityValue));
		heapInsert(points[0], priorityValue);
		
	}
	public void serveAcustomer() throws IOException
	{
		heapDelete();
	}
	
	/****************************** setupPrQ ***************************/
	public void setupPrQ(String filename) throws IOException
	{
		
		File file = new File(filename);
		Scanner input = new Scanner(file);
		
		log.write(String.format("**Opened file %s \n", filename));
		
		String line;
		String[] info;
		
		while(input.hasNextLine())
		{
			line = input.nextLine();
			
			if(!line.startsWith("//"))
			{
				info = line.split(",");
				
				determinePriorityValue(info);
				
				heapInsert(info[0], priorityValue);
			}
		}
		
		log.write(String.format("**initial heap built containing %d nodes \n", N));
		log.write(String.format("**Closed file %s \n", filename));
		
		input.close();	
	}
	//**************************** PRIVATE METHODS *****************************
	
	/****************************** heapDelete *********************************/
	private void heapDelete() throws IOException
	{
		log.write(String.format("%s (%d) \n",heap[0].getName(),heap[0].getPriorityValue()));
		heap[0] = heap[N-1];	
		N--;
		
		walkDown(0);	
	}
	
	/****************************** heapInsert ***************************/
	private void heapInsert(String value, int pv)
	{
		HeapNode newNode = new HeapNode(value, pv);
		
		heap[N] = newNode;
		N++;
		
		walkUp(N-1);
		
	}
	
	/****************************** walkDown ***************************/
	private void walkDown(int i)
	{
		int smallestChild = getSmallestChild(i);
		
		HeapNode temp = heap[i];
		
		while((2*i+1) <= (N-1) && (temp.getPriorityValue() > heap[smallestChild].getPriorityValue()))
		{
			heap[i] = heap[smallestChild];
			
			i = smallestChild;
			
			smallestChild = getSmallestChild(i);
		}
		heap[i] = temp;		
		
	}

	/****************************** walkUp ***************************/
	private void walkUp(int i)
	{
		
		HeapNode temp=heap[i];
		while(i > 0 && temp.getPriorityValue() < heap[(i-1)/2].getPriorityValue())
		{
		
				heap[i] = heap[(i-1)/2];
				i = (i-1)/2;
			
		}
		heap[i] = temp;
	}
	
	/****************************** determinePriorityValue **********************/
	private void determinePriorityValue(String[] points) throws IOException
	{
		int age = new Integer(points[3]);
		priorityValue = ++initialPriorityValue;
		
		
		for(int i = 1; i<3;i++)
		{
			switch (points[i].toLowerCase()) 
			{
				case "employee":
					priorityValue-=25;
					
					break;
				case "owner":
					priorityValue-=80;
					break;
				case "vip":
					priorityValue-=5;
					break;
				case "supervip":
					priorityValue-=10;
					break;
				
			}
		}
		if(age >= 65)
		{
			priorityValue-= 15;
			
		}
		
		if(age >=80)
		{
			priorityValue-= 15;
		}

	}
	
	/****************************** heapEmpty ***************************/
	private boolean heapEmpty()
	{
		if(N==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/****************************** getSmallestChild ***************************/
	private int getSmallestChild(int i)
	{
		if((2*i+2) > (N-1) || heap[2*i+1].getPriorityValue() <= heap[2*i+2].getPriorityValue())
		{
			return (2*i)+1;
		}
		else
		{
			return (2*i)+2;
		}
	}
}
