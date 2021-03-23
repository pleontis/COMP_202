package BinSearchTree;

public class Node {
	private Node left;		//Left child of Node
	private Node right;		//Right child of Node
	private int value;		//Key of Node
    
    //Constructor for creating Node objects
    public Node(int value){
        this.value = value;
        this.left = null;
        this.right = null;  
    }
    //Getters and Setters Methods for member variables of class
	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
