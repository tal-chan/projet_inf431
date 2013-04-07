import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * Init - Puts the cursor at the beginning of the data source.
	 */
	private void Init () throws IOException  {
		if (this.type==FILE){
			this.stream = new BufferedReader(new FileReader(name));
		}
		else{
			URL url = new URL(name);
			this.stream = new BufferedReader(new InputStreamReader(url.openStream()));
		}
	}
	
	private char readChar() throws EOFException, IOException {
		int c = stream.read();
		if (c==-1) {
			throw new EOFException("reached end of file");
		}
		return (char)c;
	}

	private Element nextElement () throws NoMoreElement, IOException {
		String content = new String();
		try{
			char c = readChar();
			while(!((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-')){
				c=readChar();
			}
			while((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-'){
				if (c>='A' && c<='Z') {
					c += 32;
				}
				content = content+c;
				c = readChar();
			}

		} catch(EOFException e){
			if (content.length() != 0) {
				return new Element(content);
			}
			throw new NoMoreElement("reached end of file");
		}
		return new Element(content);
	}
	
	/*
	 * locate - Locates a hash in a sorted ArrayList
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
	 * count - Precise count of the number of distinct elements of the data stream.
	 */
	public int count () throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		Init();
		try {
			for(;;) {
				Element el = nextElement();
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

	/*
	 * hyperLogLog - Quick count of the number of distinct elements of the data stream.
	 */
	public double hyperLogLog () throws IOException {
		return hyperLogLog(11);
	}
	public double hyperLogLog (int b) throws IOException {
		if ((b<4) || (b>16)) {
			System.out.printf("Error! In function hyperLogLog : Parameter b out of range.\n");
			return 0;
		}
		
		int m = 1<<b;
		byte[] M = new byte[m];
		for (int i=0; i<m; i++) {
			M[i] = -1;
		}
		
		Init();
		int mask=m-1;
		try {
			for(;;) {
				Element el = nextElement();
				int hash = el.GetHash();
				
				int group = hash & mask;
				hash >>>= b;
			
				int tmp_mask = 1;
				byte num_zeros = 0;
				for (; (b+num_zeros<32) && ((tmp_mask&hash)==0); tmp_mask=(tmp_mask<<1)+1, num_zeros++) {}
				
				if (M[group] < num_zeros + 1) {
					M[group] = (byte) (num_zeros + 1);
				}				
			}
		} catch (NoMoreElement e) {}
		
		double alpha;
		switch (b) {
			case 4:
				alpha = 0.673;
				break;
			case 5:
				alpha = 0.697;
				break;
			case 6:
				alpha = 0.709;
				break;
			default:
				alpha = 0.7213/(1+(1.079/m)); 
		}

		float tmp = 0;
		for (int i=0; i<m; i++) {
			if (M[i] >= 0) {
				tmp += Math.pow(2, -M[i]);
			}
		}
		
		return alpha * (double)m * (double)m / tmp;
	}

}
