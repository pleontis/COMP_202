import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import List.StaticTree;
import List.DynamicTree;
import List.Node;
import List.OrderedArray;

public class Console {
	
	static DynamicTree binaryTree;
	static StaticTree arrayTree;
	static OrderedArray orderedArray;
	static Scanner scan;
	static RandomAccessFile mFile;
	static LinkedList<Integer> intList;
	static long start;
	static long end;
	static float sec;
	public static void main(String[] args) throws IOException {
		intList=new LinkedList<Integer>();
		// Generate a list of random integers for testing
		for(int i=0;i<100;i++) {
			int randomInt = ThreadLocalRandom.current().nextInt();
			intList.add(randomInt);
		}
		//Open File and check for errors during process
		try {
		mFile=new RandomAccessFile(args[0],"r");
		}
		catch(FileNotFoundException e) {
			System.out.println("Error, File:"+ args[0]+" wasn't found");
		}
		//**************************************Static Memory Allocation***************************************************//
		//Create an ArrayTree object and take measurements for each wanted method
		arrayTree=createArrayTree();
		System.out.println("Average compares for creating a StaticTree: "+arrayTree.getCompares()/(mFile.length()/4));
		arrayTree.setCompares(0);
		//Test searching method for 100 random numbers
		//Finding the time before the operation is executed
	    start = System.nanoTime();
		for(int i=0;i<100;i++) {
			arrayTree.search(arrayTree.getRoot(), intList.get(i));
		}
		//Finding the time after the operation is executed
		end = System.nanoTime();
		//Finding the time difference and converting it into seconds
		sec = (end-start)/1000000F;
		System.out.println("Time for 100 random searches in StaticTree: "+sec+" ms");
		System.out.println("Average compares for 100 random searches in StaticTree: "+arrayTree.getCompares()/100);
		arrayTree.setCompares(0);
		System.out.print("Numbers found in searchByRange (Ê=100): ");
		//Test searchByRange method for 100 random numbers with range of 100
		for(int i=0;i<100;i++) {
			arrayTree.searchByRange(arrayTree.getRoot(), intList.get(i),intList.get(i)+100);
		}
		System.out.println("\nAverage compares for 100 random searches by Range(K=100) in StaticTree: "+arrayTree.getCompares()/100);
		arrayTree.setCompares(0);
		System.out.print("Numbers found in searchByRange (Ê=1000): ");
		//Test searchByRange method for 100 random numbers with range of 1000
		for(int i=0;i<100;i++) {
			arrayTree.searchByRange(arrayTree.getRoot(), intList.get(i),intList.get(i)+1000);
		}
		System.out.println("\nAverage compares for 100 random searches by Range(K=1000) in StaticTree: "+arrayTree.getCompares()/100);
		arrayTree.setCompares(0);
		
		//**************************************Dynamic Memory Allocation***************************************************//
		//Create an BinarySearchTree object and take measurements for each wanted method
		binaryTree= createBinarySearchTree();
		System.out.println("Average compares for creating a DynamicTree: "+binaryTree.getCompares()/(mFile.length()/4));
		binaryTree.setCompares(0);
		//Test searching method for 100 random numbers
		//Finding the time before the operation is executed
		start = System.nanoTime();
		for(int i=0;i<100;i++) {
			binaryTree.search(binaryTree.getRoot(), intList.get(i));
		}
		//Finding the time after the operation is executed
		end = System.nanoTime();
		//Finding the time difference and converting it into seconds
		sec = (end - start) /1000000F;
		System.out.println("Time for 100 random searches in DynamicTree: "+sec+" ms");
		System.out.println("Average compares for 100 random searches in DynamicTree: "+binaryTree.getCompares()/100);
		binaryTree.setCompares(0);
		System.out.println("Numbers found in searchByRange (Ê=100): ");
		//Test searchByRange method for 100 random numbers with range of 100
		for(int i=0;i<100;i++) {
			binaryTree.searchByRange(binaryTree.getRoot(), intList.get(i),intList.get(i)+100);
		}
		System.out.println("Average compares for 100 random searches by Range(K=100) in DynamicTree: "+binaryTree.getCompares()/100);
		binaryTree.setCompares(0);
		System.out.print("Numbers found in searchByRange (Ê=1000): ");
		//Test searchByRange method for 100 random numbers with range of 1000
		for(int i=0;i<100;i++) {
			binaryTree.searchByRange(binaryTree.getRoot(), intList.get(i),intList.get(i)+1000);
		}
		System.out.println("\nAverage compares for 100 random searches by Range(K=1000) in DynamicTree: "+binaryTree.getCompares()/100);
		binaryTree.setCompares(0);
		
		//**************************************Ordered Array Implementation***************************************************//
		//Create an OrderedArray object from list witch the method inOrder has returned
		int[] orderedList=binaryTree.inOrder(binaryTree.getRoot());
		//Use orderedArray for taking measurements
		orderedArray= new OrderedArray();
		//Test searching method for 100 random numbers
		//Finding the time before the operation is executed
		start = System.nanoTime();
		for(int i=0;i<100;i++) {
			orderedArray.search(orderedList,intList.get(i),0,orderedList.length-1);
		}
		//Finding the time after the operation is executed
		end = System.nanoTime();
		//Finding the time difference and converting it into seconds
		sec = (end - start) /1000000F;
		System.out.println("\nTime for 100 random searches in Ordered Array: "+sec+" ms");
		System.out.println("Average compares for 100 random searches in Ordered Array: "+orderedArray.getCompares()/100);
		orderedArray.setCompares(0);
		
		System.out.print("Numbers found in searchByRange (Ê=100): ");
		//Test searchByRange method for 100 random numbers with range of 100
		for(int i=0;i<100;i++) {
			orderedArray.searchByRange(orderedList,intList.get(i),intList.get(i)+100,0,orderedList.length-1);
		}
		System.out.println("\nAverage compares for 100 random searches by Range(K=100) in OrderedArray: "+orderedArray.getCompares()/100);
		orderedArray.setCompares(0);
		System.out.print("Numbers found in searchByRange (Ê=1000): ");
		//Test searchByRange method for 100 random numbers with range of 1000
		for(int i=0;i<100;i++) {
			orderedArray.searchByRange(orderedList,intList.get(i),intList.get(i)+1000,0,orderedList.length-1);
		}
		System.out.println("\nAverage compares for 100 random searches by Range(K=1000) in OrderedArray: "+orderedArray.getCompares()/100);
		orderedArray.setCompares(0);
		
		//Print order Array to see results
		//orderedArray.printArray(orderedList);
	}
	//Method for reading a binary file and creating a binary search tree
	public static DynamicTree createBinarySearchTree() throws IOException {
		//Set cursor at the beginning of the File for reading
		mFile.seek(0);
		//Total numbers to be read in File
		int totalNums=(int)mFile.length()/4;
		int flagship=0;
		//Create Binary Search Tree
		DynamicTree tree = new DynamicTree(totalNums);
		try {
			//Finding the time before the operation is executed
		    long start = System.nanoTime();
			while(flagship<totalNums) {
				//Read integer from current position in File
				int numberBuff =mFile.readInt();
				//Create a new Node with integer from file and insert it into Binary Search Tree
				Node newNode= new Node(numberBuff);
				tree.insert(newNode);
				//Increase total numbers read
				flagship++;
			}
			//Finding the time after the operation is executed
		    long end = System.nanoTime();
		    //Finding the time difference and converting it into seconds
		    float sec = (end - start) / 1000000F; 
		    System.out.println("\nTime for creating a BinarySearchTree of total "+totalNums+" nodes: "+sec+" ms");
		}
		//Error occurred while reading File
		catch (IOException e) {
			e.printStackTrace();
		}
		return tree;
	}
	//Method for reading a binary file  and creating an array tree 
	public static StaticTree createArrayTree() throws IOException {
		//Set cursor at the beginning of the File for reading
		mFile.seek(0);
		//Total numbers to be read in File
		int totalNums=(int)mFile.length()/4;
		int flagship=0;
		//Create a new Array tree with wanted nodes
		StaticTree tree = new StaticTree(totalNums);
		try {
			//Finding the time before the operation is executed
		    long start = System.nanoTime();
			while(flagship<totalNums) {
				//Read integer from current position in File
				int numberBuff =mFile.readInt();
				tree.insert(numberBuff);
				//Increase total numbers read
				flagship++;
			}
			//Finding the time after the operation is executed
			long end = System.nanoTime();
			//Finding the time difference and converting it into seconds
			float sec = (end - start) / 1000000F; 
			System.out.println("Time for creating an ArrayTree of total "+totalNums+" nodes: "+sec+" ms");			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return tree;
	}
}