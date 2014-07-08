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
    private RandomAccessFile mainData;
    private CollisionHandler collisions;
    private MainDataRecord dataRecord;
    
    private int errors=0;
    private int deletedRecords=0;
    private int recordsLoaded=0;
    
    private final int sizeOfheaderRec=22+6;
    private int sizeOfLogicalDataRec= 3+17+12+4+2+8+4+4;//54
    private int sizeOfPhysicalRec = sizeOfLogicalDataRec + 2;//56
    private final short MAX_N_HOMELOC;
    private short nHomeRec,nCollRec,homeRRN; 
    private int searchCount;
    
    private UserInterface log;
    
    //**************************** PUBLIC GET/SET METHODS **********************


    //**************************** PUBLIC CONSTRUCTOR(S) ***********************

    public DataStorage(UserInterface log,boolean isSetup) throws IOException 
    {
    	
    	
        if(!isSetup)
        {
        	fileName = "MainDataA2.bin";
        	mainData = new RandomAccessFile(fileName,"rw");
        	log.displayThis("> Opened " + fileName + " file");
        	MAX_N_HOMELOC=readHeaderRecord();
        }
        else
        {
        	MAX_N_HOMELOC = (short) 20;
        	
        	//Opening FileOutputStream with false as a parameter overwrites the file
        	FileOutputStream overWrite = new FileOutputStream("MainDataA2.bin",false);
        	overWrite.close();
        	FileOutputStream collisionsOverWrite = new FileOutputStream("MainDataCollisions.bin",false);
        	collisionsOverWrite.close();
        	
        }
    	
        dataRecord = new MainDataRecord();
        this.log = log;

    	collisionFileName = "MainDataCollisions.bin";
    	collisions = new CollisionHandler(collisionFileName,log);
    	
    	fileName = "MainDataA2.bin";
    	mainData = new RandomAccessFile(fileName,"rw");
        	
        log.displayThis("> Opened " + fileName + " file");
        
        
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
    /****************************** deleteByCode ***************************/
    public void deleteByCode(String code) throws IOException
    {
    	searchCount = 0;
    	homeRRN = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	dataRecord.setCountryCode(code);
    	short pointer = readHeadPTR(homeRRN);
    	if(homeLocIsEmpty(homeRRN))
    	{
    		searchCount++;
    		log.displayThis("**ERROR: no country with code "+code.toUpperCase());
    		log.displayThis(String.format("    [%d]", searchCount));
    	}
    	
    	else if(homeLocEquals(code))
    	{
    		
    		searchCount++;
    		
    		dataRecord.setTombStone();
    		
    		short link = readHeadPTR(homeRRN);
    		
    		writeOneRecord(homeRRN, link);
    	}
    	
    	else
    	{
    		
    		collisions.deleteByCode(dataRecord, pointer);
    		    		
    	}
      }

    /****************************** queryByCode ***************************/
    public void queryByCode(String code) throws IOException
    {
    	searchCount = 0;
    	homeRRN = hashFunction(code.toCharArray(), MAX_N_HOMELOC);
    	dataRecord.setCountryCode(code);
    	
    	short pointer = readHeadPTR(homeRRN);
    	
    	if(homeLocIsEmpty(homeRRN))
    	{
    		searchCount++;
    		log.displayThis("**ERROR: no country with code "+code.toUpperCase());
    		log.displayThis(String.format("    [%d]", searchCount));
    	}
    	
    	else if(homeLocEquals(code))
    	{
    		searchCount++;
    		read1Rec(homeRRN);
    	}
    	
    	else
    	{
    		collisions.queryByCode(dataRecord, pointer);
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
    	
    	
    	if (homeLocIsEmpty(homeRRN))
    	{
    		short link = -1;
    		writeOneRecord(homeRRN,link);
    		
    		nHomeRec++;
    		recordsLoaded++;
    	}
    	else
    	{
    		nCollRec++;
    		short headPTR = updateLink(homeRRN,nCollRec);
    		collisions.storeOneCountry(dataRecord,nCollRec,headPTR);
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
    	collisions.finsishUp();
    	log.displayThis("> Closed "+fileName +" file");
    	mainData.close();    	
    }
    
    //**************************** PRIVATE METHODS *****************************
    
	/************************** readHeaderRecord  *********************************
	* A private method that will read the header record of MainData*.txt and store* 
	* all but one of the values to memory. It returns MAX_N_HOME_LOC so we can    *
	* declare it a final short                                                    *
	*******************************************************************************/
    private short readHeaderRecord() throws IOException
    {
    	short tempMAX_N;
    	
    	byte[] byt = new byte[22];

    	mainData.seek(0);
    	mainData.read(byt);
    	//String n = new String(byt);
    	/*if(!n.substring(0,2).equals("\0\0"))
    	{*/
    	mainData.seek(0);
    	nHomeRec = mainData.readShort();
    	nCollRec = mainData.readShort();
    	tempMAX_N= mainData.readShort();
    	mainData.read(byt, 0, 22);
    	collisionFileName = new String(byt);
    	
    	//by returning a t to the constructor, we can declare MAX_N_HOME_LOC as a final short
    	return tempMAX_N;

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
    
    
    /**************************** read1Rec() *******************************
     * Takes in a already hashed country code finds that place on the disk 
     * and stores its fields to our MainDataRecord object, then displays it
     * in the log
     */
    private void read1Rec(short hashedCode) throws IOException
    {
    	int byteOffSet = calculateByteOffset(hashedCode);
    	
    	mainData.seek(byteOffSet);
    	byte[] firstBytes = new byte[32];

    	mainData.read(firstBytes,0 , firstBytes.length);
    	
    	String firstChars = new String(firstBytes);     	
    	
    	dataRecord.setCountryCode(firstChars.substring(0, 3));
    	dataRecord.setName(firstChars.substring(3, 20));
    	dataRecord.setContinent(firstChars.substring(20, 32));
    	dataRecord.setSurfaceArea(mainData.readInt());
    	dataRecord.setYearOfIndep(mainData.readShort());
    	dataRecord.setPopulation(mainData.readLong());
    	dataRecord.setLifeExp(mainData.readFloat());
    	dataRecord.setGNP(mainData.readInt());
    	
    	log.displayThis(dataRecord.prettyPrintRecord());
    	log.displayThis(String.format("    [%d]", searchCount));

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
    
    /**************************** writeOneRecord *************************************/
    private void writeOneRecord(short hashedCode,short link) throws IOException
    {  	

    		
    	byte asciiLetter;
    	char[] codeNameContinet = dataRecord.getCode_Name_Continent();
    	int byteOffSet = calculateByteOffset(hashedCode);
    		
    	mainData.seek(byteOffSet);
    		
    	for(int i=0; i < 32;i++)
    	{
    		asciiLetter= (byte)codeNameContinet[i];
    		mainData.write(asciiLetter);
    	}
    		
    	mainData.writeInt(dataRecord.getSurfaceArea());
    	mainData.writeShort(dataRecord.getYearOfIndep());
    	mainData.writeLong(dataRecord.getPopulation());
    	mainData.writeFloat(dataRecord.getLifeExp());
    	mainData.writeInt(dataRecord.getGNP());
    	mainData.writeShort(link);

    }
    
    /**************************** homeLocIsEmpty *********************************
     * takes in a hashed Code and sees if there are any bytes written to that spot 
     */
    private boolean homeLocIsEmpty(short hashedCode) throws IOException
    {
    	byte[] firstByte = new byte[2]; 
    	int byteOffSet = calculateByteOffset(hashedCode);
    	
    	if(byteOffSet < 0)
    	{
    		return true;
    	}
    	else
    	{
    		mainData.seek(byteOffSet);
        	mainData.read(firstByte);
        	
        	
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
   
    /****************************** readHeadPTR ***************************/
    private short readHeadPTR(short hashedCode) 
    {
    	short headPTR;
    	try{
    		int byteOffset = calculateByteOffset(hashedCode);
    	
    		mainData.seek(byteOffset+sizeOfLogicalDataRec);
    	
    		headPTR = mainData.readShort();
    	}
    	catch(IOException e){
    		headPTR = -1;
    	}
    	return headPTR;
    }
    
    /****************************** hashFunction ***************************/
    private short hashFunction(char[] code, short Max)
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