package List;

//Class for creating Nodes of Binary Search Tree
public class Node {
	
	private int info;
	private Node left;
	private Node right;
	
	//Constructor Method for creating new Node Objects
	public Node(int info) {
		this.info=info;
		this.left=null;
		this.right=null;
	}
	//Setters 
	public void setInfo(int info) {
		this.info=info;
	}
	public void setLeft(Node left) {
		this.left=left;
	}
	public void setRight(Node right) {
		this.right=right;
	}
	//Getters
	public int getInfo() {
		return this.info;
	}
	public Node getLeft() {
		return this.left;
	}
	public Node getRight() {
		return this.right;
	}
	
}
