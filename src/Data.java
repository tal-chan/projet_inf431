import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class Data {
	public static final int FILE = 0;
	public static final int URL = 1;
	private String name;
	private final int type;
	private MyReader stream;

	public Data (String name, int type, Settings s) {
		this.name = name;
		this.type = type;
		stream = new MyReader(s);
	}
	public Data (String name, int type) {
		this.name = name;
		this.type = type;
		stream = new MyReader();
	}

	public String getName(){
		return name;
	}

	/*
	 * init - Initializes the cursor position to the beginning of the file.
	 */

	void init() throws IOException {
		if (this.type==FILE){
			stream.init(new FileInputStream(name));
		}
		else{
			URL url = new URL(name);
			stream.init((url.openStream()));
		}
	}


	/*
	 * nextElement - reads the next element in the file.
	 * e - the last element read.
	 * 
	 */

	Element nextElement () {
		return stream.next();
	}

	boolean hasNext(){
		return stream.hasNext();
	}

	/*
	 * extractData - reading files from a directory or URL lists and storing in a Data[]
	 */

	public static Data[] extractData(String name){
		File directory = new File(name);
		File[] files = directory.listFiles();
		int l = files.length;
		Data[] data = new Data[l];
		for (int i=0;i<l;i++){
			data[i] = new Data(files[i].getAbsolutePath(),Data.FILE);
		}
		return data;
	}

	public static Data[] extractData(String[] url){
		int l = url.length;
		Data[] data = new Data[l];
		for (int i=0;i<l;i++){
			data[i] = new Data(url[i],Data.URL);
		}
		return data;
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
		init();
		while(hasNext()) {
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
	return list.size();
}
}
