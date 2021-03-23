import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import FileEditor.FileAccessor;
import FileEditor.Tuple;
import List.DoublyLinkedList;
import List.Node;
import tuc.eced.cs201.io.StandardInputRead;

public class Console {
	
	static DoublyLinkedList<String> list;
	static ArrayList<Tuple> tuples;
	private static Scanner scan;
	
	
	
	public static  void main(String[] Args) throws Exception {
		
		//Constants for String and Line length and Page Size	
		final int wordMaxLength=20;
		final int wordMinLength=4;
		final int stringMaxLength= 80;
		final int pageSize=128;
		//Reader for user's input
		StandardInputRead reader= new StandardInputRead();
		String userSentence;
		String searchWord;
		//Argument for opening File
		String fName=Args[0];
			
		//Our file for reading and writing
		RandomAccessFile mFile=new RandomAccessFile(fName,"rw");
		//Variables for the first part of the exercise	
		list=new DoublyLinkedList<String>();
		openAndCreateList(mFile,fName,stringMaxLength);
		boolean enable=false;
		//Set a temporary node so we can cross the list
		Node<?> globalPointer=list.getHead();
			
		//Creating Tuples Sorted List and FileAccessor object
		FileAccessor tuplesFile=null;
		tuples= new ArrayList<Tuple>();
		tuples=createTupleList(wordMaxLength,wordMinLength);
		int numOfPages=0;
		
		//Variable for 30 random strings testing
		int tempAccesses=0;
		int choice=0;
		
		/******************************PRINT MENU TO USER**************************/	

			char option = 0;
			while(option!='x' && option!='q') {
				
				printMenu();
				//Get users option as a character
				scan = new Scanner(System.in);
				option = scan.next().charAt(0);
			
				switch(option) {
					case '^':
						globalPointer=list.getHead();
						System.out.println("Cursor moved to the first line");
						break;
					case '$':
						while(globalPointer.getNext()!=null) {
							globalPointer=globalPointer.getNext();
						}
						System.out.println("Cursor moved to the last line");
						break;
					case '-':
						globalPointer=list.moveUp(globalPointer);
						break;
					case '+':
						globalPointer= list.moveDown(globalPointer);
						break;
					case 'a':
						//Get users's input and add it after current node
						userSentence=reader.readString("Please type your sentence: ");
						globalPointer=list.addAfter(userSentence,globalPointer);
						break;
					case 't':
						//Get users's input and add it before current node
						userSentence=reader.readString("Please type your sentence: ");				
						globalPointer=list.addBefore(userSentence, globalPointer);
						break;
					case 'd':
						globalPointer=list.deleteNode(globalPointer);
						break;
					case 'l':
						//Enable is false by default so lines are not enumerated
						list.printList(enable);
						break;
					case 'n':
						if(enable==false) {
							enable=true;
							System.out.println("Lines enumeration: {ENABLED}");
						}
						else {
							enable=false;
							System.out.println("Lines enumeration: {DISABLED}");
						}
						break;
					case 'p':
							//Enable is false by default so lines are not enumerated
							list.printNode(enable, globalPointer);
						break;
					case 'q':
						System.out.println("Exit Program");
						//Close our Random Access File
						mFile.close();
						System.exit(0);
						break;
					case 'w':
						try {
							saveFile(mFile) ;
							//Create a new list with new contents of File after saving
							openAndCreateList(mFile,fName,stringMaxLength);
							//Update Tuple List also
							tuples=createTupleList(wordMaxLength,wordMinLength);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						break;
					case 'x':
						try {
							saveFile(mFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("Exit Program");
						//Close our Random Access File
						mFile.close();
						System.exit(0);
						break;
					case '=':
						int number=list.findNodeNumber(globalPointer);
						if(number!=0) {
							System.out.println("Line number "+ number);
						}
						//Node was not found
						else {
							System.out.println("Error");
						}
						break;
					case '#':
						list.totalCharAndLines();
						break;
					case 'c':
						tuplesFile= new FileAccessor("DOKIMH",pageSize,wordMaxLength);
						//Method returns number of byte pages in File
						numOfPages=tuplesFile.stringToByte(tuples);
						break;
					case 'v':
						//Check if ndx file was created in option c
						if(numOfPages==0||tuplesFile==null) {
							System.out.println("Error,File is not created.Option 'c' must be picked ");
							break;
						}
						ArrayList<Tuple> convTuples =new ArrayList<Tuple>();
						//Create Tuples List with contents of one page of 128 bytes
						for(int i=0;i<numOfPages;i++) {
							byte[] b =tuplesFile.read(i);
							//Add Tuples list to the previous one for every repetition
							convTuples.addAll(tuplesFile.byteToString(b));
							//Clear byte array for avoiding overwriting problems
							Arrays.fill( b, (byte)0);
						}
						for(int i=0;i<convTuples.size();i++) {
							System.out.println(convTuples.get(i).getWord()+convTuples.get(i).getLine());
						}
						break;
					case 's':
						//Check if ndx file was created in option c
						if(numOfPages==0||tuplesFile==null) {
							System.out.println("Error,File is not created.Option 'c' must be picked ");
							break;
						}
						//Extra function for 30 Random String Test
						choice=reader.readPositiveInt("Type '1' for Serial search or '2' for 30 Random String test: ");
						
						if(choice==1) {
							//Get user's Input and Serial search
							searchWord=reader.readString("Type word for search: ");
							tuplesFile.searchSerial(searchWord,numOfPages);
							tuplesFile.setFileAccesses(0);
						}
						else {
							//******************RANDOM STRING SERIAL SEARCH******************//
							tempAccesses=0;
							for(int i=0;i<30;i++) {
									tuplesFile.searchSerial(RandomString.getAlphaNumericString(10), numOfPages);
									tempAccesses+=tuplesFile.getFileAccesses();
									tuplesFile.setFileAccesses(0);
							}	
							tempAccesses=tempAccesses/30;
							System.out.println("AVG Accesses: "+tempAccesses);
						}
						break;	
					case 'b':
						//Check if ndx file was created in option c
						if(numOfPages==0||tuplesFile==null) {
							System.out.println("Error,File is not created.Option 'c' must be picked ");
							break;
						}
						//Extra function for 30 Random String Test
						choice=reader.readPositiveInt("Type '1' for Binary search or '2' for 30 Random String test: ");
						if(choice==1) {
							//Get user's Input and Binary search
							searchWord=reader.readString("Type word for search: ");
							ArrayList<Integer> lines=tuplesFile.searchBinary(searchWord,0,(numOfPages),(numOfPages));
							//User's word was found
							if(!lines.isEmpty()) {
								System.out.print(searchWord+" is on line(s): ");
								for(int i=0;i<lines.size();i++) {
									System.out.print(lines.get(i)+", ");
								}
								System.out.println("\nDisk accesses: "+tuplesFile.getFileAccesses());
							}
							else {
								System.out.println("Word wasn't found");
								System.out.println("Disk accesses: "+tuplesFile.getFileAccesses());
							}
							tuplesFile.setFileAccesses(0);
						}
						else {
							//********************RANDOM STRING BINARY SEARCH********************//
							tempAccesses=0;
							for(int i=0;i<30;i++) {
								tuplesFile.searchBinary(RandomString.getAlphaNumericString(10),0,numOfPages,numOfPages );
								tempAccesses+=tuplesFile.getFileAccesses();
								tuplesFile.setFileAccesses(0);
							}
							tempAccesses=tempAccesses/30;
							System.out.println("AVG Accesses: "+tempAccesses);
						}
						break;						
					default :
						System.out.println("Bad command. User option : "+option+" ignored.");
				}
			}	
		}
	public static void printMenu() {
		System.out.println("\n                        Please type your option");
		System.out.println("=======================================================================");
		System.out.println("Go to the first line '^'");
		System.out.println("Go to the last line '$'");
		System.out.println("Go up one line '-'");
		System.out.println("Go down one line '+'");
		System.out.println("Add new line after current line 'a'");
		System.out.println("Add new line before current line 't'");
		System.out.println("Delete current line 'd'");
		System.out.println("Print all lines 'l'");
		System.out.println("Toggle whether line numbers are displayed when printing all lines 'n'");
		System.out.println("Print current line 'p'");
		System.out.println("Quit without save 'q'");
		System.out.println("Write file to disk 'w'");
		System.out.println("Exit with save 'x'");
		System.out.println("Print current line number '='");
		System.out.println("Print number of lines and characters '#'");
		System.out.println("Create ndx file 'c'");
		System.out.println("Print index (word, line number) 'v'");
		System.out.println("Print lines of word serial 's'");
		System.out.println("Print lines of word binary search 'b'");	
		System.out.println("=======================================================================");
		System.out.print("CMD> ");	
	}
	public static void openAndCreateList(RandomAccessFile mFile,String fName,int stringLength) throws FileNotFoundException {
		try {
			//Read first line of the File
			String stringBuff = mFile.readLine();
			while(stringBuff != null) {
				//Add to list only wanted size lines
				if(stringBuff.getBytes().length>stringLength) {
					System.out.println("Line bigger than 80 characters");
				}
				else{
					list.add(stringBuff);
				}
				//Read next line of the File
				stringBuff = mFile.readLine();
			}
		}	
		catch(FileNotFoundException ex) {
	        System.out.println("Unable to open file" +fName + "'");                
	    }
	    catch(IOException ex) {
	        System.out.println("Error reading file" + fName + "'");                  
	      }
	}
	private static void saveFile(RandomAccessFile file) throws IOException {
		//Node for crossing the list
		Node<?> temp = list.getHead();
		
		String stringBuffer=null;
		Character endOfLine='\n';
		//Empty file
		file.setLength(0);
		while(temp!=null) {
			stringBuffer = temp.getData().toString();
			//Write a line to file
			file.writeBytes(stringBuffer);
			//Write new line char next to line
			file.writeChar(endOfLine);
			//Get next line
			temp = temp.getNext();
		}
		System.out.println("File saved");
	}
	public static ArrayList<Tuple> createTupleList(int maxLength,int minLength) {
		Node<?> temp=list.getHead();
		
		//Data for creating Tuple
		String word;
		int line=1;
		
		String data=null;
		String delims="\\P{Alpha}+";
		String[] result=null;
		
		while(temp!=null) {
			//Split String to words
			data=(String) temp.getData();
			result=data.split(delims);
			
			for(int x=0;x<result.length;x++) {
				//Get word
				word=result[x];
				//Create Tuple only with wanted size words and add it to List
				if(word.length()>minLength) {		
					if(word.length()>maxLength) {
						//If word is bigger than maxLength cut it
						word=word.substring(0,maxLength);
					}
					tuples.add(new Tuple(word,line));				
				}
			}
			//Get next line of File
			temp=temp.getNext();
			line++;	
		}
		//Sort list in alphabetical order
		Collections.sort(tuples);
		return tuples;
	}

}