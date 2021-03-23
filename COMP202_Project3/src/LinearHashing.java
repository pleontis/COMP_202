
public class LinearHashing {
	private Bucket[] hBuckets;	//Pointer to the hash buckets
	private float maxThreshold;	//Max load factor threshold
	private float minThreshold;	//Min load factor threshold
	
	private int sizeOfBucket;		//Max number of keys in Bucket
	private int numOfKeys;			//Number of keys stored in the hash table
	private int availSpace;			//Total space available for keys
	private int nextBucketSplit;	//Pointer to the next Bucket to be split
	private int numOfBuckets;		//Number of Buckets in hash table
	private int M;					//The M used for the hash function
	private int minBuckets;			//Min number of Buckets that this has table can have
	private int compares;			//Variable for taking measurements
	
	//Constructor method for creating a LinearHashing Object
	//Initializing variables
	public LinearHashing(int sizeOfBucket, int initPages, float maxThreshhold, float minThreshold) { 

		this.sizeOfBucket=sizeOfBucket;
		numOfKeys = 0;
		nextBucketSplit = 0;
		numOfBuckets = initPages;
		M = initPages;
		minBuckets = initPages;
		availSpace = numOfBuckets*this.sizeOfBucket;
		this.maxThreshold = maxThreshhold;
		this.minThreshold = minThreshold;

		if ((sizeOfBucket == 0) || (numOfBuckets == 0)) {
		  System.out.println("Space for the table cannot be 0. Terminating Program...");
		  System.exit(0);
		}
		this.hBuckets = new Bucket[numOfBuckets];
		for (int i = 0; i < numOfBuckets; i++) {
		   hBuckets[i] = new Bucket(sizeOfBucket);
		}
	}
	
	//Getters and Setters Methods for member variables
	public int getSizeOfBucket() {return sizeOfBucket;}
	public int getNumOfKeys() {return numOfKeys;}
	public int getAvailSpace() {return availSpace;}
	public int getNumOfBuckets() {return numOfBuckets;}
	public int getCompares() {return compares;}
	public void setSizeOfBucket(int size) {sizeOfBucket = size;}
	public void setNumOfKeys(int num) {numOfKeys = num;}
	public void setAvailSpace(int space) {availSpace = space;}
	public void setCompares(int compares) {this.compares=compares;}
	
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
	//Method that returns the current load factor 
	//of the hash table
	private float loadFactor() {		
		return ((float)this.numOfKeys)/((float)this.availSpace);
	}
	//Method that returns a hash based on the key
	private int hashFunction(int key) {
		//Use first hash function
		int returnVal = key%this.M;
		if (returnVal < 0) {
			returnVal *= -1;
		}
		//Current Bucket is already split 
		if (increaseCompares()&&(returnVal < nextBucketSplit)){
			//Use second hash function
			returnVal = key%(2*this.M);
			 if (returnVal < 0) {
				 returnVal *= -1;
			 }
		}	 
		return returnVal;
	}
	//Method that splits the bucket that pointer nextBucketSplit points.
	private void bucketSplit() {		
		//Create an array with one more Bucket than before and copy keys
		Bucket [] newHBuckets= new Bucket[numOfBuckets+1];
		for (int i = 0; i < this.numOfBuckets; i++){
		   newHBuckets[i] = this.hBuckets[i];
		   increaseComparesBy(1);
		}
		//Create a new Bucket in Linear Hashing and update Class
		hBuckets = newHBuckets;
		hBuckets[this.numOfBuckets] = new Bucket(this.sizeOfBucket);
		this.availSpace += this.sizeOfBucket;
		//Call split method of Bucket Class
		this.hBuckets[this.nextBucketSplit].split(this, 2*this.M, this.nextBucketSplit, hBuckets[this.numOfBuckets]);
		this.numOfBuckets++;
		increaseComparesBy(4);
		//Change of Phase
		if (increaseCompares()&&(this.numOfBuckets == 2*this.M)) {
			this.M = 2*this.M;
			this.nextBucketSplit = 0;
			increaseComparesBy(2);
		}
		else {
		    this.nextBucketSplit++;
		    increaseComparesBy(1);
		}
	}
	//Method that merges the last bucket that was split
	private void bucketMerge() { 		
		Bucket[] newHashBuckets= new Bucket[numOfBuckets-1];
		for (int i = 0; i < this.numOfBuckets-1; i++) {
		   newHashBuckets[i] = this.hBuckets[i];
		   increaseComparesBy(1);
		}
		if (increaseCompares()&&(this.nextBucketSplit == 0)) {
			this.M = (numOfBuckets)/2;
			this.nextBucketSplit = this.M-1;
			increaseComparesBy(2);
		}
		else {
			this.nextBucketSplit-=1;
			increaseComparesBy(1);
		}
		//Update Linear Hashing Class 
		this.numOfBuckets-=1;
		this.availSpace -= this.sizeOfBucket;
		//Call merge method of Bucket Class
		this.hBuckets[this.nextBucketSplit].merge(this, hBuckets[this.numOfBuckets]);
		hBuckets = newHashBuckets;
		increaseComparesBy(3);
	}
	//Method for inserting a new key
	public void insertKey(int key) {	
		//Call insert method of Bucket Class
		this.hBuckets[this.hashFunction(key)].insert(key, this);
		//Check if split is needed after insertion is done
		if (increaseCompares()&&(this.loadFactor() > maxThreshold)){
			this.bucketSplit();
		}
	}
	//Method for deleting a key
	public void deleteKey(int key) {
		//Call delete method of Bucket Class
		this.hBuckets[this.hashFunction(key)].delete(this, key);
		//Check if split is needed after deletion is done
		if (increaseCompares()&&(this.loadFactor() > maxThreshold)){
			this.bucketSplit();
		}
		//Check if merge is needed
		else if (increaseCompares()&&((this.loadFactor() < minThreshold) && (this.M > this.minBuckets))){
			this.bucketMerge();
		}
	}
	//Method for searching a key
	public boolean searchKey(int key) {		
		return this.hBuckets[this.hashFunction(key)].search(key, this);
	}
	
}
