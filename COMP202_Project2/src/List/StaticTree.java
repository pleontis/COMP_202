package List;

public class StaticTree implements Tree<Integer>{
	private int avail;
	private int root;
	private int[] treeKey;
	private int[] treeLeft;
	private int[] treeRight;
	private int compares;
	//Array for storing ordered data from inOrder() method
	private int[] orderedArray;
	private int oArrayAvail=0;
	//Constructor for  creating ArrayTree objects
	public StaticTree (int depth) {
		initializeStack(depth);
		//Initialize root for an empty tree
		this.root=-1;
		//First available position when creating a new Tree
		this.avail=0;
		compares=0;
		orderedArray=new int[depth];
	}
	//Getter methods
	public int getRoot() {
		return this.root;
	}
	public int getAvail() {
		return this.avail;
	}
	public int[] getTreeKey() {
		return this.treeKey;
	}
	public int[] getTreeLeft() {
		return this.treeLeft;
	}
	public int[] getTreeRight() {
		return this.treeRight;
	}
	public int getCompares() {
		return this.compares;
	}
	//Get number in position index of treeKey array 
	public int getKey(int position) {
		return this.treeKey[position];
	}
	//Get number in position index of treeLeft array
	public int getLeft(int position) {
		return this.treeLeft[position];
	}
	//Get number in position index of treeRight array
	public int getRight(int position) {
		return this.treeRight[position];
	}
	//Setter methods 
	public void setRoot(int line) {
		this.root=line;
	}
	public void setCompares(int compares) {
		this.compares=compares;
	}
	//Set value in position index of treeKey array
	public void setKey(int position,int key) {
		this.treeKey[position]=key;
	}
	//Set value in position index of treeLeft array
	public void setLeft(int position,int key) {
		this.treeLeft[position]=key;
	}
	//Set value in position index of treeRight array
	public void setRight(int position,int key) {
		this.treeRight[position]=key;
	}
	//Method for increasing compares number by 1 for
	//testing numbers
	public boolean increaseCompares() {
		this.compares++;
		return true;
	}
	//Method for initializing Array Tree
	public void initializeStack(int depth) {
		//Initialize arrays for creating Array Tree
		treeKey= new int [depth];
		treeLeft=new int [depth];
		treeRight= new int [depth];
		for(int i=0;i<depth;i++) {
			//Right column is next available place
			setRight(i,i+1);
			setLeft(i,-1);
			//End of array
			if(i==depth-1) {
				setRight(i,-1);
			}
		}
	}
	//Method for inserting new node to ArrayTree
	public void insert(Integer key){
		int start=this.root;
		int currentPos=avail;
		int temp=-1;
		while(increaseCompares() && start!=-1) {
			temp=start;
			compares++;
			if(increaseCompares() && key<getKey(start)) {
				start=getLeft(start);
				compares++;
			}
			else {
				start=getRight(start);
				compares++;
			}
		}
		//Tree is empty. New Node is now root of tree
		if(increaseCompares() && temp==-1) {
			setRoot(0);
			compares++;
			setKey(avail, key);
			compares++;
			avail=getRight(currentPos);
			compares++;
			setRight(currentPos,-1);
			compares++;
		}
		//Compare values and place newNode either left or right of previous Node
		else if(increaseCompares() && key<getKey(temp)) {
			//Set previous (parent) Node of new inserted Node
			setLeft(temp, currentPos);
			compares++;
			//Initialize line of new Node
			setKey(currentPos,key);
			compares++;
			avail=getRight(currentPos);
			compares++;
			setRight(currentPos,-1);
			compares++;
		}
		else {
			//Set previous (parent) Node of new inserted Node
			setRight(temp, currentPos);
			compares++;
			//Initialize line of new Node
			setKey(currentPos,key);
			compares++;
			avail=getRight(currentPos);
			compares++;
			setRight(currentPos,-1);
			compares++;
		}
	}
	//Method for searching a key into
	//Tree by recursion
	public Integer search(Integer currentLine, int key) {
		//Key was found or there is not 
		//any new Node to search
		if(increaseCompares() && (currentLine==-1 || key==getKey(currentLine))) {
			return currentLine;
		}
		//Get left subtree for searching	
		if(increaseCompares() && key<getKey(currentLine)) {
			return search(getLeft(currentLine),key);
		}
		//Get right subtree for searching
		else {
			return search(getRight(currentLine),key);
		}
		
	}
	//Recursive function for printing info value for 
	//all nodes of Tree after argument currentLine
	public int[] inOrder(Integer currentLine){
	    		
		//Case for exit recursive function. CurrentLine is a leaf of the tree
		if (currentLine == -1){
	    	 return orderedArray; 
	    }
		//Search left subtree
		inOrder(getLeft(currentLine));
	    //Add to array the info of current Node
	    orderedArray[oArrayAvail]=getKey(currentLine);
	    oArrayAvail++;
	    //Search right subtree
	    inOrder(getRight(currentLine));
	    return orderedArray;
		
	}
	//Recursive method for searching tree and printing all numbers 
	//which are between the given range [firstNum,lastNum]
	public void searchByRange(Integer currentLine, int k1, int k2) {
		//Check if user typed values k1>k2 and swap if needed
		if(k1>k2) {
			//Temporary variable for storing k1 value
			int temp=k1;
			//Swap values of k1 & k2
			k1=k2;
			k2=temp;
		}
		//Base case
		if(increaseCompares() && currentLine==-1) {
			return;
		}
		//Recurse for left subtree first
		if(increaseCompares() && k1<getKey(currentLine)) {
			searchByRange(getLeft(currentLine),k1,k2);
		}
		//If node's data lies in range, then print data
		if(increaseCompares() && (k1<=getKey(currentLine) && k2>=getKey(currentLine))) {
			System.out.print(getKey(currentLine)+ ",");
		}
		//Recurse for right subtree
		if(increaseCompares() && k2>getKey(currentLine)) {
			searchByRange(getRight(currentLine),k1,k2);
		}
	}
}
