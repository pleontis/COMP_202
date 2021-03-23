import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import BinSearchTree.BinarySearchTree;
import BinSearchTree.Node;

public class Console {
	static BinarySearchTree bTree;			//Object for Binary Search Tree
	static LinearHashing lHashing_05;		//Object for LinearHashing with split rule=0.5
	static LinearHashing lHashing_08;		//Object for LinearHashing with split rule=0.7
	static LinkedList<Integer> intList;		//List for placing all numbers of the file
	static LinearHashing deletedNums; 		//Hash table of numbers which have been selected for deletion
	static int[] searchNums;				//Array of random numbers from file which will be searched  
	static RandomAccessFile mFile;			//Object for opening file with numbers for testing
	
	public static void main(String[] args) throws IOException {
		//Initialize Merge and Split Rule for creating Linear Hashing Objects
		final float mergeRule=(float)0.5;
		final float splitRule_05=(float)0.5;
		final float splitRule_08=(float)0.8;
		final int sizeOfBucket=10, M=100;
		//Open File and check for errors during process
		try {
			mFile=new RandomAccessFile(args[0],"r");
		}
		catch(FileNotFoundException e) {
			System.out.println("Error, File:"+ args[0]+" wasn't found");
		}
		//Create a list with all numbers from File, a Hash table with the selected numbers for deletion
		//and aí Árray with the randomly selected numbers for search
		intList=createList();
		deletedNums= new LinearHashing(sizeOfBucket,M,splitRule_08,mergeRule);
		searchNums=new int[50];
		//Create two Hashing Tables and one Binary Search Tree
		lHashing_05= new LinearHashing(sizeOfBucket,M,splitRule_05,mergeRule);
		lHashing_08= new LinearHashing(sizeOfBucket,M,splitRule_08,mergeRule);
		bTree= new BinarySearchTree();
		
		//Print to console the measurements 
		printResults();
		
	}
	//Method for reading the whole file and create a Linked List with numbers
	public static LinkedList<Integer> createList() throws IOException{
		//Set cursor at the beginning of the File for reading
		mFile.seek(0);
		//Total numbers to be read in File
		int totalNums=(int)mFile.length()/4;
		int flagship=0;
		//Create a temporary list for storing all data
		LinkedList<Integer> list =new LinkedList<Integer>();
		try {
			while(flagship<totalNums) {
				//Read integer from current position in File
				int numberBuff=mFile.readInt();
				//Insert into list
				list.add(numberBuff);
				//Increase total numbers read
				flagship++;
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		//Set pointer at the begin of the File
		mFile.seek(0);
		return list;
	}
	//Method for printing measurements to console
	//AVG number of compares for insertion, search, deletion
	public static void printResults() throws IOException {
		int fileSize= (int) (mFile.length()/4);
		System.out.println("=========================================================Average Compares=========================================================");
		System.out.println("==================================================================================================================================");
		System.out.println("Input Size N    LH u>50%    LH u>80%    BST    		LH u>50%    LH u>80%    BST    		LH u>50%    LH u>80%    BST     		  ");
		System.out.println("		INSERTION   INSERTION	INSERTION	SEARCH	    SEARCH	SEARCH		DELETION   DELETION	DELETION                		  ");
		for(int i=0;i<fileSize;i+=100) {
			System.out.print("N="+(i+100));
			//Take measurements and print Result
			insert100();
			search50();
			delete50();
		}
	}
	//Method for inserting 100 numbers from File
	public static void insert100() throws IOException
	{
		for(int i=0;i<100;i++) {
			//Read number from current position in File
			int number=mFile.readInt();
			//Execute task in 3 Objects
			lHashing_05.insertKey(number);
			lHashing_08.insertKey(number);
			bTree.insert(new Node(number));
		}
		//Create a list of 50 random numbers which are inserted from File
		pickRandomNums();
		//Print measurements
		System.out.print("		"+lHashing_05.getCompares()/100+" 	    "+lHashing_08.getCompares()/100+"   	"+bTree.getCompares()/100);
		//Set compares equal to 0 for all 3 Objects  				
		eraseCompares();
	}
	//Method for searching 50 random numbers that exist into File in each Object
	public static void search50() {
		for(int i=0;i<50;i++) {
			//Get a random number from File
			int number=searchNums[i];
			//Execute task in 3 Objects
			lHashing_05.searchKey(number);
			lHashing_08.searchKey(number);
			bTree.search(bTree.getRoot(), number);
		}
		System.out.print("		"+lHashing_05.getCompares()/50+" 	    "+lHashing_08.getCompares()/50+"   	"+bTree.getCompares()/50);
		//Set compares equal to 0 for all 3 Objects  
		eraseCompares();
	}
	public static void delete50() {
		for(int i=0;i<50;i++) {
			//Get a random number from File
			int number=searchNums[i];
			//Execute task in 3 Objects
			lHashing_05.deleteKey(number);
			lHashing_08.deleteKey(number);
			bTree.delete(number);
		}
		System.out.println("		"+lHashing_05.getCompares()/50+" 	    "+lHashing_08.getCompares()/50+"   	"+bTree.getCompares()/50);
		//Set compares equal to 0 for all 3 Objects  
		eraseCompares();	
	}
	//Method for creating a list of numbers from the list that has been inserted
	public static void pickRandomNums() throws IOException {
		//Calculate how many numbers have been inserted until now
		long pointer=mFile.getFilePointer()/4;
		//Keep loop until we complete a list with 50 numbers to search
		int index=0;
		while((index<50)) {
			int rNum=intList.get((int)(Math.random()*pointer));
			//Check if rNum has been already deleted
			if(!deletedNums.searchKey(rNum)){
				//Add random number in List for searching and also add it in Hash table 
				//with already deleted numbers
				searchNums[index]=rNum;
				deletedNums.insertKey(rNum);
				index++;
			}
		}
	}
	//Method for setting compares for all 3 
	public static void eraseCompares() {
		lHashing_05.setCompares(0);
		lHashing_08.setCompares(0);
		bTree.setCompares(0);
	}
}
