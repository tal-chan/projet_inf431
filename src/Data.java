import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class Data {
	public static final int FILE = 0;
	public static final int URL = 1;
	private String name;
	private final int type;
	private BufferedReader stream;

	public Data (String name, int type) {
		this.name = name;
		this.type = type;
	}
	

	public static class NoMoreElement extends Exception {
		private static final long serialVersionUID = -7666683929533309641L;
		NoMoreElement(String message){super(message);}
	}
	
	/*
	 * init - Initializes the cursor position to the beginning of the file.
	 */

	void init() throws IOException {
		if (this.type==FILE){
			this.stream = new BufferedReader(new FileReader(name));
		}
		else{
			URL url = new URL(name);
			this.stream = new BufferedReader(new InputStreamReader(url.openStream()));
		}
	}
	/*
	 * FirstElement - initializes the cursor position to the beginning of the file
	 * and returns the first element of the text.
	 * 
	 * k - number of words consecutive words in each element.
	 */
	
	Element FirstElement(int k) throws IOException, NoMoreElement {
		init();
		String content = new String();
		for (int i=0;i<k;i++){
			content = content+readWord();
		}
		return new Element (content,k);
		
	}
	
	/*
	 * readChar - reads a single character from the file.
	 */
	
	private char readChar() throws EOFException, IOException {
		int c = stream.read();
		if (c==-1) {
			throw new EOFException("reached end of file");
		}
		return (char)c;
	}
	
	/*
	 * readWord - reads a single word from the file.
	 */
	
	private String readWord() throws NoMoreElement, IOException {
		String content = new String();
		try{
			char c = readChar();
			while(!Word.isLetter(c)){
				c=readChar();
			}
			while(Word.isLetter(c)){
				if (c>='A' && c<='Z') {
					c += 32;
				}
				content = content+c;
				c = readChar();
			}

		} catch(EOFException e){
			if (content.length() != 0) {
				return content;
			}
			throw new NoMoreElement("reached end of file");
		}
		return content;
	}
	
	/*
	 * nextElement - reads the nex element in the file.
	 * e - the last element read.
	 * 
	 */
	
	Element nextElement () throws NoMoreElement, IOException{
		return new Element(readWord(),1);
	}

	Element nextElement (Element e) throws NoMoreElement, IOException {
		int k = e.GetNbWords();
		if (k<2){return nextElement();}
		String s = e.removeFirstWord();
		s=s+readWord();
		return new Element(s, k);		
	}
	
	/*
	 * locate - Locates a hash in a sorted ArrayList with a predictive dichotomous algorithm.
	 */
	private int locate (List<Integer> list, int hash) {
		if (list.size() == 0) {
			return 0;
		}
		
		int index_a = -1;
		int index_b = list.size();
		float value_a = (float)list.get(index_a+1);
		float value_b = (float)list.get(index_b-1);
		float hash_float = (float)hash;
		
		if (hash <= value_a) {
			return 0;
		}
		if (hash > value_b) {
			return list.size();
		}
		
		while (index_b - index_a > 1) {
			int index_mid = index_a+(int)((float)(index_b-index_a)*(hash_float-value_a)/(value_b-value_a));
			if (index_mid == index_a) {
				index_mid++;
			} else if (index_mid == index_b) {
				index_mid--;
			}
			if (hash >= list.get(index_mid)) {
				index_a = index_mid;
				value_a = list.get(index_a);
			} else {
				index_b = index_mid;
				value_b = list.get(index_b);
			}
		}
		if (hash == list.get(index_a)) {
			return index_a;
		}
		return index_b;
	}
	
	/*
	 * count - Precise count of the number of distinct elements of the data chunk.
	 */
	public int count () throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		try {
			for(int i=0;;i++) {
				Element el;
				if (i==0) {
					el = FirstElement(1);
				} else  {
					el = nextElement();
				}
				int hash = el.GetHash();
				int pos = locate (list, hash);
				if ((pos == list.size()) || (list.get(pos) != hash)) {
					list.add(pos, hash);
				}
				/*for (i=0; i<list.size() && list.get(i)<hash; i++) {}
				if ((i==list.size()) || (list.get(i) != el.GetHash())) {
					list.add(i, hash);
				}*/
			}
		} catch (NoMoreElement e) {}
		
		return list.size();
	}
}
