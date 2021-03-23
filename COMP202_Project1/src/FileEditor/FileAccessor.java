package FileEditor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class FileAccessor {
	String fileName;
	int pageSize;
	int maxStringLength;
	int pageTuples;
	private RandomAccessFile bFile;
	private int fileAccesses;
	
	public int getFileAccesses() {
		return fileAccesses;
	}
	public void setFileAccesses(int fileAccesses) {
		this.fileAccesses=fileAccesses;
	}
	
	public FileAccessor(String fileName, int pageSize,int maxStringLength) throws Exception {
		
		this.fileName = fileName;
		this.pageSize = pageSize;
		this.maxStringLength=maxStringLength;
		this.fileAccesses=0;
		
			//Creating file for writing Tuple list
			File file = new File("newFile.txt");
			if (file.createNewFile()) {
		     	  System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");	
			}
			bFile = new RandomAccessFile("newFile.txt","rw");
			bFile.setLength(0);
		
		//Number of tuples that can fit a page of bytes
		this.pageTuples = pageSize/(maxStringLength+4);
	}
	//Close file
	public void close() {
		try{
			this.bFile.close();
		} 
		catch (Exception e) {
		
		e.printStackTrace();
		};
	}
	public byte[] read(int pageNumber) throws IOException {
		byte[] b =new  byte[pageSize];
		//Set offset at the start of page of bytes for reading
		bFile.seek(pageNumber*pageSize);
		bFile.read(b);
		return b;
	}
	public void write(byte[] b,int PageNo) throws Exception {
		//Set offset at the start of page of bytes for writing
		 bFile.seek(PageNo*128);
		 bFile.write(b);
	}
	public int stringToByte(ArrayList<Tuple> tuples) throws Exception {
		java.nio.ByteBuffer bb =null;
		int pageNumber = 0;
		byte byteArray[];
		bb = java.nio.ByteBuffer.allocate(pageSize);
		for(int i=0;i<tuples.size();i++) {
			//Get line and word from Tuple
			int line=tuples.get(i).getLine();
			String word=tuples.get(i).getWord();
			//Creating words of 20 bytes
			while(word.length()<maxStringLength) {
				word+=" ";
			}
			bb.put(word.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
			bb.putInt(line);
			//Check if there is enough space to byte buffer for a new array of bytes
			if(bb.remaining()<(maxStringLength+4) || i==(tuples.size()-1)){
				//Writing a page of bytes to file and go to the next page
				byteArray = bb.array();
				write(byteArray, pageNumber);
				pageNumber++;
				bb.clear();
				//Clear byte array avoiding overwrite problems
				Arrays.fill( byteArray, (byte)0);
			}
		}
		System.out.println("Data pages of size "+pageSize+" bytes: "+(pageNumber));
		System.out.println("Total bytes: "+this.pageSize*pageNumber);
		return pageNumber;
	}
	public ArrayList<Tuple> byteToString(byte[] b){
		ByteBuffer bb=ByteBuffer.wrap(b);
		int flagShip=0;
		ArrayList<Tuple> tuples =new ArrayList<Tuple>();
		while(flagShip!=bb.capacity()) {
			if(bb.get()==0) {
				break;
			}
			else {
				bb.position(flagShip);
			}
			byte byteArray[] = new byte[maxStringLength+4];
			// Fills byteArray with maxStringLength bytes from ByteBuffer 
			bb.get(byteArray,0, maxStringLength+4); 
			String word = new String(byteArray,0,maxStringLength,java.nio.charset.StandardCharsets.US_ASCII);
			
			//Move buffer after the word so we can read the line number(int)
			int lineNumber = bb.getInt(flagShip+maxStringLength);
			tuples.add(new Tuple(word,lineNumber));
			flagShip+=(maxStringLength+4);
			bb.position(flagShip);
		}
		return tuples;
	}
	public void searchSerial(String key,int pageNumber) throws IOException {
		ArrayList<Tuple> tuples=new ArrayList<Tuple>();
		String lines="";
		String lastWord;
		for(int i=0;i<pageNumber;i++) {
			fileAccesses++;
			byte[] b =read(i);
			//Create a new tuples List with contents of current page
			tuples.addAll(byteToString(b));
			for(int y=0;y<tuples.size();y++) {
				//Create a new String with tuple word without spaces so we can compare
				String word=tuples.get(y).getWord();
				word=word.trim();
				//Save line of word 
				if(key.equals(word)) {
					lines=lines+tuples.get(y).getLine()+", ";
				}
			}
			//Get 20 bytes last word of page and get rid of empty bytes
			lastWord=tuples.get(tuples.size()-1).getWord();
			lastWord=lastWord.trim();
			//Compare with last word of page to stop serial searching
			if(key.compareTo(lastWord)<0) {
				break;
			}
			//Clear tuple list
			tuples.clear();
			//Clear byte array avoiding overwrite problems
			Arrays.fill( b, (byte)0);
		}
		if(lines.equals("")) {
			System.out.println("Word wasn't found");
			System.out.println("Disk accesses: "+getFileAccesses());
		}
		else {
			System.out.println(key+" is on line(s): "+lines);
			System.out.println("Disk accesses: "+getFileAccesses());
		}
	}
	public ArrayList<Integer> readPreviousPage(String key,int pageNumber) throws IOException{
		fileAccesses++;
		ArrayList<Tuple> tuples=new ArrayList<Tuple>();
		ArrayList<Integer>lines=new ArrayList<Integer>();
		boolean pageBefore=false;
		//Get  content of previous page
		byte[] b=read(pageNumber-1);
		//Create a new tuples List with contents of current page
		tuples.addAll(byteToString(b));
		for(int i=0;i<tuples.size();i++){
			//Get 20 byte word from tuple and get rid of empty bytes
			String word=tuples.get(i).getWord();
			word=word.trim();
			if(key.equals(word)) {
				lines.add(tuples.get(i).getLine());
				//First word of page is equal with key
				if(i==0) {
					pageBefore=true;
				}
			}
		}
		//Read previous page and search for key
		if(pageBefore==true && pageNumber>1) {
			lines.addAll(0,readPreviousPage(key, pageNumber-1));
		}
		tuples.clear();
		return lines;
	}
	public ArrayList<Integer> readNextPage(String key,int pageNumber,int lastPageNum) throws IOException{
		fileAccesses++;
		ArrayList<Tuple> tuples=new ArrayList<Tuple>();
		ArrayList<Integer>lines=new ArrayList<Integer>();
		boolean pageAfter=false;
		//Get  content of next page
		byte[] b=read(pageNumber+1);
		//Create a new tuples List with contents of current page
		tuples.addAll(byteToString(b));
		for(int i=0;i<tuples.size();i++){
			//Get 20 byte word from tuple and get rid of empty bytes
			String word=tuples.get(i).getWord();
			word=word.trim();
			if(key.equals(word)) {
				lines.add(tuples.get(i).getLine());
				//Last word of page is equal with key
				if(i==(tuples.size()-1)) {
					pageAfter=true;
				}
			}
		}
		//Read next page and search for key
		if(pageAfter==true && pageNumber<(lastPageNum+1)) {
			lines.addAll(readNextPage(key, pageNumber+1, lastPageNum));
		}
		tuples.clear();
		return lines;
	}
	public ArrayList<Integer> searchBinary(String key, int firstPage, int lastPage,int totalPages) throws IOException{
		fileAccesses++;
		int middlePage=(firstPage+lastPage)/2;
		//Variables for searching before and after of middle page
		boolean pageBefore=false;
		boolean pageAfter=false;
		ArrayList<Integer> lines=new ArrayList<Integer>();
		ArrayList<Integer> tempLines=new ArrayList<Integer>();
		ArrayList<Tuple> tuples=new ArrayList<Tuple>();
		//Get  content of middle page
		byte[] b=read(middlePage);
		//Create a new tuples List with contents of current page
		tuples.addAll(byteToString(b));
		//Get first and last word of page to compare with key
		String firstWord=tuples.get(0).getWord();
		firstWord=firstWord.trim();
		String lastWord=tuples.get(tuples.size()-1).getWord();
		lastWord=lastWord.trim();

		//Final page witch might contain key
		if((firstPage+1)==lastPage) {
			for(int i=0;i<tuples.size();i++) {
				//Get 20 bytes word and get rid of empty bytes
				String word=tuples.get(i).getWord();
				word=word.trim();
				if(key.equals(word)) {
					lines.add(tuples.get(i).getLine());
					//First word of page is equal with key
					if(i==0) {
						pageBefore=true;
					}
					//Last word of page is equal with key
					else if(i==(tuples.size()-1)) {
						pageAfter=true;
					}	
				}
			}
		}
		else {
			//Case if key is before middle page
			if(firstWord.compareTo(key)>0 && middlePage>0){
				lines.addAll(searchBinary(key,firstPage,middlePage,totalPages));
			}	
			//Case if key is after middle page
			else if(lastWord.compareTo(key)<0 && middlePage<totalPages-1) {
				lines.addAll(searchBinary(key,middlePage,lastPage,totalPages));
			}
			//Search for key in middle page
			else {
				for(int i=0;i<tuples.size();i++) {
					//Get 20 bytes word and get rid of empty bytes
					String word=tuples.get(i).getWord();
					word=word.trim();
					if(key.equals(word)) {
						lines.add(tuples.get(i).getLine());
						//First word of page is equal with key
						if(i==0) {
						pageBefore=true;
						}
						//Last word of page is equal with key
						else if(i==(tuples.size()-1)) {
						pageAfter=true;
						}	
					}
				}
			}
		}
		//Search previous page
		if(pageBefore==true && middlePage>0) {
			tempLines.addAll(readPreviousPage(key, middlePage));
			//Add tempLines before lines for printing in order
			lines.addAll(0,tempLines);
			tempLines.clear();
		}
		//Search next page
		if(pageAfter==true && middlePage<totalPages) {
			tempLines.addAll(readNextPage(key, middlePage,totalPages));
			lines.addAll(tempLines);
			tempLines.clear();
		}
		tuples.clear();
		return lines;
	}
}


