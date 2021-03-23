package BinSearchTree;


//Class for implementation of a Binary Search Tree
//by dynamic memory allocation
public class BinarySearchTree {
	private Node root;
	private int compares;
	//Constructor for creating BinarySearchTree objects
    public BinarySearchTree(){
        this.root = null;
        compares=0;
    }
    //Getter and Setter methods for member variables
	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
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
	//Method for increasing compares by a specific index
	public void increaseComparesBy(int index) {
		compares+=index;
	}
	//Insert a new Node into the Tree
    public void insert(Node newNode){
       //Get start of Tree and set a temporary Node
       //for crossing the Tree
        Node x =getRoot();
        Node temp = null;
        increaseComparesBy(2);
        while(increaseCompares() && x != null){
            temp = x;
            increaseComparesBy(1);
            //Go to left half of tree
            if(increaseCompares() && newNode.getValue() < x.getValue()) {
                x = x.getLeft();
                increaseComparesBy(1);
            }
            //Go to half right of tree
            else {
                x = x.getRight();
                increaseComparesBy(1);
            }
        }
        //Tree is empty. New Node is now root of Tree
        if(increaseCompares() && temp == null){
            setRoot(newNode);
            increaseComparesBy(1);
        }
        //Compare values and place newNode either left or right of previous Node
        else if(increaseCompares() && newNode.getValue() < temp.getValue()) {
            temp.setLeft(newNode);
            increaseComparesBy(1);
        }
        else {
            temp.setRight(newNode);
            increaseComparesBy(1);
        }
    }
  //Method for searching a key into binary search Tree by recursion
  	public Node search(Node n,int key){
  		//Key was found or there is not any new Node to search
  		if(increaseCompares() && (n == null || key == n.getValue())){
  			return n;
        }
  	    //Get left subtree for searching
        if(increaseCompares() && key < n.getValue()){
            return search(n.getLeft(),key);
        }
        //Get right subtree for searching
        else{
            return search(n.getRight(),key);
        }
    }
  	//Method for deleting a key from Binary Search Tree
  	//This method calls recursive method deleteTreeNode
  	public void delete(int data){
  	    root = deleteTreeNode(root ,data);
  	    increaseComparesBy(1);
  	}
  	//Recursive method for crossing a binary search tree
  	//and deleting a specific Node with data
  	private Node deleteTreeNode(Node root, int data) {
  	    //Get root of tree for crossing it
  		Node cur = root;
  	    increaseComparesBy(1);
  	    //Tree is empty
  	    if(increaseCompares()&&cur == null){
  	        return cur;
  	    }
  	    //Search first subtree first for deletion
  	    if(increaseCompares()&&cur.getValue() > data){            
  	        cur.setLeft(deleteTreeNode(cur.getLeft(), data));
  	        increaseComparesBy(1);
  	    }
  	    //Search right subtree for deletion
  	    else if(increaseCompares()&&cur.getValue() < data){
  	        cur.setRight(deleteTreeNode(cur.getRight(), data));
  	    }
  	    //Key was found in current Node
  	    else{
  	    	//Current node is a leaf
  	        if(increaseCompares()&&(cur.getLeft() == null && cur.getRight() == null)){
  	            cur = null;
  	            increaseComparesBy(1);  
  	        }
  	        //Current node has only a left child
  	        else if(increaseCompares()&&cur.getRight() == null){
  	            cur = cur.getLeft();
  	            increaseComparesBy(1);
  	        }
  	        //Current node has only a right child
  	        else if(increaseCompares()&&cur.getLeft() == null){
  	            cur = cur.getRight();
  	            increaseComparesBy(1);
  	        }
  	        else{
  	            Node temp  = findMinFromRight(cur.getRight());
  	            cur.setValue(temp.getValue());
  	            cur.setRight(deleteTreeNode(cur.getRight(), temp.getValue()));
  	            increaseComparesBy(3);
  	        }
  	    }
  	    return cur;
  	}
  	//Find the min key 
  	private Node findMinFromRight(Node node) {
  	    while(increaseCompares()&&node.getLeft() != null){
  	        node = node.getLeft();
  	        increaseComparesBy(1);
  	    }
  	    return node;
  	}
}