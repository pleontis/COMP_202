package FileEditor;

public class Tuple implements Comparable<Tuple>{
	private String word;
	private int line;
	
	public Tuple(String w, int l) {
		this.line=l;
		this.word=w;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}

	public int compareTo(Tuple T) {
		return this.word.compareTo(T.getWord());
	}	
	
}
