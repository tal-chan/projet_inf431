import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;



public class HashTest {

	/*
	 *  In construction...
	 */

	/*
	 * locate - Locates a String in a sorted ArrayList with a predictive dichotomous algorithm.
	 */
	static int locate (List<String> list, String s) {
		if (list.size() == 0) {
			return 0;
		}

		int index_a = -1;
		int index_b = list.size();
		String value_a = list.get(index_a+1);
		String value_b = list.get(index_b-1);
		if (s.compareTo(value_a)<=0) {
			return 0;
		}
		if (s.compareTo(value_b)>0) {
			return list.size();
		}

		while (index_b - index_a > 1) {
			int index_mid = index_a+(index_b-index_a)/2;
			if (list.get(index_mid).compareTo(s)<=0) {
				index_a = index_mid;
				value_a = list.get(index_a);
			} else {
				index_b = index_mid;
				value_b = list.get(index_b);
			}
		}
		if (s.equals(list.get(index_a))) {
			return index_a;
		}
		return index_b;
	}

	/*
	 * extractWords - extracting distinct words from a text file.
	 * This is to create relevant data to test the hash function
	 */
	
	public static void extractWords(String inFile, String outFile) throws IOException{
		Data data = new Data(inFile,Data.FILE);
		List<String> list = new ArrayList<String>();
		data.init();
		try {
			for(;;){
				Element el = data.nextElement();
				String word = el.GetContent();
				int pos = locate(list,word);
				if ((pos == list.size()) || (!list.get(pos).equals(word))) {
					list.add(pos, word);
				}
			}

		} catch (Data.NoMoreElement e) {}
		Writer writer =  new OutputStreamWriter(new FileOutputStream(outFile));
		try{
			for(String s:list){
				writer.write(s+"\n");
			}

		}finally{ try {writer.close();} catch (Exception ex) {}
		}

	}
	
	public static void histogram (String name, int lowBound, int range,String outFile)throws IOException{
		Data data = new Data(name, Data.FILE);
		int[] count = new int[range];
		data.init();
		try {
			for(;;){
				int hash = data.nextElement().GetHash();
				if (hash>= lowBound && hash < lowBound+range){
					count[hash-lowBound]++;
				}
			}

		} catch (Data.NoMoreElement e) {}
		Writer writer =  new OutputStreamWriter(new FileOutputStream(outFile));
		try{
			for(int i=lowBound;i<range+lowBound;i++){
				int c = count[i-lowBound];
				writer.write(i+"\t"+c+"\n");
			}

		}finally{ try {writer.close();} catch (Exception ex) {}
		}
		
	}

	/*
	 * Testing the hash function with Shakespeare's vocabulary
	 */
	
	public static void main(String[] args) throws IOException {
		String name = "vocab.txt";
		String outFile = "histogram.txt";
		/*Data data = new Data(name, Data.FILE);
		data.init();
		try {
			for(;;){
				int hash = data.nextElement().GetHash();
				System.out.println(hash);
				}
		} catch (Data.NoMoreElement e) {}
		*/
		histogram(name,-223722832,20000000,outFile);
	}
}
