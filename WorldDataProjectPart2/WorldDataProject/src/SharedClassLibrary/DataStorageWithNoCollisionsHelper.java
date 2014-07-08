/* PROJECT: WorldDataProject (Java)         CLASS: DataStorage
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED: 
 * 	input:MainData.txt 
 * 	output: UserInterFace object 
 * FILE STRUCTURE: See specs
 * DESCRIPTION: Constructor takes a parameter of UserInterFace for outputting 
 * 	to the logFile, and a boolean of isSetup to tell if it should overwrite 
 * 	the file or not.
*******************************************************************************/

package SharedClassLibrary;
import java.io.*;


public class DataStorage 
{
    //**************************** PRIVATE DECLARATIONS ************************
    private String fileName,collisionFileName; 
    private RandomAccessFile mainData,collisions;
    //private CollisionHandler collisions;
    private int headRecordCount=0;
    private int errors=0;
    private int deletedRecords=0;
    private int recordsLoaded=0;
    private final int sizeOfheaderRec=22+6;
    private MainDataRecord dataRecord;
    private int sizeOfLogicalDataRec= 3+17+12+4+2+8+4+4;//54
    private int sizeOfPhysicalRec = sizeOfLogicalDataRec + 2;//56
    private short MAX_N_HOMELOC;
    private short nHomeRec,nCollRec,homeRRN; 
    private int searchCount;
    
    private UserInterface log;
    
    //**************************** PUBLIC GET/SET METHODS **********************
    /*public void setMAX_N_HOMELOC(short max_N_HomeLoc)
    {
    	MAX_N_HOMELOC = max_N_HomeLoc;
    }
    */
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************

    public DataStorage(UserInterface log,boolean isSetup) throws IOException 
    {
    	MAX_N_HOMELOC = (short) 20;
    	
        if(isSetup)
        {
        	//Opening FileOutputStream with false as a parameter overwrites the file
        	FileOutputStream overWrite = new FileOutputStream("MainData.bin",false);
        	overWrite.close();
        	FileOutputStream collisionsOverWrite = new FileOutputStream("MainDataCollisions.bin",false);
        	collisionsOverWrite.close();
        }
        
    	dataRecord = new MainDataRecord();
        this.log = log;

    	collisionFileName = "MainDataCollisions.bin";
    	//collisionFileName = nameOfCollisionsFile;
		
    	collisions = new RandomAccessFile(collisionFileName, "rw");
    	log.displayThis("> Opened " + collisionFileName + " file");
    	//collisions = new CollisionHandler(collisionFileName,log);
    	
    	fileName = "MainData.bin";
    	mainData = new RandomAccessFile(fileName,"rw");
        	
        log.displayThis("> Opened " + fileName + " file");
        
        readHeaderRecord();
    }
    //**************************** PUBLIC SERVICE METHODS **********************
    
    
    /**************************** insertOneRecord *********************************/
    public void insertOneRecord() throws IOException
    {
    	log.displayThis("**SORRY: insert function not yet operational");
    }
    
    /**************************** listByID ****************************************/
    public void listByID() throws IOException
    {
    	log.displayThis("**SORRY: listById function not yet operational");
    }
	
    /**************************** queryByID *********************************/
    public void queryByID(int rrn) throws IOException
    {
    	log.displayThis("**SORRY: queryById function not yet operational");
    }
    
    /**************************** deleteByID ********************************/ 
    public void deleteByID(int rrn) throws IOException
    {
    	log.displayThis("**SORRY:  deleteByID function not yet operational");
    }
    public void deleteByCode(String code) throws IOException
    {
    	searchCount = 0;
    	homeRRN = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	dataRecord.setCountryCode(code);
    	int byteOffSet = calculateByteOffset(homeRRN);
    	
    	if(homeLocIsEmpty(mainData,homeRRN))
    	{
    		searchCount++;
    		log.displayThis("**ERROR: no country with code "+code.toUpperCase());
    		log.displayThis(String.format("    [%d]", searchCount));
    	}
    	else if(codesMatch(mainData,code,byteOffSet))
    	{
    		searchCount++;
    		deleteLogicalRecord(mainData,homeRRN);
    	}
    	
    	else
    	{
    		short pointer = readHeadPTR(homeRRN);
    		
    	}
      }

    public void queryByCode(String code) throws IOException
    {
    	searchCount = 0;
    	homeRRN = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	dataRecord.setCountryCode(code);
    	int byteOffSet = calculateByteOffset(homeRRN);
    	if(homeLocIsEmpty(mainData,homeRRN))
    	{
    		searchCount++;
    		log.displayThis("**ERROR: no country with code "+code.toUpperCase());
    		log.displayThis(String.format("    [%d]", searchCount));
    	}
    	else if(codesMatch(mainData,code,z))
    	{
    		searchCount++;
    		log.displayThis(read1Rec(mainData,homeRRN));
    		log.displayThis(String.format("    [%d]", searchCount));
    	}
    	
    	else
    	{
    		short pointer = readHeadPTR(homeRRN);
    		collisionsQueryByCode(pointer);
    	}
      }


