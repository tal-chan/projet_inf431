import java.io.BufferedReader;
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

	public Element nextElement () throws NoMoreElement {
		String content = new String();
		try{
			char c = (char)stream.read();
			while(!((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-')){
				c=(char)stream.read();
			}
			while((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-'){
				if (c>='A' && c<='Z') {
					c += 32;
				}
				content = content+c;
				c = (char)stream.read();
			}

		} catch(IOException e){
			if (content.length() != 0) {
				return new Element(content);
			}
			throw new NoMoreElement("reached end of file");
		}
		return new Element(content);
	}
	
	/*
	 * Count - Precise count of the number of distinct elements of the data stream.
	 */
	public int Count () throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		
		Init();
		try {
			for(;;) {
				Element el = nextElement();
				int i;
				int hash = el.GetHash();
				for (i=0; i<list.size() && list.get(i)<hash; i++) {}
				if ((i==list.size()) || (list.get(i) != el.GetHash())) {
					list.add(i, hash);
				}				
			}
		} catch (NoMoreElement e) {}
		
		return list.size();
	}

	/*
	 * HyperLogLog - Quick count of the number of distinct elements of the data stream.
	 * 
	 * Returns an approximation of log(n)
	 */
	public double HyperLogLog (int b) throws IOException {
		if ((b<4) || (b>16)) {
			System.out.printf("Error! In function LogLogCount : Parameter b out of range.\n");
			return 0;
		}
		
		int m = 1<<b;
		byte[] M = new byte[m];
		
		Init();
		int mask=0;
		for (int i=0; i<b; i++) {
			mask = (mask << 1) + 1;
		}
		try {
			for(;;) {
				Element el = nextElement();
				int hash = el.GetHash();
				
				int group = hash & mask;
				hash >>>= b;
			
				int tmp_mask = 1;
				byte num_zeros = 0;
				for (; (tmp_mask&hash)==0; tmp_mask=(tmp_mask<<1)+1, num_zeros++) {}
				
				if (M[group] < num_zeros) {
					M[group] = num_zeros;
				}				
			}
		} catch (NoMoreElement e) {}
		
		double alpha = 0;
		switch (b) {
			case 2:
				alpha = 0.673;
				break;
			case 3:
				alpha = 0.697;
				break;
			case 4:
				alpha = 0.709;
				break;
			default:
				alpha = 0.7213/(1+(1.079/m)); 
		}

		double tmp = 0;
		for (int i=0; i<m; i++) {
			tmp += Math.pow(2, -M[i]); 
		}
		
		return alpha * m * m / tmp;
	}

}
