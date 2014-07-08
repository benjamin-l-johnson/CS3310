/* PROJECT: WorldDataProject (Java)         CLASS: BSTNode
 * AUTHOR: Benjamin Johnson
 * FILES ACCESSED: None
 * DESCRIPTION: BSTNode is a simple object with getters and setters to hold
 * 			a Binary Search Tree node. It holds a string value, shorts for 
 * 			child pointers, and short value for a Data Record pointer.
 ******************************************************************************/

package SharedClassLibrary;

public class BSTNode
{
    //**************************** PRIVATE DECLARATIONS ************************
    private short leftChildPointer=-1;
    private short rightChildPointer=-1;
    private short drp;
    private String keyValue;
    
    //**************************** PUBLIC GET/SET METHODS **********************
    
    /****************************** getLeftChildPointer ***************************/
    public short getLeftChildPointer()
    {
    	return leftChildPointer;
    }
    /****************************** setLeftChildPointer ***************************/
    public void setLeftChildPointer(short lcp)
	{
		leftChildPointer = lcp;
	}
	/****************************** getRightChildPointer ***************************/
	public short getRightChildPointer() 
	{
		return rightChildPointer;
	}
	/****************************** setRightChildPointer ***************************/
	public void setRightChildPointer(short rcp)
	{
		rightChildPointer = rcp;
	}
	/****************************** getDrp ***************************/
	public short getDrp()
	{
		return drp;
	}
	/****************************** getKeyValue ***************************/
	public String getKeyValue() 
	{
		return keyValue;
	}   
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public BSTNode(String keyVal ,short drp_)
    {
    	keyValue = keyVal;
    	drp = drp_;	
    }
}
