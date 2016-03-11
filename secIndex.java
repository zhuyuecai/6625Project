package comp6521_project;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class secIndex {

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
					String filePath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\dbase\\person.txt";
					//String filePath = "e:\\dbase\\person.txt"; //SSD
				    //accessing the file @ filePath for random line number//////
					RandomAccessFile file = new RandomAccessFile(filePath, "r");
				    String raw = "";
				    String result = "";
				    /////computing exact byte location of the tuple/////////////
				    position --;
				    position = position * 100;
				    ////////////////////////////////////////////////////////////
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
	{   System.out.println("one is call");			// that is first level index, not to be loaded in main memory
		long sPos = 0;
		long ePos = 0;
		Writer wr;
        for (int key = 18;key<100;++key) 
        {// loop through any hash map for each key
            
            
          
            String iPath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\" + key+ ".txt";
            //String iPath = "e:\\index\\" + String.valueOf(sKey)+ ".txt";//SSD
            File file = new File(iPath);// index file corresponding the key
    	    long fileSize = file.length();// take its file size
    	    System.out.println(fileSize);
    	    String sPoint = "";
    	    ePos = sPos + fileSize;
    	    // compute the start and end position according to file size
    	   	sPoint = String.valueOf(sPos) + " " + String.valueOf(ePos);
     	    Pointers.put(key, sPoint);// note it in the hash map that will server
     	    							// as second level index
     	    sPos = ePos;//next time start position will start from previous end position
    	    //////////////////////////////////////////////////////////////
    	    List <Long> list = new ArrayList<>();
    	    //////////////////////////////////////////////////////////////
    	    try (Scanner scanner = new Scanner(file);) 
    	    {
    	        while (scanner.hasNextLong()) 
    	        {
    	            list.add(scanner.nextLong());//filling the list
    	        }	// read the pointers in a list
    	    } 
    	    catch (Exception e) 
    	    {
    	        e.printStackTrace();
    	    }
    	    System.out.println("delete");
    	    file.delete();// once file content has been loaded we can delete the
    	    				// intermediary first level index files
            ///////////////////////////////////////////////////////////////
    	    for (int i=0; i < list.size(); i++)
    	    {
    	    	try 
        		{ 	// write to a new file which will hold indexes for all keys
        			wr = new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\index1.txt", true);
    	    		//wr = new FileWriter("e:\\index\\index1.txt", true);//SSD
    	    		wr.write(String.valueOf(list.get(i)));
        			wr.write(" ");
        			wr.close();
        		} 
        		catch (IOException e) 
        		{
        			e.printStackTrace();
        		}
    	    }
        }
		
		
		
	}
	/////////////////////////////////////////////////////
	
	
	/////////////////////////////////////////////////////////////////////////////
	static void saveKeys()////function to save hash maps data
	{						// to be used later without creating new index
		///////////////////////////////////////////////////////////
		Writer wr;
				
        for (Map.Entry<Integer, Long> entry : Count.entrySet()) 
        {// loop the hash map and write corresponding values of each key
        	//from all hash maps to the file after converting to string
            int key = entry.getKey();
            long count = entry.getValue();
            long salary = Salary.get(key);
            String points = Pointers.get(key);
            String kLine = String.valueOf(key) + " " + String.valueOf(count) + " " + String.valueOf(salary) + " " + points;
           ////////////////
    		try 
    		{
    			//writing the kLine string to the index file key.txt
    			// kLine will contain Key >> its count / corresponding Salary sum
    			// and position reference points for actual pointers that are in a separate file
    			wr = new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\key.txt", true);
    			//wr = new FileWriter("e:\\index\\key.txt", true);//SSD
    			wr.write(kLine);
    			wr.write("\r\n");
    			wr.close();
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
               
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
	    int listSize = ePoint - sPoint; // calculate list size
	    byte[] bytes = new byte[listSize];
	    //////////////////////////////////////////////////////////////////
		String filePath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\index1.txt";
	    //String filePath = "e:\\index\\index1.txt";//SSD
	    String result = "";
	    //random access the first level index to read the required corresponding bytes//////
		RandomAccessFile file = new RandomAccessFile(filePath, "r");
			
			file.seek(sPoint);
		    file.read(bytes, 0, listSize);// read in a byte array
		    file.close();
    
	    //////////////////////////////////////////////////////////////////
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
		}
	}
	
	///////////////////////////////////////////////////////////////
	static void keyMap(int Age, long cSal, long tCount)
	{
	//function keyMap takes key::Age, Salary sum of corresponding tuples, and their Count 
		if(Count.containsKey(Age))//if key already exists increase count by 1
	        Count.compute(Age, (k, v) -> v + 1);
		else// if new key then add key and add value >> count i.e. tCount
		{
			Count.put(Age, tCount);
		}
		//////////////////////////////////////////////////////////////////////
		if(Salary.containsKey(Age))//if key already exists add salary amount to 
								   //existing value of salary
	        Salary.compute(Age, (k, v) -> v + cSal);
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
		String sLine = String.valueOf(line);//parsing line number to string
		long cTmp = 1;
		///////////////////////////////////////////////////////////
		keyMap(iAge, cSal, cTmp);//calling keyMap to fill hash maps
		///////////////////////////////////////////////////////////
/*		Writer wr;
		try {
			//writing the line numbers to the index[pointer] file named as age[key]
			//creating new file or appending if file already exist
			wr = new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\" + age + ".txt", true);
			//wr = new FileWriter("e:\\index\\" + age + ".txt", true);//SSD
			wr.write(sLine);
			wr.write(" ");
			wr.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	*/}
	/////////////////////////////////////////////////////////////////////////////
	//function to load existing index from key.txt file and fill up hash maps directly
	static void loadIndex() throws IOException
	{     
		  // loading the keys with analytic details 
		  //from saved key.txt file in a list of strings
		  File iKey = new File("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\key.txt");
		  //File iKey = new File("e:\\index\\key.txt");//SSD
		  BufferedReader reader = new BufferedReader(new FileReader(iKey));
	      List<String> lines = new ArrayList<String>();
	      String line = reader.readLine();
	      while (line != null) 
	      {
	          lines.add(line);
	          line = reader.readLine();
	      }
	      reader.close();
	      /////////////////////////////////////////////////////////////////////
	      for (int i=0; i < lines.size(); i++) // loop the list to extract
	    	  // key _ count _ salary and loading into the hash maps
			{
	    	  String sVals = lines.get(i);
	    	  String[] kVals = sVals.split(" ");
	    	  int key = Integer.parseInt(kVals[0]);
	    	  long count = Integer.parseInt(kVals[1]);
	    	  long salary = Integer.parseInt(kVals[2]);
	    	  String points = kVals[3] + " " + kVals[4];
	    	  //////////////////////////////////////////
	    	  Pointers.put(key, points);
	    	  Salary.put(key, salary);
	    	  Count.put(key, count);
			}
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
			bufferedReader = new BufferedReader(new FileReader(path), block);
			//bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\me2.txt", true), block);
		
		   
			char[] age = new char[2];
		/////////////////////////////////////////////////////////////
		if(file.exists())
		{
		    for (int a = 18;a<99;++a){
		    	bufferedWriter[a-18] = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\"+a+".txt", true), block);	
		    	
		    }
			long fileSize = file.length();
			System.out.println("    File Size : " + fileSize);
			
			// Skipping first 39 characters on the first line to get to the location of teh age field
		    bufferedReader.skip(39);
		    int index;
		    // Reading age field into age array until reaching the end of file
		    while ((bufferedReader.read(age, 0, 2)) > 0) {
		    	index = Integer.parseInt(String.valueOf(age))-18;
		    	// Writing the age value to the output file (2 characters)
		    	// Writing the left-justified 0-padding for the line number which depends on the number of digits in this line number,
		        // in order to make line number fixed width, consisting of 11 characters
		       // bufferedWriter.write(LINE_LEN_ZERO_PADDING_ARR, 0, Config.LINE_NUMBER_CHAR_LEN - getLineNumberCharLength(lineNum));
		        // Writing the line number into output file
		        bufferedWriter[index].write(String.valueOf(lineNum) + " ");
		        
		        //System.out.println(String.valueOf(lineNum));
		        // skipping the rest of the current line and the beginning of the next line until the next age field
		        bufferedReader.skip(98);
		        
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
/*		int runSize = 3000000;
		int nTuples = runSize/100;
		int tupSize = 100;
		long lineNum = 1;//for first line number initial value set as 1
		File file = new File(path);
		/////////////////////////////////////////////////////////////
		if(file.exists())
		{
			///////////Calculating total runs according to runSize and memory configuration
			long fileSize = file.length();
			System.out.println("    File Size : " + fileSize);
			long finRun = 0;
			long fulRuns = fileSize / runSize;
			if ((fulRuns*runSize) < fileSize)//computing if last run is needed depending on file size
			{
				finRun = fileSize - (fulRuns*runSize);
			}
			if(fulRuns > 0)
				System.out.println("    " + fulRuns + " runs of size : " + runSize + " B");
			if(finRun > 0)
				System.out.println("  + 1 run of size : " + finRun + " B");
			///////////////////////////////////////////////////////////////////
			try
			{
				RandomAccessFile rFile = new RandomAccessFile(path, "r");
			//processing data file @ path to create index on Age
			///////////////////////////////////////////////////////////////////
			if (fulRuns > 0)
			{// loop for number on runs needed to read and process file in chunks 
				for (int i = 0; i < fulRuns; i++)
				{
					long position = i*runSize;
					rFile.seek(position);
					//byte array to hold data file's chunk
					byte[] rBytes = new byte[runSize];
					//reading data file chunk in byte array
					rFile.read(rBytes, 0, runSize);
					///////////////////////////////////////////
					int sPos = 0;//initial start position for first tuple in current data chunk
					int ePos = (tupSize-1);//initial end position for first tuple in current data chunk			
					////////////////////////////////////////////
					for (int lp = 0; lp < nTuples; lp++)
					{//inner loop for number of tuple in one chunk
						//read each tuple from data chunk by start and end position 
						byte[] tBytes = Arrays.copyOfRange(rBytes, sPos, ePos);
						// array to hold tuple as bytes from data chunk rBytes
						
					//updating start and end positions for next run
					sPos = sPos + 100;
					ePos = ePos + 100;
					String St = "";//string to hold tuple
					
						for(int ik = 0; ik < tBytes.length; ik++)
						  {// loop tuple bytes to convert into tuple string
							  char c = (char)tBytes[ik];   
					          St = St + c;
					      }
					  //call indexFile to write line number in corresponding index file
					  indexFile(lineNum, St);// tuple string will be used to extract key as 
					  						//Age and its salary to further process for hashMaps
					  lineNum ++; // increase line number by 1
					  tBytes = null;// clear tuple byte array
					}
					rBytes = null;// clear data chunk byte array
				}
			}
			
				if (finRun > 0)//Same routine as above if there is a final run
							   // of size less than runSize
				{
					long position = fulRuns*runSize;
					rFile.seek(position);
					byte[] fBytes = new byte[(int) finRun];
					rFile.read(fBytes, 0, (int) finRun);
					////////////////////////////////////////////
					int siPos = 0;
					int eiPos = 99;	
					int finTups = (int) (finRun / 100);
					////////////////////////////////////////////
					for (int ln = 0; ln < finTups; ln++)
					{
						byte[] tBytes = Arrays.copyOfRange(fBytes, siPos, eiPos);
						
						siPos = siPos + 100;
						eiPos = eiPos + 100;
						String St = "";
					
					  for(int ik = 0; ik < tBytes.length; ik++)
					  {
						  char c = (char)tBytes[ik];   
				          St = St + c;
				      }
					  indexFile(lineNum, St);
					  lineNum ++;
					  tBytes = null;
					}
					///////////////////////////////////////////
					fBytes = null;
					///////////////////////////////////////////
				}
				rFile.close();
			
			*/
			

			////////////////////////////////////////////////////////////////////
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
	static int getAge()
	{
		////////////Getting user input and returning Age as integer
		System.out.println("\r\nEnter AGE in range 18 _ 99 Inclusive:");
		Scanner choice = new Scanner(System.in);
		int age = choice.nextInt();
		return age;
	}
	///////////////////////////////////////////////////////////////////////////
	static int loadMake()
	{
		System.out.println("YOU CAN LOAD AN EXISTING INDEX FILE OR CREATE A NEW ONE.");
		////////////Getting user choice 1/2 to load index or create new
		System.out.println("1. Enter '1' TO CREATE NEW INDEX.");
		System.out.println("2. Enter '2' TO LOAD EXISTING INDEX.");
		Scanner choice = new Scanner(System.in);
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
		String filePath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\dbase\\person.txt";
		//String filePath = "e:\\dbase\\person.txt";//SSD
		long sTime = 0;
		long eTime = 0;
		////////////////////////////////////////////////////////////////////////////////
		int ch = loadMake();//user choice to load existing index files
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
		int Age = getAge();//Function to get integer Age as user input
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
