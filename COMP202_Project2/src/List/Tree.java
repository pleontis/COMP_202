package List;

public interface Tree<T> {
	public void insert(T node);
	public T search(T start,int key);
	public int[] inOrder(T start);
	public void searchByRange(T start,int firstNum,int lastNum);
}
