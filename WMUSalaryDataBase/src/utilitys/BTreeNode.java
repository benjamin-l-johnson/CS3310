/**
* PROJECT: WMUsalary         CLASS: BTreeNode.java
* AUTHOR:bigbadben
* FILES ACCESSED:
* INTERNAL INDEX STRUCTURE:               
* FILE STRUCTURE: 
* DESCRIPTION: 
***********************************************************************************/
package utilitys;

/**
 * @author bigbadben
 *
 */
public class BTreeNode {
	private int M =100;
	private int N=0;
	private int[] TP = new int[100];
	private int[] KV = new int[99];
	private int[] DRP= new int[99];
	
	public BTreeNode()
	{
		for(int i=0;i<TP.length;i++)
		{
			TP[i]=-1;
		}
		for(int i=0;i<TP.length;i++)
		{
			KV[i]=-1;
		}
		for(int i=0;i<TP.length;i++)
		{
			DRP[i]=-1;
		}
	}
	public int[] getTP()
	{
		return TP;
	}
	public int[] getKV()
	{
		return KV;
	}

}
