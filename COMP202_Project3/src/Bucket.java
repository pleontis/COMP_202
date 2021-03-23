
public class Bucket {
	private int keysNum;
	private int[] keys;
	private Bucket overflBucket;

	//Constructor method for creating Hash Buckets
	//Initializing variables
	public Bucket(int size) {
		keysNum=0;
		keys= new int[size];
		overflBucket=null;
	}
	//Getter Method for keysNum variable
	public int getkeysNum() {return this.keysNum;}
	
	//Method for inserting a new key to the node
	public void insert(int key, LinearHashing lineH) {
		int i;
		int sizeOfBucket=lineH.getSizeOfBucket();
		int numOfKeys=lineH.getNumOfKeys();
		int availSpace=lineH.getAvailSpace();
		lineH.increaseComparesBy(4);
		//Search if key already exists in Hash Table 
		for(i=0;lineH.increaseCompares()&&((i<this.keysNum)&&(i<sizeOfBucket)) ;i++) {
			//Do not insert the new one
			if(lineH.increaseCompares()&&this.keys[i]==key) {
				return;
			}
		}
		//Bucket is not full. Insert the new key in current Bucket
		if(lineH.increaseCompares()&&(i<sizeOfBucket)) {
			keys[i]=key;
			this.keysNum+=1;
			//Update Linear Hashing Class
			lineH.setNumOfKeys(numOfKeys+1);
			lineH.increaseComparesBy(3);
		}
		//Overflow case
		else {
			//Create a new overflow Bucket and insert the new key in it
			if(lineH.increaseCompares()&&(this.overflBucket==null)) {
				this.overflBucket=new Bucket(sizeOfBucket);
				//Update Linear Hashing Class
				lineH.setAvailSpace((availSpace+sizeOfBucket));
				lineH.increaseComparesBy(2);
				this.overflBucket.insert(key, lineH);
			}
			//Insert key to Overflow Bucket
			else {
				this.overflBucket.insert(key, lineH);
			}
		}
		
	}
	//Method that splits the current Bucket
	public void split(LinearHashing lineH,int M, int position,Bucket nBucket) {
		int sizeOfBucket=lineH.getSizeOfBucket();
		int availSpace=lineH.getAvailSpace();
		
		for (int i=0;lineH.increaseCompares()&&((i<this.keysNum)&&(i<sizeOfBucket));i++){
			//Key must be moved to a new Bucket
			if(lineH.increaseCompares()&&((this.keys[i]%M)!=position)) {
				nBucket.insert(this.keys[i], lineH);
				this.keysNum+=-1;
				//Update Linear Hashing Class
				int numOfKeys=lineH.getNumOfKeys();
				numOfKeys--;
				lineH.setNumOfKeys((numOfKeys));
				this.keys[i]=this.keys[this.keysNum];
				lineH.increaseComparesBy(5);
			}
		}
		//Split also overflow Bucket if exists
		if(lineH.increaseCompares()&&(this.overflBucket!=null)) {
			this.overflBucket.split(lineH,M,position,nBucket);
		}
		while(lineH.increaseCompares()&&(this.keysNum<sizeOfBucket)) {
			if(lineH.increaseCompares()&&(this.overflBucket==null)) {
				return;
			}
			//Overflow Bucket is not empty
			if(lineH.increaseCompares()&&(this.overflBucket.keysNum!=0)) {
				this.keys[this.keysNum]=this.overflBucket.removeLastKey(lineH);
				lineH.increaseComparesBy(1);
				//Overflow Bucket is Empty
				if(lineH.increaseCompares()&&this.overflBucket.keysNum==0) {
					//Free overflow Bucket
					this.overflBucket=null;
					//Update Linear Hashing Class
					availSpace-=sizeOfBucket;
					lineH.setAvailSpace((availSpace));
					lineH.increaseComparesBy(3);
				}
				this.keysNum++;
				lineH.increaseComparesBy(1);
			}
			//Overflow Bucket is empty
			else {
				//Free overflow Bucket and update Linear Hashing Class
				this.overflBucket=null;
				availSpace-=sizeOfBucket;
				lineH.setAvailSpace((availSpace));
				lineH.increaseComparesBy(3);
			}
		}
	}
	//Method for searching a key in Hash Table
	//Method returns true if key was found or false if not
	public boolean search(int key, LinearHashing lineH) {
		int sizeOfBucket=lineH.getSizeOfBucket();
		lineH.increaseComparesBy(1);
		for(int i=0;lineH.increaseCompares()&&((i<this.keysNum) && (i<sizeOfBucket));i++) {
			//Key was found
			if(lineH.increaseCompares()&&(this.keys[i]==key)) {
				return true;
			}
		}
		//Search also in overflow for key
		if(lineH.increaseCompares()&&(this.overflBucket!=null)) {
			return this.overflBucket.search(key, lineH);
		}
		else {
			return false;
		}
	}
	//Method for merging the current Bucket
	public void merge(LinearHashing lineH,Bucket mergedBucket) {
		while(lineH.increaseCompares()&&(mergedBucket.getkeysNum()!=0)) {
			this.insert(mergedBucket.removeLastKey(lineH),lineH);
		}
	}
	//Method for removing last key of the Bucket
	//Returns the value of the key
	public int removeLastKey(LinearHashing lineH) {
		int sizeOfBucket=lineH.getSizeOfBucket();
		int availSpace=lineH.getAvailSpace();
		lineH.increaseComparesBy(2);
		//Check if there is not an overflow Bucket first
		if(lineH.increaseCompares()&&(this.overflBucket==null)) {
			//Bucket is not empty
			if(lineH.increaseCompares()&&(this.keysNum!=0)) {
				this.keysNum-=1;
				lineH.increaseComparesBy(1);
				return this.keys[this.keysNum];
			}
			else {
				return 0;
			}
		}
		else {
			int value=this.overflBucket.removeLastKey(lineH);
			lineH.increaseComparesBy(1);
			//Overflow Bucket is empty
			if(lineH.increaseCompares()&&(this.overflBucket.keysNum==0)) {
				//Free overflow Bucket
				this.overflBucket=null;
				//Update Linear Hashing Class
				lineH.setAvailSpace((availSpace-sizeOfBucket));
				lineH.increaseComparesBy(2);
			}
			return value;
		}
	}
	//Method for deleting a key
	public void delete(LinearHashing lineH, int key) {
		int sizeOfBucket=lineH.getSizeOfBucket();
		int numOfKeys=lineH.getNumOfKeys();
		int availSpace=lineH.getAvailSpace();
		//Search Bucket for key to deletion first
		for(int i=0;lineH.increaseCompares()&&((i<this.keysNum)&&(i<sizeOfBucket));i++) {
			//Key was found
			if(lineH.increaseCompares()&&(this.keys[i]==key)) {
				//Check if there is no an overflow Bucket first
				if(lineH.increaseCompares()&&(this.overflBucket==null)) {
					this.keys[i]=this.keys[this.keysNum-1];
					this.keysNum--;
					numOfKeys--;
					//Update Linear Hashing Class
					lineH.setNumOfKeys(numOfKeys);
					lineH.increaseComparesBy(4);
				}
				//There is an overflow Bucket
				//Remove a key from there and bring it here
				else {
					this.keys[i]=this.overflBucket.removeLastKey(lineH);
					numOfKeys--;
					//Update Linear Hashing Class
					lineH.setNumOfKeys(numOfKeys);
					lineH.increaseComparesBy(3);
					//Overflow Bucket is empty
					if(lineH.increaseCompares()&&this.overflBucket.keysNum==0) {
						//Free overflow Bucket
						this.overflBucket=null;
						//Update Linear Hashing Class
						availSpace-=sizeOfBucket;
						lineH.setAvailSpace(availSpace);
						lineH.increaseComparesBy(3);
					}
				}
				return;
			}
		}
		//Search at the overflow Bucket for the key to be deleted 
		//if one exists
		if(lineH.increaseCompares()&&this.overflBucket!=null) {
			this.overflBucket.delete(lineH, key);
			//Overflow Bucket is empty
			if(lineH.increaseCompares()&&this.overflBucket.keysNum==0) {
				//Free overflow Bucket
				this.overflBucket=null;
				availSpace-=sizeOfBucket;
				//Update Linear Hashing Class
				lineH.setAvailSpace(availSpace);
				lineH.increaseComparesBy(3);
			}
		}
	}
 }
