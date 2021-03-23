package List;

//Class for creating List with contents of selected File
public class DoublyLinkedList<T> {
	
		private Node<?> head;
		private int size;
		
		public DoublyLinkedList(Node<?> h) {
			this.head=h;
		}
		public DoublyLinkedList() {
			this.head=null;
			this.size=0;
		}
		
		public int getSize() {
			return size;
		}
		
		public Node<?> getHead() {
			return head;
		}
		public void setHead(Node<?> node) {
			this.head=node;
		}
		public boolean isEmpty() {
            // Returns true if we have 0 nodes
            return size == 0;
        }
		//	Method for adding Node after last node 
		//  	of the current list
		public void add(T aValue ){
            Node<T> theNode = new Node<T>(aValue);
            Node<?> temp = getHead();
            
            //If list is empty create a new one
            if(isEmpty()) {
            	setHead(theNode);
            }
            else {
            	//Finding last node and add 
            	while(temp.getNext()!=null) {
            		temp=temp.getNext();
            	}
                temp.setNext(theNode);
                theNode.setPrev(temp);
            }
            size++;
        }
		//	Method for adding Node after
		// 	current Node
		public Node<?> addAfter(T aValue,Node<?> currentNode ){
            Node<T> theNode = new Node<T>(aValue);
            //If list is empty create a new one
            if(isEmpty()) {
            	setHead(theNode);
            	System.out.println("New list was created");
            	currentNode=head;
            }
            else {
            	theNode.setPrev(currentNode);
            	//Check if current is the tail of the list
            	if(currentNode.getNext()==null) {
            		currentNode.setNext(theNode);
            		theNode.setNext(null);
            	}
            	else {
            		theNode.setNext(currentNode.getNext());
            		currentNode.getNext().setPrev(theNode);
            		currentNode.setNext(theNode);
            	}
            	System.out.println("Line was added");
            }
           
            size++;
            return currentNode;
        }
		//	Method for adding Node before
		// 	current Node
		public Node<?> addBefore(T aValue,Node<?> currentNode) {
			Node<T> theNode=new Node<T>(aValue);
			//If list is empty create a new one
            if(isEmpty()) {
            	setHead(theNode);
            	System.out.println("New list was created");
            	currentNode=head;
            }
            else {
            	theNode.setNext(currentNode);
            	
            	//Check if current is the head of the list
            	if(currentNode.equals(getHead())) {
            		currentNode.setPrev(theNode);
            		//Now theNode is the new head of the list	
            		setHead(theNode);
            	}
            	else {
            		theNode.setPrev(currentNode.getPrev());
            		currentNode.getPrev().setNext(theNode);
            		currentNode.setPrev(theNode);
            	}
            	System.out.println("Line was added");
            }
            size++;
			return currentNode;
		}
		
		public Node<?> deleteNode(Node<?> temp) {
			if(isEmpty()) {
				System.out.println("Could not execute, empty list");
				return null;
			}
			//Case if temp is the first node
			else if(temp.equals(getHead())) {
				
				setHead(temp.getNext());
				temp=temp.getNext();
			}
			
			//Case if temp is the last node
			else if(temp.getNext()==null) {
				temp=temp.getPrev();
				temp.setNext(null);
			}
			else {
				Node<?> prevNode=temp.getPrev();
				temp.getNext().setPrev(prevNode);
				prevNode.setNext(temp.getNext());
				temp=temp.getNext();
			}
			
			System.out.println("Line was deleted");
			size--;
			return temp;
		}
		
		//Deleting list after executing the program
		public void clear(){
            head = null;
            size = 0;
        }
		//	Print current's Node data
		public void printNode(boolean enableNum,Node<?> currentNode) {
			if(isEmpty()) {
				System.out.println("Sorry empty list");
				return ;
			}
			//Node for crossing the list
			Node<?> temp=head;
			
			//Find number of line
			int i=findNodeNumber(currentNode);

			while(!(temp.equals(currentNode))) {
				temp=temp.getNext();
			}
			//If enable=true lines are enumerated
			if(enableNum==true) {
				System.out.println(i+") "+currentNode.getData());	
			}
			else {
				System.out.println(currentNode.getData());
			}
		}
		
		public void printList(boolean enableNum) {
			if(isEmpty()) {
				System.out.println("Sorry empty list");
				return ;
			}
			//If enable=true lines are enumerated
			if(enableNum==true) {
				//Flagship for lines
				int i =1;
				Node<?> temp=getHead();
				while(temp!=null) {
					System.out.println(i+") "+temp.getData());
					i++;
					temp=temp.getNext();
				}
			}
			else {
				Node<?> temp=getHead();
				while(temp!=null) {
					System.out.println(temp.getData());
					temp=temp.getNext();
				}
				
			}
			
		}
		//	Search list for Node and return it's
		// 	position in List
		public int findNodeNumber(Node<?> theNode) {
			//Node for crossing the list
			Node<?> temp = getHead();
			//Line counter
			int i=0;
			
			if(isEmpty()) {
				System.out.println("Sorry empty list");
			}
			else {
				i=1;
				//Crossing the list for finding the node
				while(temp!=theNode) {
					temp=temp.getNext();
					i++;
				}
				if(temp==null) {
					System.out.println("Node wansn't found");
				}
			}
			return i;
		}
		//Get Previous of current Node
		public Node<?> moveUp(Node<?> temp) {
			//Check if file is empty
			if(temp==null) {
				System.out.println("File is empty.Cursor cannot move");
			}
			else if(temp.equals(getHead())) {
				System.out.println("Cursor is at the first line.There is not any previous line to go");
			}
			else {
				temp=temp.getPrev();
				System.out.println("Cursor moved to the previous line");
			}
			return temp;
			
		}
		//Get Next of current node
		public Node<?> moveDown(Node<?> temp) {
			//Check if file is empty
			if(temp==null) {
				System.out.println("File is empty.Cursor cannot move");
			}
			else if(temp.getNext()==null) {
				System.out.println("Cursor is at the last line.There is not any next line to go");
			}
			else {
				temp=temp.getNext();
				System.out.println("Cursor moved to the next line");
			}
			return temp;
		}
		//	Print size of list and sum all
		// 	nodes data(string) length
		public void totalCharAndLines() {
			long counter=0;
			
			Node<?> temp=getHead();
			while(temp.getNext()!=null) {
				counter+=temp.getData().toString().length();
				temp=temp.getNext();
			}
			System.out.println("Total lines: "+getSize()+" Total Characters: "+counter);
		}
		
}
