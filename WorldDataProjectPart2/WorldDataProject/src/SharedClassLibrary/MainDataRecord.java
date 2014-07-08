/* PROJECT: WorldDataProject (Java)         CLASS: MainDataRecord
 * AUTHOR:Benjamin Johnson
 * FILES ACCESSED:  NONE
 * 	input:Takes input from DataStorage  
 * DESCRIPTION: Constructor takes a parameter of UserInterFace for outputting 
 * 	to the logFile, and a boolean of isSetup to tell if it should overwrite 
 * 	the file or not.*/

package SharedClassLibrary;

public class MainDataRecord {
	private short yearOfIndep,homeRRN; 
    private long population;
    private int surfaceArea, gnp;
    private float lifeExp;
    private char[] country, name , continent;
     
  //**************************** PUBLIC SET METHODS **********************
    
    /****************************************************************************
     * Some of the set methods are overloaded to accept both numbers and strings* 
     * and are specified with #overloaded                                       *
     ****************************************************************************/
        
    /******************* setTombStone ***************************/
    public void setTombStone()
    {
    	String space = " ";
    	country = String.format("%3s",space ).toCharArray();
    	name = String.format("%12s",space ).toCharArray();
    	continent = String.format("%17s",space ).toCharArray();
    	surfaceArea = 0;
    	yearOfIndep = 0;
    	population = 0;
    	lifeExp = 0;
    	gnp = 0;   	
    }
    
    /****************************** setHomeRRN **************************
    *
    */
    public void setHomeRRN(short home)
    {
    	homeRRN = home;
    }
    /****************************** setCountryCode **************************
    *
    */
    public void setCountryCode(String cc)
    {
    	country = cc.trim().toCharArray();
    }
    
    /****************************** setName *********************************
    *
    */
    public void setName(String name_)
    {
    	name = padSpacesOnRight(name_, 17);
    }
    
    /****************************** setContinent ****************************
    *
    */
    public void setContinent(String cont)
    {
    	continent = padSpacesOnRight(cont, 12);
    }
    
    /****************************************************************************
     * The rest of the set methods are overloaded to accept both numbers and    * 
     * strings                                                                  *
     ****************************************************************************/
    
    /****************************** setSurfaceArea **************************
    *takes int
    */
    public void setSurfaceArea(int surface)
    {
    	surfaceArea = surface;
    }
    /****************************** setSurfaceArea **************************
     * takes String
     * */
    public void setSurfaceArea(String surface)
    {
    	surfaceArea = Integer.parseInt(surface);
    }
    /****************************** setYearOfIndep **************************
    *takes Short
    */
    public void setYearOfIndep(short year)
    {
    	yearOfIndep = year;
    }
    /****************************** setYearOfIndep ***************************/
    public void setYearOfIndep(String year)
    {
    	yearOfIndep = Short.parseShort(year);
    }
    /****************************** setPopulation ***************************
    *
    */
    public void setPopulation(long pop)
    {
    	population = pop;
    }
    /****************************** setPopulation ****************************/
    public void setPopulation(String pop)
    {
    	population = Long.parseLong(pop);
    }
    /****************************** setLifeExp ******************************/
    public void setLifeExp(float life)
    {
    	lifeExp = life;
    }
    /****************************** setLifeExp *****************************/
    public void setLifeExp(String life)
    {
    	lifeExp = Float.parseFloat(life);
    }
    /****************************** setGNP *********************************/
    public void setGNP(int gnp_)
    {
    	gnp = gnp_;
    }
    /****************************** setGNP **********************************/
    public void setGNP(String gnp_)
    {
    	gnp = Integer.parseInt(gnp_);
    }
      
  //**************************** PUBLIC GET METHODS **********************
    

    /****************************** getCountryCode ***************************/
    public char[] getCountryCode()
    {
    	return country;
    }
    
    /****************************** getName **********************************/
    public char[] getName()
    {
    	return name;
    }
    
    /****************************** getContinent *****************************/
    public char[] getContinent()
    {
    	return continent;
    }
    
    /****************************** getSurfaceArea ***************************/
    public int getSurfaceArea()
    {
    	return surfaceArea;
    }
    
    /****************************** getYearOfIndep ***************************/
    public short getYearOfIndep()
    {
    	return yearOfIndep;
    }
    
    /****************************** getPopulation ***************************/
    public long getPopulation()
    {
    	return population;
    }
    
    /****************************** getLifeExp *******************************/
    public float getLifeExp()
    {
    	return lifeExp;
    }
    /****************************** getGNP **********************************/
    public int getGNP()
    {
    	return gnp;
    }
    /****************************** getHomeRRN ********************************/
    public short getHomeRRN()
    {
    	return homeRRN;
    }
    
    /****************************** getCode_Name_Continent **************************
     * returns one char array containing the Country code, name of the Country, and 
     * they continent.
     * */
    public char[] getCode_Name_Continent()
    {
    	String line = new String(country) + 
    				  new String(name)    +
    				  new String(continent);
    	
    	return line.toCharArray();
    }
    
    /************************** prettyPrintRecordInMem ***************************
	* Takes the record currently stored in memory, and returns a string that     *
	* is split up with a proper neatly format                                    *
	******************************************************************************/
    public String prettyPrintRecord()
    {
    	String line = String.format("  %s  %s   %s   %,10d  %5d  %,13d   %.1f %d", 
    			new String(country),new String(name), new String(continent),surfaceArea, yearOfIndep, population,lifeExp,gnp);

    	return line;
    }
    //**************************** PRIVATE METHODS *****************************

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
}
