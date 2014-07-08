
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
    private String fileName; 
    private RandomAccessFile mainData;
    private int rrn;
    private int headRecordCount=0;
    private int errors=0;
    private int deletedRecords=0;
    private int recordsLoaded=0;
    private final int sizeOfheaderRec=3;
    private String recordInMem;
    private int sizeOfDataRec=71;
    private char[] id, country, name , continent , region , surfaceArea, yearOfIndep , population , lifeExp;
    private UserInterface log;
    
    //**************************** PUBLIC GET/SET METHODS **********************

    
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public DataStorage(UserInterface log_,boolean isSetup) throws IOException {
       
        if(isSetup)
        {
        	//Opening FileOutputStream with false as a parameter overwrites the file
        	FileOutputStream overWrite = new FileOutputStream("MainData.txt",false);
        	overWrite.close();
        }

    	log = log_;
    	
    	
    	fileName = "MainData.txt";

        mainData = new RandomAccessFile(fileName,"rw");
        	
        log.displayThis("> Opened " + fileName + " file");
        
        setHeaderRecord();
    }
    //**************************** PUBLIC SERVICE METHODS **********************
    
	//Not a functioning operation yet
	public void insertOneRecord() throws IOException
    {
    	log.displayThis("**SORRY: insert function not yet operational");
    }
    
    
    /**************************** listByID ****************************************/
	
    public void listByID() throws IOException
    {
    	int count = 0;  	
    	mainData.seek(3);
    	
    	log.displayThis("  ID  CODE NAME               CONTINENT     REGION        AREA       " +
    			"INDEP  POPULATION      L.EXP");
    	
    	while(!((recordInMem = read1Rec()).equals("-1")))
    	{
    		
    		if(!recordInMem.substring(0,2).equals("\0\0"))
    		{
    			count++;    		
    			log.displayThis(prettyPrintRecordInMem());
    			
    		}
    	}
    	log.displayThis(" + + + + + + + + + + + + + + + + + + + END OF DATA - "+count+" " +
    			"countries + + + + + + + + + + + + + + + + + +");
    }
	
	 /**************************** queryByID *********************************/
    public void queryByID(int rrn) throws IOException
    {

    	if(isEmpty(rrn))
    	{
    		log.displayThis("**ERROR: no country with id "+rrn);
    	}
    	else
    	{  	
    	 read1Rec(rrn);
    	 log.displayThis(prettyPrintRecordInMem());
    	}    	
    }
	/**************************** deleteOneRecord *****************************/
    public void deleteOneRecord(int rrn) throws IOException
    {
    	
    	
    	if(isEmpty(rrn))
    	{
    		log.displayThis(String.format("**ERROR: no country with id %d", rrn));
    	}
    	else
    	{
    		read1Rec(rrn);
    		String oldRec = recordInMem.substring(6,23);
    		byte[] nullBytes = {0,0};
    		mainData.seek(calculateByteOffset(rrn));
    		mainData.write(nullBytes);
    		headRecordCount--;
    		deletedRecords++;
    		
			log.displayThis(String.format("Ok, %s deleted",oldRec));
    	}
    	
    }
    
	/************************  store1Country **********************************/
    public void store1Country( String id, String countryCode, String name, String continent, String region,String surfaceArea, 
    		String yearOfIndep, String population, String lifeExp) throws IOException{
    	
    	this.rrn = Integer.parseInt(id);

    	this.id = padZerosOnLeft(id, 3);
    	
    	this.country = countryCode.trim().toCharArray();
    	
    	this.name = padSpacesOnRight(name, 17);
    	
    	this.continent = padSpacesOnRight(continent, 11);
    	
    	this.region = padSpacesOnRight(region, 10);
    	
    	this.surfaceArea = padZerosOnLeft(surfaceArea, 8);
    	
    	if(yearOfIndep.startsWith("-"))
    	{
    		this.yearOfIndep = padSpacesOnRight(yearOfIndep,5);
    	}
    	else if(yearOfIndep.contains("NULL"))
    	{
    		yearOfIndep = "";
    		this.yearOfIndep = padZerosOnLeft(yearOfIndep, 5);
    	}
    	else
    	{
    		this.yearOfIndep = padZerosOnLeft(yearOfIndep, 5);
    	}
    	
    	this.population = padZerosOnLeft(population, 10);
    	
    	
    	if(lifeExp.contains("NULL"))
    	{
    		lifeExp = "";
    		this.lifeExp = padZerosOnLeft(lifeExp, 4);
    	}
    	else
    	{
    		this.lifeExp = padZerosOnLeft(lifeExp, 4);
    	}
    	
    	writeOneRecord(rrn);
    }
    /****************************** finishUp **********************************/
    public void FinishUp() throws IOException
    {
    	writeHeaderRecord();
    	log.displayThis(String.format("> DataStorage completed: %d " +
    			"records loaded correctly, %d errors, %d deleted", recordsLoaded,errors,deletedRecords));
    		
    	log.displayThis("> Closed "+fileName +" file");
    	mainData.close();    	
    }
    
    //**************************** PRIVATE METHODS *****************************
    
	/************************** setHeaderRecord  **********************************
	* A private method that will read the first 3 bytes of MainData*.txt and store* 
	* it in memory as a integer                                                   *
	*******************************************************************************/
	private void setHeaderRecord() throws IOException
    {
    	byte[] b = new byte[3];

    	mainData.seek(0);
    	mainData.read(b);
    	String n = new String(b);
    	if(!n.substring(0,2).equals("\0\0"))
    	{
    	 headRecordCount = Integer.valueOf(n);
    	 recordsLoaded = headRecordCount;
    	}
    }
	
    /************************** prettyPrintRecordInMem ***************************
	* Takes the record currently stored in memory, and returns a string that     *
	* is split up with a proper neatly format                                    *
	******************************************************************************/
    private String prettyPrintRecordInMem()
    {
    	int area = Integer.parseInt(recordInMem.substring(44,52));
    	
    	int Indep = Integer.parseInt(recordInMem.substring(52,57).trim());
    	    	
    	String indepStr = String.format("%04d", Indep);
    	
    	float lifeExp = Float.parseFloat(recordInMem.substring(67,71));

    	int pop = Integer.parseInt(recordInMem.substring(57,67));
    	
    	String line = String.format("  %s %s  %s  %s   %s   %,10d  %5s  %,13d   %.1f",
    			
    			recordInMem.substring(0, 3),recordInMem.substring(3,6),recordInMem.substring(6,23),
    			
    			recordInMem.substring(23,34),recordInMem.substring(34,44),area,
    			
    			indepStr,pop,lifeExp);
    	
    	return line;
    }
    /*************************** padSpacesOnRight *********************************
     * Takes in a string and number of spaces you want on the right and returns it*
     * as a char array                                                            *
     ******************************************************************************/
    private char[] padSpacesOnRight(String str, int n) 
    {
    	str.trim();
    	    	
    	str = String.format("%1$-" + n + "s", str);
         	
    	str = str.substring(0 , n);
    	
    	return str.toCharArray();
    }
    /*************************** padZerosOnLeft ***********************************
     * Takes in a string and number of zeros you want on the left and returns it  *
     * as a char array                                                            *
     ******************************************************************************/
    private char[] padZerosOnLeft(String str , int n)
    {
    	
    	String zero = "0";
    	   	
    	str.trim();
    	
    	if(str.length()>n)
    	{
    		str = str.substring(0,n);
    	
    	}
    	else
    	{	
    		while(str.length()<n)
        	{
        	    str = zero+str;
        	}	
    	}
    	
    	return str.toCharArray();
    }
   
    /**************************** read1Rec() ***************************************/
    private String read1Rec() throws IOException
    {

    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	
    	
    	int bytesIn = mainData.read(fixedLenRec,0 , sizeOfDataRec);
    	
    	if(bytesIn== -1)
    	{
    		return "-1";
    	}
    	else{
    	String line = new String(fixedLenRec);
    	
    	return line;
    	}
    	
    }
    /**************************** read1Rec(int rrn) ********************************/   
    private void read1Rec(int rrn) throws IOException
    {
    	int byteOffSet = calculateByteOffset(rrn);
    	
    	mainData.seek(byteOffSet);
    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	
    	
    	mainData.read(fixedLenRec,0 , sizeOfDataRec);
    	
    	recordInMem = new String(fixedLenRec);
    	
    	
    }
    
    /**************************** buildStringToMap **********************************
     * Takes the char[]'s of the fields in memory and returns them as a string      * 
     ********************************************************************************/
    private String buildStringToMap()
    {
    	
    	
    	String line = new String (id) + 
    			      new String (country) +
    				  new String (name) +
    				  new String (continent) +
    				  new String (region) +
    				  new String (surfaceArea) +
    				  new String (yearOfIndep) +
    				  new String (population) +
    				  new String (lifeExp );
    				  
 
    			
    			
    	
    	return line;
    }
    /**************************** writeOneRecord ************************************/
    
    private void writeOneRecord(int rrn) throws IOException
    {  	
    		//converting all the chars to a string
    		String lineToMap = buildStringToMap();
    		
    		if (isEmpty(rrn))
    		{
    		
    			byte[] linesToWrite = new byte[sizeOfDataRec];
        	
    			char[] charArry = new char[71];
        	
    			//converting back to a charArray to write to file
    			charArry=lineToMap.toCharArray();
        	
    			for(int i=0;i<linesToWrite.length;i++)
    			{
    				//turning the chars into bytes
        			linesToWrite[i]= (byte) charArry[i];
    			}
        	
    			int byteOffSet = calculateByteOffset(rrn);
    	
    			mainData.seek(byteOffSet);

    			mainData.write(linesToWrite,0, sizeOfDataRec);
    	
    			headRecordCount++;
    			recordsLoaded++;
    		}
    		else
    		
    		{
    			errors++;
    			read1Rec(rrn);
    			String formatedStr = String.format("**ERROR: %s not inserted (id %d is %s)", 
    					lineToMap.substring(6, 23),rrn,recordInMem.substring(6,23));
    			
    			log.displayThis(formatedStr);
    		}

    	
    }
    
    /**************************** isEmpty ******************************************
     * takes in a rrn sees if there are any bytes written to that spot              *
     * *****************************************************************************/
    private boolean isEmpty(int rrn) throws IOException
    {
    	byte[] firstByte = new byte[2]; 
    	int byteOffSet = calculateByteOffset(rrn);
    	
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
    /**************************** calculateByteOffset ******************************
     * takes in a rrn uses the fixed byte and file lengths to return a calculated  *
     * byteOffSet                                                                  *
     * *****************************************************************************/
    private int calculateByteOffset(int rrn)
    {
    	return sizeOfheaderRec + ((rrn-1)*sizeOfDataRec);
    } 
    /***************************** writeHeaderRecord********************************/
    private void writeHeaderRecord() throws IOException
    {
    	mainData.seek(0);        
		
		String hRecString = String.format("%03d", headRecordCount);

		mainData.write(hRecString.getBytes());
    }
   
}
