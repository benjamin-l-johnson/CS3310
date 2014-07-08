package utilitys;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataBase {

	private String fileName;
	private RandomAccessFile data;
	private int rrn;
	private int headRecordCount=0;
	private int errors=0;
	private int N=0;
	private int deletedRecords=0;
	private int recordsLoaded=0;
	private final int sizeOfheaderRec=3;
	private String recordInMem;
	private int sizeOfDataRec=110;
	private int sizeOfPhysicalData=118;
	private char[] name,title,location,period;
	private double salary;
	private Log log;

	public DataBase(Log log,boolean isSetup) throws IOException
	{
		if(isSetup)
		{
			//Opening FileOutputStream with false as a parameter overwrites the file
			FileOutputStream overWrite = new FileOutputStream("Data.bin",false);
			overWrite.close();
		}

		this.log = log;

		fileName = "Data.bin";

		data = new RandomAccessFile(fileName,"rw");

		log.displayThis("> Opened " + fileName + " file");


	}
	
	public void listByLastName() throws IOException
    {
            int count = 0;          
            data.seek(3);
            
            log.displayThis(String.format("  %-30s %-30s  %-30s %-10s %-10s  ", "Name","Department","Position Title","Salary" ,"Period"));
            
            for(int i=0;i<N+1;i++)
            {
            	recordInMem=read1Rec();
                    if(!recordInMem.substring(0,2).equals("\0\0"))
                    {
                            count++;                    
                            log.displayThis(prettyPrintRecordInMem());
                            
                    }
            }
            log.displayThis(" + + + + + + + + + + + + + + + + + + + END OF DATA - "+count+" " +
                            "countries + + + + + + + + + + + + + + + + + +");
    }

	public int store1Rec( int id, String name, String title, String location, String period,String salary) throws IOException, ParseException
	{
		N++;
		this.rrn  = id;
		this.name = String.format("%-30s", name).toCharArray();
		int nameBytes = this.name.
		this.title = String.format("%-30s", title).toCharArray();
		this.location = String.format("%-30s", location).toCharArray();
		this.period = String.format("%-20s", period).toCharArray();
		
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		
		
		
		this.salary = format.parse(salary).doubleValue();

		writeOneRecord(rrn);
		
		return id;
	}
	
	/****************************** finishUp **********************************/
	public void FinishUp() throws IOException
	{
		//writeHeaderRecord();
		log.displayThis(String.format("> DataStorage completed: %d " +
				"records loaded correctly, %d errors, %d deleted", recordsLoaded,errors,deletedRecords));

		log.displayThis("> Closed "+fileName +" file");
		data.close();
	}
	
	//**************************** PRIVATE METHODS *****************************

	private void writeOneRecord(int rrn) throws IOException
	{
		//converting all the chars to a string



		String lineToMap = new String(name)+ new String(location)+ new String(title) + new String(period);

		if (isEmpty(rrn))
		{

			byte[] linesToWrite = new byte[sizeOfDataRec];

			char[] charArry = new char[sizeOfDataRec];

			//converting back to a charArray to write to file
			charArry=lineToMap.toCharArray();

			for(int i=0;i<linesToWrite.length;i++)
			{
				//turning the chars into bytes
				linesToWrite[i]= (byte) charArry[i];
			}

			long byteOffSet = calculateByteOffset(rrn);

			data.seek(byteOffSet);
						
			data.write(linesToWrite,0, sizeOfDataRec);
			
			data.writeDouble(salary);
			
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
	private String read1Rec() throws IOException
	{

		byte[] fixedLenRec = new byte[sizeOfDataRec];

		
		int bytesIn = data.read(fixedLenRec,0 , sizeOfDataRec);
		
		salary = data.readDouble();
		
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
		long byteOffSet = calculateByteOffset(rrn);

		data.seek(byteOffSet);
		byte[] fixedLenRec = new byte[sizeOfDataRec];


		data.read(fixedLenRec,0 , sizeOfDataRec);
		salary = data.readDouble();
		
		recordInMem = new String(fixedLenRec);


	}


	/************************** prettyPrintRecordInMem ***************************
     * Takes the record currently stored in memory, and returns a string that     *
     * is split up with a proper neatly format                                    *
     ******************************************************************************/
	private String prettyPrintRecordInMem()
	{
         
         
         String line = String.format("  %-30s %-30s  %-30s %,.2f   %-20s  ",
                         
                         recordInMem.substring(0, 30),recordInMem.substring(30,60),recordInMem.substring(60,90), salary,
                         
                         recordInMem.substring(90,110));
         
         return line;
	}
	/**************************** isEmpty ******************************************
	 * takes in a rrn sees if there are any bytes written to that spot              *
	 * *****************************************************************************/
	private boolean isEmpty(int rrn) throws IOException
	{
		byte[] firstByte = new byte[2];
		long byteOffSet = calculateByteOffset(rrn);

		if(byteOffSet < 0)
		{
			return true;
		}
		else
		{
			data.seek(byteOffSet);
			data.read(firstByte);
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
	
	private long calculateByteOffset(int rrn)
	{
		return sizeOfheaderRec+(rrn*sizeOfPhysicalData);
	}
	
	


}
