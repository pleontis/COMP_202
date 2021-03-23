package List;
//Class for Nodes of the List

public class Node<T> implements Comparable<T> {
	private T data;
	private Node<?> prev;
	private Node<?> next;
	
	public Node(T data) {
		this.data=data;
		
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data=data;
	}
	
	public Node<?> getPrev() {
		return this.prev;
	}
	public void setPrev(Node<?> prev) {
		this.prev=prev;
	}
	
	public Node<?> getNext() {
		return this.next;
	}
	public void setNext(Node<?> next) {
		this.next=next;
	}
	
	/*Method for comparing string data of Node
	* and return results
	*/
	public int compareTo(T o) {
		return this.getData().toString().compareTo(o.toString());
	}
}