   /************************  store1Country **********************************
    * Takes strings of fields and stores them into our MainDataRecord Object
    * depending on where there is open space in the file it stores it there.
    */
    public void store1Country( String countryCode, String name, String continent,String surfaceArea, 
    		String yearOfIndep, String population, String lifeExp, String gnp) throws IOException{
    	
    	dataRecord.setCountryCode(countryCode);
    	dataRecord.setName(name);
    	dataRecord.setContinent(continent);
    	dataRecord.setSurfaceArea(surfaceArea);
    	dataRecord.setYearOfIndep(yearOfIndep);
    	dataRecord.setPopulation(population);
    	dataRecord.setLifeExp(lifeExp);
    	dataRecord.setGNP(gnp);
    	
    	homeRRN = hashFunction(dataRecord.getCountryCode(), MAX_N_HOMELOC);
    	
    	dataRecord.setHomeRRN(homeRRN);
    	
    	
    	if (homeLocIsEmpty(mainData,homeRRN))
    	{
    		short link = -1;
    		writeOneRecord(mainData,homeRRN,link);
    		
    		nHomeRec++;
    		recordsLoaded++;
    	}
    	else
    	{
    		nCollRec++;
    		short headPTR = updateLink(homeRRN,nCollRec);
    		writeOneRecord(collisions,nCollRec,headPTR);
    		recordsLoaded++;
    	}
    }

    /****************************** finishUp **********************************/
    public void FinishUp() throws IOException
    {
    	writeHeaderRecord();
    	
    	log.displayThis(String.format("> DataStorage completed: %d " +
    			"records loaded correctly, %d errors, %d deleted, %d in home, %d in collisons", 
    			recordsLoaded,errors,deletedRecords, nHomeRec, nCollRec));
    	log.displayThis("> Closed "+collisionFileName +" file");	
    	collisions.close();
    	log.displayThis("> Closed "+fileName +" file");
    	mainData.close();    	
    }
    
    //**************************** PRIVATE METHODS *****************************
    private void collisionsQueryByCode(short pointer) throws IOException
    {
    	searchCount = 1;
		boolean recRead=false;
		
		String prettyRecord = new String();

		String code = new String(dataRecord.getCountryCode()); 
		
		while(pointer>-1 && !recRead)
		{
			int byteOffset = pointer*sizeOfPhysicalRec;
			if(codesMatch(collisions,code,byteOffset))
			{
				searchCount++;
	    		prettyRecord = read1Rec(collisions,pointer);
	    		recRead = true;
			}
			else
			{
				searchCount++;
				pointer = readHeadPTR(pointer);
			}
		}
		if(recRead)
		{
			log.displayThis(prettyRecord);
			log.displayThis(String.format("    [%d]", searchCount));
		}
		else
		{
			log.displayThis("**ERROR: no country with code "+code.toUpperCase());
			log.displayThis(String.format("    [%d]", searchCount));
		}
		
	}
    
    
    
	/************************** setHeaderRecord  **********************************
	* A private method that will read the first 3 bytes of MainData*.txt and store* 
	* it in memory as a integer                                                   *
	*******************************************************************************/
    private void readHeaderRecord() throws IOException
    {
		
    	byte[] byt = new byte[22];

    	mainData.seek(0);
    	mainData.read(byt);
    	String n = new String(byt);
    	if(!n.substring(0,2).equals("\0\0"))
    	{
    		mainData.seek(0);
    		nHomeRec = mainData.readShort();
    		nCollRec = mainData.readShort();
    		MAX_N_HOMELOC = mainData.readShort();
    		mainData.read(byt, 0, 22);
    		collisionFileName = new String(byt);
    	
    	
    	}
    }
	
    /**************************** homeLocEquals ****************************************
     *Takes in a country code, hashes it, pulls the code from that spot on the disk
     * and compares to see if they match
     */	
    private boolean homeLocEquals(String code) throws IOException
    {
    	short hashedCode = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	mainData.seek(calculateByteOffset(hashedCode));
    	
    	byte[] codeOnFile = new byte[3];
    	
    	
    	mainData.read(codeOnFile, 0, 3);
    	
    	String fromFile = new String(codeOnFile);
    	
    	if(code.equalsIgnoreCase(fromFile))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }
    
    private boolean codesMatch(RandomAccessFile file,String code,int byteOffset) throws IOException
    {
    	//short hashedCode = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	
    	file.seek(byteOffset);
    	
    	byte[] bytesOnFile = new byte[3];
    	
    	
    	file.read(bytesOnFile, 0, 3);
    	
    	String codeOnFile = new String(bytesOnFile);
    	
    	if(code.equalsIgnoreCase(codeOnFile))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }
    
    
    /**************************** read1Rec() *******************************
     * Takes in a already hashed country code finds that place on the disk 
     * and stores its fields to our MainDataRecord object, then displays it
     * in the log
     */
    private String read1Rec(RandomAccessFile file,short hashedCode) throws IOException
    {
    	
    	int byteOffSet = calculateByteOffset(hashedCode);
    	
    	file.seek(byteOffSet);
    	byte[] firstBytes = new byte[32];

    	file.read(firstBytes,0 , firstBytes.length);
    	
    	String firstChars = new String(firstBytes);     	
    	
    	dataRecord.setCountryCode(firstChars.substring(0, 3));
    	dataRecord.setName(firstChars.substring(3, 20));
    	dataRecord.setContinent(firstChars.substring(20, 32));
    	dataRecord.setSurfaceArea(file.readInt());
    	dataRecord.setYearOfIndep(file.readShort());
    	dataRecord.setPopulation(file.readLong());
    	dataRecord.setLifeExp(file.readFloat());
    	dataRecord.setGNP(file.readInt());
    	
    	//log.displayThis(dataRecord.prettyPrintRecord());
    	//log.displayThis(String.format("    [%d]", searchCount));
    	return dataRecord.prettyPrintRecord();

    }
    
