package main;
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
	static long previousPosition = 0;
	////////////////////////////////////////////////////////////////////////////////
	static String getTuple(long position, BufferedReader file )//taking position of tuple >> line number
		    throws IOException 			//in data file and returning the formatted  
		 	{						//tuple as string after reading from data file
					//accessing the file @ filePath for random line number//////
					long skippingValue = position - previousPosition;
					String raw = "";
				    String result = "";
				    file.skip(skippingValue * 100);
				    char[] bytes = new char[100];//reading the tuple in byte array
				    file.read(bytes, 0, 100);
					  for(int i = 0; i < 100; i++)//converting byte array to string
					  {
						  raw = raw + bytes[i];
				      }
					  //formatting the raw tuple string for display
					result = raw.substring(0, 9) + "\t" + raw.substring(9, 24) + "\t" + raw.substring(24, 39) + "\t" + raw.substring(39, 41) + "\t" + raw.substring(41, 51) + "\t" + raw.substring(51, 99) + "\n";
					previousPosition = position + 1;
					return result;//returning the formatted result string
				    
		 	}
	/////////////////////////////////////////////////////////////////////////////
	static void oneIndex()////function to merge intermediate index files in one file
	{  						// that is first level index, not to be loaded in main memory
		BufferedReader[] bufferedReader = new BufferedReader[82];
		BufferedWriter bufferedWriter = null;
		try{
			bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\index1.txt", true));	
		}catch(Exception e){}
    	
		long sPos = 0;
		long ePos = 0;
		
		try{
		for (int a = 18;a<100;++a){
	    	bufferedReader[a-18] = new BufferedReader(new FileReader("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\"+a+".txt"));	
	    }
		}catch(Exception e){}
		
        for (int key = 18;key<100;++key) 
        {// loop through any hash map for each key
            String iPath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\" + key+ ".txt";
            File file = new File(iPath);
    	    long fileSize = file.length();// take its file size
    	    String sPoint = "";
    	    ePos = sPos + fileSize;
    	    // compute the start and end position according to file size
    	   	sPoint = String.valueOf(sPos) + " " + String.valueOf(ePos);
     	    Pointers.put(key, sPoint);// note it in the hash map that will server
     	    							// as second level index
     	    sPos = ePos;//next time start position will start from previous end position
    	    //////////////////////////////////////////////////////////////
    	    try{
    	    	//char[] reading = new char[4096];
    	    	String reading = null;
    	    	while(true){
	     	    reading = bufferedReader[key-18].readLine();//(reading, 0, 4096) > 0){
	     	    if(reading != null)
	     	    	{
	     	    	bufferedWriter.write(reading);
	     	    	bufferedWriter.write("\r\n");
	     	  
	     	    }else
	     	    	break;
	     	    }
    	    }catch(Exception e){}
     	    try{
     	    }catch(Exception e){}
     	  
     	  }
        try{
        	bufferedWriter.close();
        
        for (int a = 18;a<100;++a){
	    	bufferedReader[a-18].close();	
	    }
        }catch(Exception e){}
       }
	
/////////////////////////////////////////////////////////////////////////////
	static void saveKeys()////function to save hash maps data
	{						// to be used later without creating new index
		///////////////////////////////////////////////////////////
		Writer wr;
		try{
			wr = new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\key.txt", true);
		
		
        for (Map.Entry<Integer, Long> entry : Count.entrySet()) 
        {// loop the hash map and write corresponding values of each key
        	//from all hash maps to the file after converting to string
            int key = entry.getKey();
            long count = entry.getValue();
            long salary = Salary.get(key);
            String points = Pointers.get(key);
            String kLine = String.valueOf(key) + " " + String.valueOf(count) + " " + String.valueOf(salary) + " " + points;
        			wr.write(kLine);
    			wr.write("\r\n");
        }	wr.close();
    	
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
	    
	    BufferedWriter writer = null;
	    writer = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\output.txt", true));
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\index\\index1.txt"));
        BufferedReader reading = null;
        reading = new BufferedReader(new FileReader("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\dbase\\person1.txt"));
        
        int count = 0;
        char[] tuple = new char[100];
        reader.skip((long)sPoint);
        while(count != Count.get(Age)){
        	
        	tuple = getTuple(Long.valueOf(reader.readLine()), reading).toCharArray();
        	writer.write(tuple);
        	writer.newLine();
        	count++;        
        }
        reader.close();
        writer.close();
        reading.close();
    	
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

	}
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
		BufferedWriter[] bufferedWriter = new BufferedWriter[82];
		File file = new File(path);
		try{
			bufferedReader = new BufferedReader(new FileReader(path), block);
			char[] age = new char[2];
		/////////////////////////////////////////////////////////////
		if(file.exists())
		{
		    for (int a = 18;a<100;++a){
		    	
		    	bufferedWriter[a-18] = new BufferedWriter(new FileWriter("C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\temp\\"+a+".txt", true), block);	
		    	
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
		        bufferedWriter[index].write(String.valueOf(lineNum) + "\r\n");     
		        bufferedReader.read(cSal, 0, 10);
		        salary = Long.parseLong(String.valueOf(cSal));
		        keyMap(Integer.parseInt(String.valueOf(age)), salary, cTmp);
		        bufferedReader.skip(88);
		        // Incrementing the line number counter
		        lineNum++;
		            
		    }	
		    for (int a = 18;a<100;a++){
		    	bufferedWriter[a-18].close(); 
		    }
			
		    bufferedReader.close();
			//merge intermediary first level index files and save in one file
			oneIndex();
			//save hash map keys[second level index file] and analytics from hash maps
			saveKeys();
		
		}
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
		String filePath = "C:\\Users\\Nehal\\Desktop\\COMP6521_Project\\dbase\\person1.txt";
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
