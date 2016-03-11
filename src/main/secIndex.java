package main;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class secIndex {
    static String root = "/home/winnerzhu/github/6625Project/";
    static String filename = "person_fnl.txt";//"person.txt";//
	static Map <Integer, String> Pointers = new HashMap <>();
	// this serves as second level index contains reference position
	 // to actual pointers for that key to be loaded when needed, 
	  // saved in a separate file.
	static Map <Integer, Long> Count = new HashMap <>();
	// contains key and count analytics
	static Map <Integer, Long> Salary = new HashMap <>();
	// contains key and sum of salaries analytics

	////////////////////////////////////////////////////////////////////////////////
	static String getTuple(long position)//taking position of tuple >> line number
		    throws IOException 			//in data file and returning the formatted  
		 	{						//tuple as string after reading from data file
					String filePath = root + filename;
					//String filePath = "e:\\dbase\\person.txt"; //SSD
				    //accessing the file @ filePath for random line number//////
					RandomAccessFile file = new RandomAccessFile(filePath, "r");
				    String raw = "";
				    String result = "";
				    /////computing exact byte location of the tuple/////////////
				    //position --;
				    position = position * 100;
				    ////////////////////////////////////////////////////////////
				    System.out.println(position);
				    file.seek(position);
				    byte[] bytes = new byte[100];//reading the tuple in byte array
				    file.read(bytes, 0, 100);
				    file.close();
					  for(int i = 0; i < 100; i++)//converting byte array to string
					  {
						  char c = (char)bytes[i];   
						  raw = raw + c;
				      }
					//formatting the raw tuple string for display
					result = raw.substring(0, 9) + "\t" + raw.substring(9, 24) + "\t" + raw.substring(24, 39) + "\t" + raw.substring(39, 41) + "\t" + raw.substring(41, 51) + "\t" + raw.substring(51, 99);
				    return result;//returning the formatted result string
		 	}
	/////////////////////////////////////////////////////////////////////////////
	static void oneIndex()////function to merge intermediate index files in one file
	{  						// that is first level index, not to be loaded in main memory
		
		long sPos = 0;
		long ePos = 0;
		try{
		Writer wr = new FileWriter(root+"/index/index1.txt", true);
        for (int key = 18;key<99;++key) 
        {// loop through any hash map for each key
            String iPath = root + "temp/" + key+ ".txt";
            File file = new File(iPath);
            //String iPath = "e:\\index\\" + String.valueOf(sKey)+ ".txt";//SSD
            ePos = sPos+Count.get(key);
            String va = sPos + " "+ePos;
     	    Pointers.put(key, va);// note it in the hash map that will server
     	    							// as second level index
     	    //sPos = ePos;//next time start position will start from previous end position
    	    //////////////////////////////////////////////////////////////
    	    //////////////////////////////////////////////////////////////
    	    try (Scanner scanner = new Scanner(file);) 
    	    {
    	    	
    	        while (scanner.hasNextLong()) 
    	        {
                    long ww= scanner.nextLong();
    	            wr.write(String.valueOf(ww));
        			wr.write("\n");

    	        }	// read the pointers in a list
    	    } 
    	    catch (Exception e) 
    	    {
    	        e.printStackTrace();
    	    }
    	    //file.delete();// once file content has been loaded we can delete the
    	    sPos=ePos;				// intermediary first level index files
            ///////////////////////////////////////////////////////////////
    	    }
        wr.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
        }
	
/////////////////////////////////////////////////////////////////////////////
	static void saveKeys()////function to save hash maps data
	{						// to be used later without creating new index
		///////////////////////////////////////////////////////////
		Writer wr;
		try{
			wr = new FileWriter(root+"/index/key.txt", true);
		
		
        for (Map.Entry<Integer, Long> entry : Count.entrySet()) 
        {// loop the hash map and write corresponding values of each key
        	//from all hash maps to the file after converting to string
            int key = entry.getKey();
            long count = entry.getValue();
            long salary = Salary.get(key);
            String points = Pointers.get(key);
          
            String kLine = String.valueOf(key) + " " + String.valueOf(count) + " " + String.valueOf(salary) + " " + points;
           
           ////////////////
    	//	try 
    		//{
    			//writing the kLine string to the index file key.txt
    			// kLine will contain Key >> its count / corresponding Salary sum
    			// and position reference points for actual pointers that are in a separate file
    			//wr = new FileWriter("e:\\index\\key.txt", true);//SSD
    			
    			wr.write(kLine);
    			wr.write("\r\n");
        }	wr.close();
    		//} 
        
		}catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
           
        		
	}
	/////////////////////////////////////////////////////////////////////////////
	static void avSal(int Age)////take integer Age to display its analytic details
	{
		// displaying the total count of Age from hashMap Count
		System.out.println("Total records exist with AGE " + Age + " : [" + Count.get(Age) + "]");
		// computing and displaying Average salary from hashMaps Count and Salary
		System.out.println("Average Salary : [$ " + Salary.get(Age)/Count.get(Age) + "] \r\n");
	}
	/////////////////////////////////////////////////////////////////////
	static void displayTuples(int Age) throws IOException //take integer Age to search and display records
	{	//by first loading the respective pointers file from folder in a list
	
	    String points = Pointers.get(Age);// getting position reference in first level index file

	    String[] sePoint = points.split(" ");
	    int sPoint = Integer.parseInt(sePoint[0]);// start position
	    int ePoint = Integer.parseInt(sePoint[1]);// end position
        

	    //////////////////////////////////////////////////////////////////
		String filePath = root+"index/index1.txt";
	    //String filePath = "e:\\index\\index1.txt";//SSD
	   // String result = "";
	    //random access the first level index to read the required corresponding bytes//////
		RandomAccessFile file = new RandomAccessFile(filePath, "r");
		//String tuple;
		//file.seek(sPoint);
	    //file.read(bytes, 0, listSize);// read in a byte array
	    //file.close();
	    System.out.println("SIN      	First Name	Last Name     	Age	Yearly Income	Address");
	    file.seek(sPoint *10);
	    for (int i=sPoint;i<ePoint;++i){
	    	long index = Long.valueOf(file.readLine()).longValue();
	    	System.out.println(getTuple(index));
	    	
	    }
	    
	    
   /* //////////////////////////////////////////////////////////////////
    for(int i = 0; i < listSize; i++)//converting byte array to string
	  {
		  char c = (char)bytes[i];   
		  result = result + c;
      }
    //////////////////////////////////////////////////////////////////

    List<Long> list = new ArrayList<>();
    //////////////////////////////////////////////////////////////////
    try (Scanner scanner = new Scanner(result);) 
    {// now reading the result string in a list of long
        while (scanner.hasNextLong()) 
        {	      
            list.add(scanner.nextLong());//filling the list
        }
    } 
    catch (Exception e) 
    {
        e.printStackTrace();
    }
	//////////////////////////////////////////////////////////////////////
    System.out.println("SIN      	First Name	Last Name     	Age	Yearly Income	Address");
    String tuple = "";
	
    for (int i=0; i < list.size(); i++) // loop the loaded list 
    									//to get and display tuples
	{
		try 
		{
			tuple = getTuple(list.get(i));//function call getTuple to get tuple 
											// as string from data file
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println(tuple); // displaying the obtained formatted tuple
	}*/
	    } 

		//////////////////////////////////////////////////////////////////////

	   
		

	
	
	///////////////////////////////////////////////////////////////
	static void keyMap(int Age, long cSal, long tCount)
	{
	//function keyMap takes key::Age, Salary sum of corresponding tuples, and their Count 
		if(Count.containsKey(Age))//if key already exists increase count by 1

		    Count.put(Age,Count.get(Age)+1);
		else// if new key then add key and add value >> count i.e. tCount
		{
			Count.put(Age, tCount);
		}
		//////////////////////////////////////////////////////////////////////
		if(Salary.containsKey(Age))//if key already exists add salary amount to 
								   //existing value of salary
	      
		    Salary.put(Age,Salary.get(Age)+cSal);
		else// if new key then add key and add value salary i.e. cSal
		{
			Salary.put(Age, cSal);
		}
		//////////////////////////////////////////////////////////////////////
	}
	
	///////////////////////////////////////////////////////////////////////
	static void indexFile(long line, String tup)
	{
		//Getting line number as long, and a string i.e. tuple on that line
		String age = tup.substring(39, 41);//reading age from the tuple
		String sal = tup.substring(42, 51);//reading salary from the tuple
		int iAge = Integer.parseInt(age);//parsing age to integer
		long cSal = Integer.parseInt(sal);//parsing salary to long
		//String sLine = String.valueOf(line);//parsing line number to string
		long cTmp = 1;
		///////////////////////////////////////////////////////////
		keyMap(iAge, cSal, cTmp);//calling keyMap to fill hash maps
		///////////////////////////////////////////////////////////

	}
	/////////////////////////////////////////////////////////////////////////////
	//function to load existing index from key.txt file and fill up hash maps directly
	static void loadIndex() throws IOException
	{     
		  // loading the keys with analytic details 
		  //from saved key.txt file in a list of strings
		  File iKey = new File(root+"/index/key.txt");
		  //File iKey = new File("e:\\index\\key.txt");//SSD
		  BufferedReader reader = new BufferedReader(new FileReader(iKey));
	      String line = reader.readLine();
	      while ( (line = reader.readLine() ) != null) 
	      {
	  
	    	  String[] kVals = line.split(" ");
	    	  int key = Integer.parseInt(kVals[0]);
	    	  long count=Long.valueOf(kVals[1]).longValue();
	    	  long salary=Long.valueOf(kVals[2]).longValue();
	    	  
	    	  String points = kVals[3] + " " + kVals[4];
	    	  //////////////////////////////////////////
	    	  Pointers.put(key, points);
	    	  Salary.put(key, salary);
	    	  Count.put(key, count);
	      }
	      reader.close();
	      /////////////////////////////////////////////////////////////////////

	}
	
	////////////////////////////////////////////////////////////////////////////
	//function taking file path as string and creating index files for that file
	static void makeIndex(String path)
	{   
		int block = 4096;
		long lineNum = 0;
		BufferedReader bufferedReader = null;
		BufferedWriter[] bufferedWriter = new BufferedWriter[81];
		File file = new File(path);
		try{
			bufferedReader = new BufferedReader(new FileReader(file), block);
			//bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\me2.txt", true), block);
		
		   
			char[] age = new char[2];
		/////////////////////////////////////////////////////////////
		if(file.exists())
		{
		    for (int a = 18;a<99;++a){
		    	
		    	bufferedWriter[a-18] = new BufferedWriter(new FileWriter(root+"temp/"+a+".txt", true), block);	
		    	
		    }
			long fileSize = file.length();
			System.out.println("    File Size : " + fileSize);
			
			// Skipping first 39 characters on the first line to get to the location of teh age field
		    bufferedReader.skip(39);
		    int index;
		    char[] cSal = new char[10];
		    long salary;
		    long cTmp = 1;
			
		    // Reading age field into age array until reaching the end of file
		    while ((bufferedReader.read(age, 0, 2)) > 0) {
		    	index = Integer.parseInt(String.valueOf(age))-18;
		    	// Writing the age value to the output file (2 characters)
		    	// Writing the left-justified 0-padding for the line number which depends on the number of digits in this line number,
		        // in order to make line number fixed width, consisting of 11 characters
		       // bufferedWriter.write(LINE_LEN_ZERO_PADDING_ARR, 0, Config.LINE_NUMBER_CHAR_LEN - getLineNumberCharLength(lineNum));
		        // Writing the line number into output file
		        bufferedWriter[index].write(String.valueOf(lineNum) + " ");
		        bufferedReader.read(cSal, 0, 10);
		        salary = Long.parseLong(String.valueOf(cSal));
		        //System.out.println(String.valueOf(lineNum));
		        // skipping the rest of the current line and the beginning of the next line until the next age field
		        keyMap(Integer.parseInt(String.valueOf(age)), salary, cTmp);
		        bufferedReader.skip(88);
		        
		        // Incrementing the line number counter
		        lineNum++;
		            
		    }	
		    for (int a = 18;a<99;a++){
		    	bufferedWriter[a-18].close(); 
		    }
			
		    
		    bufferedReader.close();
			//merge intermediary first level index files and save in one file
			oneIndex();
			//save hash map keys[second level index file] and analytics from hash maps
			saveKeys();
		
		}
		// for 5MB RAM [-Xms5m -Xmx5m] >> 900 blocks each run = 40 tuples each 
		//=> 30000 tuples = 3000000 B
		// for 2MB RAM [-Xms2m -Xmx2m] >> 100 blocks each run = 40 tuples each 
		//=> 4000 tuples = 400000 B
		
			else
			{
				 System.out.println("File does not exists!");
			}
	}catch (IOException e) 
	{
		e.printStackTrace();
	}
	
	}
	////////////////////////////////////////////////////////////////////////////
	static int getAge(Scanner choice)
	{
		////////////Getting user input and returning Age as integer
		System.out.println("\r\nEnter AGE in range 18 _ 99 Inclusive:");

		int age = choice.nextInt();

		return age;
	}
	///////////////////////////////////////////////////////////////////////////
	static int loadMake(Scanner choice)
	{   
		System.out.println("YOU CAN LOAD AN EXISTING INDEX FILE OR CREATE A NEW ONE.");
		////////////Getting user choice 1/2 to load index or create new
		System.out.println("1. Enter '1' TO CREATE NEW INDEX.");
		System.out.println("2. Enter '2' TO LOAD EXISTING INDEX.");

		int choix = choice.nextInt();

		return choix;
		
	}
	///////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException 
	{
		
		////////////////////////////////////////////////////////////////////////////
		System.out.println("COMP 6521 _ INDEX @ ATRIBUTE AGE FOR RELATION::PERSON");
		System.out.println("CREATE FOLDERS @ ROOT C: NAMED 'index' AND 'dbase'.");
		System.out.println("PLACE DATA FILE 'person.txt' IN FOLDER 'dbase'. \r\n");
		String filePath = root+filename;
		//String filePath = "e:\\dbase\\person.txt";//SSD
		long sTime = 0;
		long eTime = 0;
		////////////////////////////////////////////////////////////////////////////////
		Scanner choice = new Scanner(System.in);
		int ch = loadMake(choice);//user choice to load existing index files
							//or make new index on data file
		////////////////////////////////////////////////////////////////////////////////
		switch(ch)
		{
	    case 1: //
			System.out.println("Creating Index on atribute AGE . . .");
			sTime = System.currentTimeMillis();// note start time
			makeIndex(filePath);//Function to take file path as string and
								//create index files for the given file
			///////////////////////////////////////////////////////
			loadIndex();
			eTime = System.currentTimeMillis();//note end time
			System.out.println("Index Created in : " + (eTime - sTime) + " ms");
			break;
			//////////////////////////////////////////////////////////////////////
	    case 2: //
		
			System.out.println("Loading Index from existing file . . .");
			sTime = System.currentTimeMillis();// note start time
			loadIndex();// function to load existing index file
			eTime = System.currentTimeMillis();//note end time
			System.out.println("Index Loaded in : " + (eTime - sTime) + " ms");
			break;
			//////////////////////////////////////////////////////////////////////
	    default: // system will shut down for invalid choice
			System.out.println("INVALID CHOICE, SYSTEM SHUTTING DOWN . . .");
			System.out.println("PRESS ENTER TO EXIT.");
		    try {
		        System.in.read();
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		    System.exit(0);
		}
		///////////////////////////////////////////////////////////////////////////
		while(true)
		{
		int Age = getAge(choice);//Function to get integer Age as user input
		//////////////////////////////////////////////////////////////
			if (Count.containsKey(Age))//check in hash map if key(age) exist
			{
			avSal(Age);//call for avSal function to compute and display desired
					   //analytics >> count & average salary for this age group
			sTime = System.currentTimeMillis();// note start time
			displayTuples(Age);//function call to load and display corresponding tuples
			eTime = System.currentTimeMillis();// note end time
			System.out.println("Records displayed in : " + (eTime - sTime) + " ms");
			}
			else
				System.out.println("No record found for Age : " + Age);
		}		// if key not found display message
		///////////////////////////////////////////////////////////////////////////
		
	}
}
