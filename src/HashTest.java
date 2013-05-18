import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class HashTest {

	/*
	 * N - number of words in Shakespeare's vocabulary (Complete works). For testing purposes.
	 */	
	static int N = 22929;

	/*
	 * locateString - Locates a String in a sorted ArrayList with a dichotomous algorithm.
	 */
	static int locateString (List<String> list, String s) {
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
				int pos = locateString(list,word);
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

	/*
	 * hashCount - counting the hash values hit discriminating by the 12 least significant bits
	 */	
	public static int[] hashCount(String name) throws IOException{
		Data data = new Data(name, Data.FILE);
		int mask=0xfff;
		int[]count = new int[mask+1];
		data.init();
		try {
			for(;;){
				int hash = data.nextElement().GetHash();
				count[hash&mask]++;
			}

		} catch (Data.NoMoreElement e) {}
		return count;
	}

	/*
	 * randomCount - simulating a uniform repartition of hash values.
	 */	
	public static int[] randomCount(){
		Random gen = new Random();
		int[] count = new int[0x1000];
		for (int i=0;i<N;i++){
			int random = gen.nextInt(0x1000);
			count[random]++;
		}
		return count;
	}

	/*
	 * chi2 - calculating the chi2 test of a set of data 
	 */	
	public static double chi2(int[] sample){
		int n=sample.length;
		double theory = N/(double)n;
		double res = 0;
		for (int i=0;i<n;i++){
			res+=((double)sample[i]-theory)*((double)sample[i]-theory);
		}
		return res/theory;
	}

	/*
	 * averageRandomChi2 - calculating the average chi2 for a pseudo-randomly
	 * generated set of data over n repeated experiences. 
	 */
	public static double averageRandomChi2 (int n){
		double res=0;
		for (int i=0;i<n;i++){
			res+=chi2(randomCount());
		}
		return res/n;
	}

	/*
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

	}*/

	/*
	 * Testing the hash function with Shakespeare's vocabulary
	 *	
	public static void main(String[] args) throws IOException {
		String name = "vocab.txt";
		//String outFile = "histogram.txt";
		//Data data = new Data(name, Data.FILE);
		/*Data data = new Data(name, Data.FILE);
		data.init();
		try {
			for(;;){
				int hash = data.nextElement().GetHash();
				System.out.println(hash);
				}
		} catch (Data.NoMoreElement e) {}
	 *
		//histogram(name,-223722832,20000000,outFile);
		int[] count = hashCount(name);
		System.out.println(chi2(count));
		System.out.println(averageRandomChi2(1000));
		/*Writer writer =  new OutputStreamWriter(new FileOutputStream(outFile));
		try{
			for(int i=0;i<count.length;i++){
				int c = count[i];
				for(int j=0;j<c;j++) writer.write("*");
				writer.write("\r\n");
			}

		}finally{ try {writer.close();} catch (Exception ex) {}
		}*
	}*/
	
	/*
	 * time - Evaluates the time took by the hash calculation.
	 */
	static double time () throws IOException {
		int nbr = 10000;
		
		long t0 = System.currentTimeMillis();
		Data data1 = new Data("vocab.txt", Data.FILE);
		data1.init();
		try {
			for(;;){
				String str;
				Element el = data1.nextElement();
				str = el.GetContent();
				for (int i=0;i<nbr; i++) {}
			}

		} catch (Data.NoMoreElement e) {}
		long referenceDelay = System.currentTimeMillis() - t0;

		t0 = System.currentTimeMillis();
		Data data2 = new Data("vocab.txt", Data.FILE);
		data2.init();
		try {
			for(;;){
				String str;
				Element el = data2.nextElement();
				str = el.GetContent();
				for (int i=0;i<nbr; i++) { Element.Hash(str); }
			}

		} catch (Data.NoMoreElement e) {}
		long realDelay = System.currentTimeMillis() - t0;
		
		double mean_delay = (double)(realDelay-referenceDelay) / ((double)N*nbr) *1000000;
		
		System.out.println ("Mean time of hash function's execution : " + mean_delay + "ns.");
		// 492ns with nbr = 10000
		
		return mean_delay;
	}
	

	public static void main(String[] args) throws IOException {
		time();
	}
}
