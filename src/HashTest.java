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
	public static int extractWords(String inFile, String outFile) throws IOException{
		Data data = new Data(inFile,Data.FILE);
		List<String> list = new ArrayList<String>();
		data.init();
		while(data.hasNext()){
			Element el = data.nextElement();
			String word = el.GetContent();
			int pos = locateString(list,word);
			if ((pos == list.size()) || (!list.get(pos).equals(word))) {
				list.add(pos, word);
			}
		}


		Writer writer =  new OutputStreamWriter(new FileOutputStream(outFile));
		try{
			for(String s:list){
				writer.write(s+"\n");
			}

		}finally{ try {writer.close();} catch (Exception ex) {}
		}
		return list.size();

	}

	/*
	 * hashCount - counting the hash values hit discriminating with the remainder modulo n.
	 */	
	public static int[] hashCount(String name, int n) throws IOException{
		Data data = new Data(name, Data.FILE);
		int[]count = new int[n];
		data.init();

		while(data.hasNext()){
			int hash = data.nextElement().GetHash();
			count[((hash%n)+n)%n]++;
		}
		return count;
	}

	/*
	 * randomCount - simulating a uniform repartition of hash values.
	 */	
	public static int[] randomCount(int n){
		Random gen = new Random();
		int[] count = new int[n];
		for (int i=0;i<N;i++){
			int random = gen.nextInt(n);
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
	 * generated set of data of length n over k repeated experiences. 
	 */
	public static double averageRandomChi2 (int k, int n){
		double res=0;
		for (int i=0;i<k;i++){
			res+=chi2(randomCount(n));
		}
		return res/k;
	}


	/*
	 * time - Evaluates the time took by the hash calculation.
	 */
	static double time () throws IOException {
		int nbr = 1000;

		long t0 = System.currentTimeMillis();
		Data data1 = new Data("vocab.txt", Data.FILE);
		data1.init();
		while(data1.hasNext()){
			String str;
			Element el = data1.nextElement();
			str = el.GetContent();
			for (int i=0;i<nbr; i++) {}
		}
		long referenceDelay = System.currentTimeMillis() - t0;

		t0 = System.currentTimeMillis();
		Data data2 = new Data("vocab.txt", Data.FILE);
		data2.init();
		while(data2.hasNext()){
			String str;
			Element el = data2.nextElement();
			str = el.GetContent();
			for (int i=0;i<nbr; i++) { Element.Hash(str); }
		}
		long realDelay = System.currentTimeMillis() - t0;

		double mean_delay = (double)(realDelay-referenceDelay) / ((double)N*nbr) *1000000;

		System.out.println ("Mean time of hash function's execution : " + mean_delay + "ns.");
		// 492ns with nbr = 10000

		return mean_delay;
	}
	/*
	 * createVocab - for testing purposes : creating a list of shakespeare's vocabulary.
	 */
	public static void createVocab() throws IOException{
		String name = "Texts/Shakespeare/";
		String file = "Complete.txt";
		String output = "vocab.txt";
		System.out.println(HashTest.extractWords(name+file, output));
	}
	public static void main(String[] args) throws IOException {
		String name = "vocab.txt";
		Data data = new Data(name, Data.FILE);
		int nbr = 4000;
		
		data.init();
		while(data.hasNext()){
			int hash = data.nextElement().GetHash();
		}
		int[] count = hashCount(name, nbr);
		System.out.println("Chi2 using hash function : " + chi2(count));
		System.out.println("Average Chi2 on 1000 tests : " + averageRandomChi2(1000, nbr));
		
		time();
	}
}
