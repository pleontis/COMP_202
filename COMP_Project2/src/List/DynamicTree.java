package List;


//Class for creating a Binary Search Tree
public class DynamicTree implements Tree<Node> {
	//Start of the Tree
	private Node root;
	private int compares;
	//Array for storing ordered data from inOrder() method
	private int [] orderedArray;
	private int oArrayAvail=0;
	//Constructor for creating Binary Search Tree Objects
	public DynamicTree(int N) {
		this.root=null;
		compares=0;
		orderedArray= new int[N];
	}
	//Getter and Setter Method for root and compares variable
	public Node getRoot() {
		return this.root;
	}
	public void setRoot(Node root) {
		this.root=root;
	}
	public int getCompares(){
		return this.compares;
	}
	public void setCompares(int compares) {
		this.compares=compares;
	}
	//Method for increasing compares number by 1 for
	//testing numbers
	public boolean increaseCompares() {
		this.compares++;
		return true;
	}
	//Insert a new Node into the Tree
    public void insert(Node newNode){
       //Get start of Tree and set a temporary Node
       //for crossing the Tree
        Node x =getRoot();
        Node temp = null;
        while(increaseCompares() && x != null){
            temp = x;
            compares++;
            //Go to left half of tree
            if(increaseCompares() && newNode.getInfo() < x.getInfo()) {
                x = x.getLeft();
                compares++;
            }
            //Go to half right of tree
            else {
                x = x.getRight();
                compares++;
            }
        }
        //Tree is empty. New Node is now root of Tree
        if(increaseCompares() && temp == null){
            setRoot(newNode);
            compares++;
        }
        //Compare values and place newNode either left or right of previous Node
        else if(increaseCompares() && newNode.getInfo() < temp.getInfo()) {
            temp.setLeft(newNode);
            compares++;
        }
        else {
            temp.setRight(newNode);
            compares++;
        }
    }
	//Recursive function for printing 
    //info value for all nodes of Tree 
    //after argument node
	public int[] inOrder(Node node){
		//Case for exit recursive function. node is a leaf of the tree
		if(node == null){
			return orderedArray;
		}
		//Search left subtree
		inOrder(node.getLeft()); 
		//Add to array the info of current Node
		orderedArray[oArrayAvail]= node.getInfo();
		oArrayAvail++;
		//Search right subtree
		inOrder(node.getRight());
		return orderedArray; 
	 }
	//Method for searching a key into binary search Tree by recursion
	public Node search(Node n,int key){
		//Key was found or there is not any new Node to search
		if(increaseCompares() && (n == null || key == n.getInfo())){
            return n;
        }
	    //Get left subtree for searching
        if(increaseCompares() && key < n.getInfo()){
           return search(n.getLeft(),key);
        }
        //Get right subtree for searching
        else{
            return search(n.getRight(),key);
        }
    }
	//Recursive method for searching tree and printing all numbers 
	//which are between the given range [firstNum,lastNum]
	public  void searchByRange(Node node,int k1, int k2) {
		//Check if user typed values k1>k2 and swap if needed
		if(k1>k2) {
			//Temporary variable for storing k1 value
			int temp=k1;
			//Swap values of k1 & k2
			k1=k2;
			k2=temp;
		}
		//Base case
        if(increaseCompares() && node == null) { 
        	return; 
        } 
       //Recurse for left subtree first  
       if(increaseCompares() && k1 < node.getInfo()) { 
    	   searchByRange(node.getLeft(), k1, k2); 
       } 
       //If node's data lies in range, then print data
       if(increaseCompares() && (k1 <= node.getInfo() && k2 >= node.getInfo())) { 
    	   System.out.print(node.getInfo() + ", "); 
       } 
       //Recurse for right subtree
       if(increaseCompares() && k2 > node.getInfo()) { 
           searchByRange(node.getRight(), k1, k2); 
       } 
   }
	

}
