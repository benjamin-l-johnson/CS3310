/* PROJECT: WorldDataProject (Java)         CLASS: CollisionHandler
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED: MainDataCollisions.txt 
 * 	input: UserInterFace object
 * 	output: UserInterFace object 
 * FILE STRUCTURE: See specs
 * DESCRIPTION: Constructor takes a parameter of UserInterFace for outputting 
 * 	to the logFile, this class acts as a handler class for all data collisions
 *  in MainData. This class deals with writing, reading, and searching for 
 *  Collison Records getting information from MainDataRecord object that is 
 *  passed in with every public service method.
 * SEARCHING:
 *  It searches for records in the same synonym family linearly, using similar
 *  attributes as to a linked list (pointer, null pointer, etc).
*******************************************************************************/
package SharedClassLibrary;


import java.io.IOException;
import java.io.RandomAccessFile;

public class CollisionHandler {
	
	//**************************** PRIVATE DECLARATIONS ************************
	private String collisionFileName; 
    private RandomAccessFile collisions;
    private int sizeOfLogicalDataRec= 3+17+12+4+2+8+4+4;//54
    private int sizeOfPhysicalRec = sizeOfLogicalDataRec + 2;//56
    private MainDataRecord record;
    private UserInterface log;
    private int searchCount;
	
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
	public CollisionHandler(String nameOfCollisionsFile,UserInterface log) throws IOException
	{
		this.log = log;
		collisionFileName = nameOfCollisionsFile;
		collisions = new RandomAccessFile(collisionFileName, "rw");
		log.displayThis("> Opened " + collisionFileName + " file");

	}
	//**************************** PUBLIC SERVICE METHODS **********************
	
	/****************************** queryByCode ***************************/
	public boolean queryByCode(MainDataRecord record,short pointer) throws IOException
	{
		//searchCount =1 because we already searched 1 location in the home record
		searchCount = 1;
		this.record = record;

		String code = new String(record.getCountryCode()); 
		
		if(searchCode(code, pointer))
		{//when seachCode finds a match it stores the RRN in the MainDataRecord object field
	    	
			read1Rec(record.getHomeRRN());
	    	return true;
		}
	    else
		{
	    	log.displayThis("**ERROR: no country with code "+code.toUpperCase());
			log.displayThis(String.format("    [%d]", searchCount));
			return false;
		}
	}
	
	/****************************** deleteByCode **************************
	 * Returns a boolean to see if you actually deleted a code or not. 
	 * */
	public boolean deleteByCode(MainDataRecord record,short pointer) throws IOException
	{
		//searchCount =1 because we already searched 1 location in the home record 
		searchCount = 1;
		this.record = record;
		String code = new String(record.getCountryCode()); 
		
		if(searchCode(code, pointer))
		{
			record.setTombStone();
			short link = readHeadPTR(record.getHomeRRN());
	    	writeOneRecord(record.getHomeRRN(),link);
	    	log.displayThis(String.format(" OK, %s deleted",code));
	    	log.displayThis(String.format("    [%d]", searchCount));
	    	return true;
		}
	    else
		{	    
	    	log.displayThis("**ERROR: no country with code "+code.toUpperCase());
			log.displayThis(String.format("    [%d]", searchCount));
			return false;
		}
	}
	
	
	/****************************** finsishUp ***************************/
	public void finsishUp() throws IOException
	{
		log.displayThis("> Closed "+collisionFileName + " File");
		collisions.close();
	}

	/****************************** read1Rec ***************************/
	private void read1Rec(short pointer) throws IOException
    {
    	int byteOffSet = pointer*sizeOfPhysicalRec;
    	collisions.seek(byteOffSet);
    	
    	//reading all char bytes into a string and splitting it up from there.
    	byte[] firstBytes = new byte[32];
    	collisions.read(firstBytes,0 , firstBytes.length);
    	
    	String firstChars = new String(firstBytes);     	
    	
    	//setting variables for our MainDataRecord object
    	record.setCountryCode(firstChars.substring(0, 3));
    	record.setName(firstChars.substring(3, 20));
    	record.setContinent(firstChars.substring(20, 32));
    	
    	record.setSurfaceArea(collisions.readInt());
    	record.setYearOfIndep(collisions.readShort());
    	record.setPopulation(collisions.readLong());
    	record.setLifeExp(collisions.readFloat());
    	record.setGNP(collisions.readInt());
    	
    	log.displayThis(record.prettyPrintRecord());
    	log.displayThis(String.format("    [%d]", searchCount));
    }

	/****************************** storeOneCountry ***************************/
	public void storeOneCountry(MainDataRecord record,short rrn,short link) throws IOException
	{
		this.record = record;

		writeOneRecord(rrn, link);
	}

	/****************************** writeOneRecord **************************
	 * takes in a RRN to map the spot it will write at. Also takes a link to 
	 * write onto the back of that record.
	 * */
	public void writeOneRecord(short rrn,short link) throws IOException
	{
		byte asciiLetter;
		
		char[] codeNameContinet = record.getCode_Name_Continent();
		
		int byteOffSet = sizeOfPhysicalRec * rrn;
						
		collisions.seek(byteOffSet);
			
		for(int i=0; i < 32;i++)
		{
			asciiLetter= (byte)codeNameContinet[i];
			collisions.write(asciiLetter);
		}

		collisions.writeInt(record.getSurfaceArea());
		collisions.writeShort(record.getYearOfIndep());
		collisions.writeLong(record.getPopulation());
		collisions.writeFloat(record.getLifeExp());
		collisions.writeInt(record.getGNP());
		collisions.writeShort(link);
				
	}
    //**************************** PRIVATE METHODS *****************************
	
	/****************************** searchCode ***************************/
	private boolean searchCode(String code,short pointer) throws IOException
	{
		//acts as a flag
		boolean containsCode = false;
		byte[] codeOnFile = new byte[3];		
		String fromFile;

		while(pointer>-1 && !containsCode)
		{
			searchCount++;
			
			collisions.seek(pointer*sizeOfPhysicalRec);
	    	collisions.read(codeOnFile, 0, 3);
	    	fromFile = new String(codeOnFile);
	    	
	    	if(code.equalsIgnoreCase(fromFile))
	    	{
	    		containsCode = true;
	    	}
	    	    	
			else
	    	{
				//the pointer gets the value of the next pointer on the list
				pointer = readHeadPTR(pointer);
	    	}
		}
		if(containsCode)
		{
			//we found a record lets set its homeRRN in memory for everyone to use
			record.setHomeRRN(pointer);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/****************************** readHeadPTR ***************************/
	private short readHeadPTR(short rrn) 
	{
		short headPTR;
		
		try{
			
		int byteOffSet = sizeOfPhysicalRec * rrn; 
    	collisions.seek(byteOffSet+sizeOfLogicalDataRec);
    	headPTR = collisions.readShort();
    	 
		}
		
		//if we hit EOF set the pointer to null so the loop will terminate 
		catch (IOException  e)
		{
			headPTR = -1;
		}
    	return headPTR;
	}
}
