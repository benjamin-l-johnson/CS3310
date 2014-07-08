/* PROJECT: WorldDataProject (Java)         CLASS: NameIndex
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED:
 *                 LOG.txt        (through the UserInterface Object)
 *                 MainDataA3.txt (through the mainData object created only if called by UserApp)
 *                 NameIndex.bin
 * INTERNAL INDEX STRUCTURE:
 *                 Array of BSTNodes
 * FILE STRUCTURE: NameIndex.bin is a sequential file written out in the same 
 * 			order as the BSTNode array
 * DESCRIPTION: Using a binary tree search it hangs a BSTNode into the correct 
 * 		place on the tree, it then querys for a name value using a similar BST
 * 		search comparing name values as it goes along. See spec's for more details 
 ***********************************************************************************/
package SharedClassLibrary;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import SharedClassLibrary.UserInterface;
import SharedClassLibrary.BSTNode;

public class NameIndex
{
	//**************************** PRIVATE DECLARATIONS ************************
	private BSTNode[] nodeArray;
	private short n;
	private short rootPtr;
	private UserInterface log;
	private boolean fromSetup;
	private MainData mainData;
	private int maxPossibleN = 300;
	//**************************** PUBLIC GET/SET METHODS **********************


	//**************************** PUBLIC CONSTRUCTOR ***********************
	public NameIndex(UserInterface log_, boolean setup) throws ClassNotFoundException, IOException
	{
		log = log_;
		fromSetup = setup;

		if(fromSetup)
		{
			nodeArray = new BSTNode[maxPossibleN];
			rootPtr=-1;
			n=0;
		}
		else
		{
			mainData= new MainData(log_);
			readBackup();
		}
	}
	//**************************** PUBLIC SERVICE METHODS **********************

	/****************************** queryByName ***************************/
	public void queryByName(String keyVal) throws IOException
	{
		String cn = keyVal;
		keyVal = keyVal.trim().toUpperCase();
		short searchCount=0;
		short i = rootPtr;
		boolean found = false;
		int compareVaule;

		while(i>-1 && !found)
		{
			searchCount++;
			
			compareVaule = keyVal.compareTo(nodeArray[i].getKeyValue());

			if(compareVaule == 0)
			{
				found = true;
			}
			else if(compareVaule < 1)
			{
				i = nodeArray[i].getLeftChildPointer();
			}
			else
			{
				i = nodeArray[i].getRightChildPointer();
			}
		}
		if(found)
		{
			mainData.getThisData(nodeArray[i].getDrp());
			log.displayThis(String.format("  [%d BST nodes visited]", searchCount));
		}
		else
		{
			log.displayThis(String.format("**ERROR: no country named %s", cn));
			log.displayThis(String.format("  [%d BST nodes visited]", searchCount));
		}
	}

	/****************************** listAllByName ***************************/
	public void listAllByName() throws IOException
	{
		log.displayThis("CODE NAME--------------  CONTINENT----  ---POPULATION  L.EXP");
		inOrderTraversal(rootPtr);
		log.displayThis("+ + + + + + + + + END OF DATA -"+n+" countries + + + + + + +");
	}

	/****************************** insertOneCountry ***************************/
	public void insertOneCountry(String keyVal, String drp_)
	{
		short drp = Short.parseShort(drp_);
		keyVal = keyVal.trim().toUpperCase();

		if(rootPtr == -1)
		{
			rootPtr = n;
			nodeArray[n] = new BSTNode(keyVal, drp);
			n++;
		}
		else
		{
			nodeArray[n] = new BSTNode(keyVal, drp);
			boolean leftPtrFlag = false;
			short i = rootPtr;
			int parentI = i;
			int compareVaule;

			while(i!=-1)
			{
				parentI = i;
				compareVaule = keyVal.compareTo(nodeArray[i].getKeyValue());
				if(compareVaule < 1)
				{
					i = nodeArray[i].getLeftChildPointer();
					leftPtrFlag = true;
				}
				else
				{
					i = nodeArray[i].getRightChildPointer();
					leftPtrFlag=false;
				}
			}
			if(leftPtrFlag)
			{
				nodeArray[parentI].setLeftChildPointer(n);
			}
			else
			{
				nodeArray[parentI].setRightChildPointer(n);
			}
			n++;
		}
	}
	/****************************** finishUp ***************************/
	public void finishUp() throws IOException
	{
		if(fromSetup)
		{
			createBackup();
		}
		else
		{
			mainData.FinishUp();
		}
	}
	//**************************** PRIVATE METHODS *****************************

	/****************************** inOrderTraverSal ***************************/
	private void inOrderTraversal(short nextPtr) throws IOException
	{
		if(nextPtr!=-1)
		{
			inOrderTraversal(nodeArray[nextPtr].getLeftChildPointer());

			mainData.getThisData(nodeArray[nextPtr].getDrp());

			inOrderTraversal(nodeArray[nextPtr].getRightChildPointer());
		}
	}
	/****************************** readBackup **************************/
	private void readBackup() throws IOException
	{
		short lcp,drp;
		String kv;
		InputStream file = new FileInputStream("IndexBackup.bin");
		log.displayThis("> Opened IndexBackup.bin file");
		DataInputStream indexBackup = new DataInputStream(file);

		rootPtr = indexBackup.readShort();
		n = indexBackup.readShort();

		nodeArray = new BSTNode[n];

		for(int i =0; i < n ;i++)
		{
			lcp = indexBackup.readShort();
			kv = indexBackup.readUTF();
			drp = indexBackup.readShort();
			nodeArray[i] = new BSTNode(kv, drp);
			nodeArray[i].setLeftChildPointer(lcp);
			nodeArray[i].setRightChildPointer(indexBackup.readShort());
		}

		log.displayThis("> Closed IndexBackup.bin file");
		indexBackup.close();
	}
	 /****************************** createBackup ***********************
	 * This writes a short for LCP, then a UTF string (ie. a java style * 
	 * string) then a short for drp and short for RCP in that order.    *
	 * *****************************************************************/
	private void createBackup() throws IOException
	{
		FileOutputStream fs = new FileOutputStream("IndexBackup.bin");
		log.displayThis("> Opened IndexBackup.bin file");
		DataOutputStream indexBackup = new DataOutputStream(fs);

		indexBackup.writeShort(rootPtr);
		indexBackup.writeShort(n);
		for(int i =0; i < n ;i++)
		{
			indexBackup.writeShort(nodeArray[i].getLeftChildPointer());
			indexBackup.writeUTF(nodeArray[i].getKeyValue());
			indexBackup.writeShort(nodeArray[i].getDrp());
			indexBackup.writeShort(nodeArray[i].getRightChildPointer());
		}
		log.displayThis("> Closed IndexBackup.bin file");
		indexBackup.close();
	}
}