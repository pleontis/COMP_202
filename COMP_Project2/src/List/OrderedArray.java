package List;


//Class for creating order arrays from inOrder method
//in BinarySearchTree and ArrayTree classes
public class OrderedArray{
	private int compares;
	
	//Constructor Methods for creating objects of this Class
	public OrderedArray() {
		compares=0;
	}
	//Getter and Setter methods for compares variable
	public int getCompares() {
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
	//Method for searching binary an ordered list by recursion
	//and returns the node of wanted key(If it has been found)
	public int search(int[] oArray,int key,int start,int end) {
		if(end==start+1) {
			return -1;
		}
		else {
			int middle=(start+end)/2;
			//Three-way comparison
			//Key is in lower subset
			if(increaseCompares() && key<oArray[middle]) {
				return search(oArray,key,start,middle);
			}
			//Key is in upper subset
			else if(increaseCompares() && key>oArray[middle]) {
				return search(oArray,key,middle,end);
			}
			//Key has been found
			else{
				return middle;
			}	
		}
	}
	//Recursive method for searching Array and printing all the numbers
	//which are between the given range[k1,k2]
	public void searchByRange(int[] oArray,int k1,int k2,int start,int end) {
		//Check if user typed values k1>k2 and swap if needed
		if(k1>k2) {
			//Temporary variable for storing k1 value
			int temp=k1;
			//Swap values of k1 & k2
			k1=k2;
			k2=temp;
		}
		int middle=(start+end)/2;
		//Base case
		if(increaseCompares() && end==start+1) {
			return;
		}
		//Recurse for lower subset first
		if(increaseCompares() && k1<oArray[middle]) {
			searchByRange(oArray, k1, k2, start, middle);
		}
		//If node's data lies in range, then print data
		if(increaseCompares() && (k1<=oArray[middle] && k2>=oArray[middle])) {
			System.out.print(oArray[middle]+ ", ");
		}
		//Recurse for upper subset
		if(increaseCompares() && k2>oArray[middle]) {
			searchByRange(oArray, k1, k2, middle, end);
		}
	}
	//Method for printing an Array
	public void printArray(int [] oArray) {
		for(int i=0;i<oArray.length;i++) {
			System.out.print(oArray[i]+", ");
		}
	}
}