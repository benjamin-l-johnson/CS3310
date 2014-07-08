package utilitys;

import java.io.*;

import javax.swing.text.DefaultEditorKit.InsertTabAction;



public class Btree {
	public class Node
	{
		int KV;
		int DRP;
		int LTP;
		int RTP;
		public Node(int kv, int dr)
		{
			
		}
		public void getDRP(){}
		
	}
	
	private int M=40;
	private int[] TP = new int[M];
	private int[] KV = new int[M-1];
	private int[] DRP= new int[M-1];
	private int[] fromFileTP = new int[M];
	private int[] fromFileKV = new int[M-1];
	private int[] fromFileDRP = new int[M-1];
	private int[] bigTP = new int[M+1];
	private int[] bigKV = new int[M];
	private int[] bigDRP= new int[M];
	private int large = Integer.MAX_VALUE;
	private int rootPTR=0;
	private int N=1;
	private int sizeOfNode=((M-1)*(12))+4;
	
	
	public Btree() throws IOException
	{
		File btree = new File("BTree.bin");
		
		RandomAccessFile data = new RandomAccessFile(btree,"rw");
		
	}
	public void delete()
	{
		
	}
	public void insert(int kv, int drp)
	{
		if(rootPTR ==0)
		{
			
			growTree(kv, drp,-1);
			
		}
		else
		{
			getCorrectLeaf(kv);
			insertOnBigNode(kv, drp);
			if(bigKV[M-1] != large)
			{
				
			}
		}
	}
	
	public void finishUp()
	{
		
	}
	private void theBidSplit()
	{
		
	}
	private void getCorrectLeaf(int kv)
	{
		
		int[] stack= new int[M];
		int i=0;
		int stackCount=0;
		readNode(rootPTR);
		stack[stackCount]=rootPTR;
		
		while(fromFileTP[0]!=-1)
		{
			if(kv<fromFileKV[i])
			{
				stack[++stackCount] = fromFileTP[i];
				readNode(fromFileTP[i]);
				i=0;
			}
			else if(kv == fromFileKV[i])
			{
				System.out.println("There are equal saliereis");
				System.exit(0);
			}
			else
			{
				i++;
			}
		}
		makeBigNode();
		bigKV[M-1]=large;
		
		
	}
	
	private void insertOnBigNode(int kv,int drp)
	{
		int insert=0;
		int i=0;
		
		while(kv<fromFileKV[insert])
		{
			insert++;
		}
		i=insert;
		bigDRP[i]=drp;
		bigKV[i] =kv;
		bigTP[i]=-1;
		
		while(i<bigDRP.length)
		{
			bigDRP[i+1]=fromFileDRP[i];
			bigKV[i+1] =fromFileKV[i];
			bigTP[i+1]=fromFileTP[i];
			i++;
		}
		
	}
	private void makeBigNode()
	{
		for(int i=0;i<fromFileDRP.length;i++)
		{
			bigDRP[i]=fromFileDRP[i];
			bigKV[i] =fromFileKV[i];
			bigTP[i]=fromFileTP[i];
		}
		bigTP[fromFileTP.length-1]=fromFileTP[fromFileTP.length-1];
		
	}
	private void writeNode(int rrn)
	{
		
	}
	
	
	/****************************** growTree *************************
	 * Completed
	 * 
	 * Setup new arrays for storage, put data in new node, write the 
	 * node hang node on tree (rootptr=N). 
	 *****************************************************************/
	private void growTree(int newKV, int newDRP, int newTP)
	{
		setUpArrays();
		KV[0] = newKV;
		DRP[0] = newDRP;
		TP[0] = rootPTR;
		TP[1] = newTP;
		
		writeNode(N);
		rootPTR=N;
		N++;
		
	}
	
	/****************************** readNode ************************
	 *Just read in from the binary file  
	 *
	 *****************************************************************/
	private void readNode(int rrn)
	{
		
	}
	
	private int calculateOffset(int rrn)
	{
		return (sizeOfNode*rrn);
	}
	
	private void setUpArrays()
	{
		for(int i=0;i<TP.length;i++)
		{
			TP[i]=-1;
		}
		for(int i=0;i<KV.length;i++)
		{
			KV[i]=large;
		}
		for(int i=0;i<DRP.length;i++)
		{
			DRP[i]=-1;
		}
		
	}
}
