/* NOTE:  NOT USED IN ASGN 1
 * */

/* PROJECT: WorldDataProject (Java)         CLASS: MainData
 * AUTHOR:
 * FILES ACCESSED:
 * FILE STRUCTURE:
 * DESCRIPTION:
*******************************************************************************/

package SharedClassLibrary;
import java.io.*;

public class MainData 
{
    //**************************** PRIVATE DECLARATIONS ************************
    private String fileName = "MainData.txt"; 
    private RandomAccessFile mainData;
    private int rrn;
   // private int byteOffSet;
    private int headRecordCount=0;
    private final int sizeOfheaderRec=3;
    private int sizeOfDataRec=71;
    private String id, country , name , continent , region , surfaceArea, yearOfIndep , population , lifeExp;
    
    //**************************** PUBLIC GET/SET METHODS **********************
    public void setRRN(int r)
    {
    	rrn = r;
    }
    
    public void setId(String id)
    {
    	
    }
    
    public void setCountry(String cc)
    {
    	
    }
    
    public void setName(String na)
    {
    	
    }
    
    public void setContinent(String cont)
    {
    	
    }
    
    public void setRegion(String regi)
    {
    	
    }
    
    public void setSurfaceArea(String surf)
    {
    	
    }
    
    public void setYearOfIndep(String YOI)
    {
    	
    }
    
    public void setPopulation(String pop)
    {
    	
    }
    
    public void setLifeExp(String LE)
    {
    	
    }
    
    //**************************** PUBLIC CONSTRUCTOR(S) ***********************
    public MainData(String readOrWrite) throws IOException {
        System.out.println("OK, MainData object created");
        if(readOrWrite == "r")
        {
        	mainData = new RandomAccessFile(fileName,"r");
        }
        if(readOrWrite == "rw")
        {
        	mainData = new RandomAccessFile(fileName,"rw");
        	
        }
        System.out.println("opened " + fileName + "in *" + readOrWrite + "* mode" );
    }
    //**************************** PUBLIC SERVICE METHODS **********************
    
    public String readOneRec() throws IOException
    {
    	
    	int byteOffSet = calculateByteOffset(rrn);
    	
    	mainData.seek(byteOffSet);
    	byte[] fixedLenRec = new byte[sizeOfDataRec];
    	
    	
    	mainData.read(fixedLenRec,0 , sizeOfDataRec);
    	
    	String line = new String(fixedLenRec);
    	
    	return line;
    	
    	//mainData.rea
    }
    
    
    
    
    
    
    public void store1Country( String id, String countryCode, String name, String continent, String region,String surfaceArea, String yearOfIndep, String population, String lifeExp) throws IOException{
    	
    	this.rrn = Integer.parseInt(id);
    	
    	
    	
    	this.id = padZerosOnLeft(id, 3);
    	
    	this.country = countryCode.trim();
    	
    	this.name = padSpacesOnRight(name, 17);
    	
    	this.continent = padSpacesOnRight(continent, 11);
    	
    	this.region = padSpacesOnRight(region, 10);
    	
    	this.surfaceArea = padZerosOnLeft(surfaceArea, 8);
    	
    	if(yearOfIndep.startsWith("-"))
    	{
    		this.yearOfIndep = padSpacesOnRight(yearOfIndep,5);
    	}
    	else
    	{
    		this.yearOfIndep = padZerosOnLeft(yearOfIndep, 5);
    	}
    	
    	this.population = padZerosOnLeft(population, 10);
    	
    	this.lifeExp = padZerosOnLeft(lifeExp, 4);
    	
    	System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s %n",this.id,this.country,this.name,this.continent,this.region,this.surfaceArea,this.yearOfIndep,this.population,this.lifeExp);
    	
    	String aLine = this.id + this.country + this.name + this.continent + this.region + this.surfaceArea + this.yearOfIndep + this.population + this.lifeExp;
    	
    	mapFixedLecRec(rrn, aLine);
    }

    //**************************** PRIVATE METHODS *****************************
    
    
   
    
    
    private String padZerosOnLeft(String str , int n)
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
    	
    	return str;
    	
    }
    
    private String padSpacesOnRight(String str, int n) //custom function to pad Strings
    {
    	//trimming to remove any extra spaces on the left or right.
    	str.trim();
    	
    	//adding the correct amount of white space
    	str = String.format("%1$-" + n + "s", str);
        
    	//trimming of
    	str = str.substring(0 , n);
    	
    	return str;
    }
    
    
    
    private void mapFixedLecRec(int rrn,String lineToMap) throws IOException
    {
    	
    	//System.out.println(lineToMap.getBytes().length);
    	
    	byte[] linesToWrite = new byte[sizeOfDataRec];
    	
    	//System.out.println(aLine.length());
    	linesToWrite = lineToMap.getBytes();
    	//System.out.println(linesToWrite.length);
    	
    	int byteOffSet = calculateByteOffset(rrn);
    	
    	headRecordCount++;
    	String hRecCount = String.valueOf(headRecordCount);
    	hRecCount = padZerosOnLeft(hRecCount, 3);
    	
    	mainData.seek(0);
    	
    	mainData.write(hRecCount.getBytes());
    	
    	mainData.seek(byteOffSet);
//    	
    	mainData.write(linesToWrite,0, sizeOfDataRec);
    	
    	//mainData.seek(0);
    	
    	//System.out.println(mainData.readLine());
    	
    }
    
    private int calculateByteOffset(int rrn)
    {
    	return sizeOfheaderRec + ((rrn-1)*sizeOfDataRec);
    } 
//    private int rrnToInt(String id)
//    {
//    	Integer rrn = new Integer(id);
//    	this.rrn = rrn;
//    	return rrn;
//    }
   
}