    /*************************** updateLink  *******************************
     * Takes in a hashed record location and a new link to update that 
     * record with. It finds the link from the old record and stores it in 
     * memory, then it replaces that link with a new link.
     * last it returns the old link
     */
    private short updateLink(short oldRec,short newLink) throws IOException
    {
    	int byteOffset = calculateByteOffset(oldRec);
    	
    	mainData.seek(byteOffset+sizeOfLogicalDataRec);
    	
    	short headPTR = mainData.readShort();
    	
    	mainData.seek(byteOffset+sizeOfLogicalDataRec);
    	
    	mainData.writeShort(newLink);
    	
    	return headPTR;
    
    }
    
    
    private void deleteLogicalRecord(RandomAccessFile file,short hashedCode) throws IOException
    {
    	int byteOffSet = calculateByteOffset(hashedCode);
    	file.seek(byteOffSet);
    	char space = ' ';
    	for(int i=0; i < 32;i++)
    	{
    		file.write((byte)space);
    	}
    		
    	file.writeInt(0);
    	file.writeShort(0);
    	file.writeLong(0);
    	file.writeFloat(0);
    	file.writeInt(0);
    	log.displayThis(String.format("" ));
    	log.displayThis(String.format("    [%d]", searchCount));
    	
    }
    /**************************** writeOneRecord *************************************/
    private void writeOneRecord(RandomAccessFile file,short hashedCode,short link) throws IOException
    {  	
    	
    		
    	byte asciiLetter;
    	char[] codeNameContinet = dataRecord.getCode_Name_Continent();
    	int byteOffSet = calculateByteOffset(hashedCode);
    		
    	file.seek(byteOffSet);
    		
    	for(int i=0; i < 32;i++)
    	{
    		asciiLetter= (byte)codeNameContinet[i];
    		file.write(asciiLetter);
    	}
    		
    	file.writeInt(dataRecord.getSurfaceArea());
    	file.writeShort(dataRecord.getYearOfIndep());
    	file.writeLong(dataRecord.getPopulation());
    	file.writeFloat(dataRecord.getLifeExp());
    	file.writeInt(dataRecord.getGNP());
    	file.writeShort(link);

    }
    
    /**************************** homeLocIsEmpty *********************************
     * takes in a hashed Code and sees if there are any bytes written to that spot 
     */
    private boolean homeLocIsEmpty(RandomAccessFile file,short hashedCode) throws IOException
    {
    	byte[] firstByte = new byte[2]; 
    	int byteOffSet = calculateByteOffset(hashedCode);
    	
    	if(byteOffSet < 0)
    	{
    		return true;
    	}
    	else
    	{
    		file.seek(byteOffSet);
        	file.read(firstByte);
        	
        	
        	if(firstByte[0] == 0 && firstByte[1]==0)
        	{
        	 return true;	
        	} 
        	else
        	{
        	 return false;
        	}
    	
    	}
    	
    }
    /**************************** calculateByteOffset ******************************/
    private int calculateByteOffset(short hashedCode)
    {
    	return (sizeOfheaderRec + (hashedCode*sizeOfPhysicalRec));
    } 
    
    
    /***************************** writeHeaderRecord********************************/
    private void writeHeaderRecord() throws IOException
    {
    	mainData.seek(0); 
    	mainData.writeShort(nHomeRec);
    	mainData.writeShort(nCollRec);
    	mainData.writeShort(MAX_N_HOMELOC);
    	
		
		String hRecString = "MainDataCollisions.bin"; 
	
		mainData.write(hRecString.getBytes());
    }
   
    private short readHeadPTR(short hashedCode) throws IOException
    {
    	int byteOffset = calculateByteOffset(hashedCode);
    	
    	mainData.seek(byteOffset+sizeOfLogicalDataRec);
    	
    	short headPTR = mainData.readShort();
    	
    	return headPTR;
    	
    }
    
    public short hashFunction(char[] code, short Max)
    {
    	short homeRRN;
    	int multipliedTogether;
    	int remainder;
    	
    	for(int i=0;i<code.length;i++)
    	{
    		code[i]= Character.toUpperCase(code[i]);
    	}
    
    	multipliedTogether = code[0]*code[1]*code[2];
    	
    	remainder = multipliedTogether % Max;
    	
    	homeRRN=(short) (remainder+1); 
 
    	return homeRRN;
    }

    
}
