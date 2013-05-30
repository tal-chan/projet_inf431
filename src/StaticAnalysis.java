import java.io.IOException;


public class StaticAnalysis extends FingerPrint {
	public StaticAnalysis(Data d) throws IOException {
		super();
		this.compute(d);
	}
	public StaticAnalysis(Data d, int b) throws IOException {
		super(b);
		this.compute(d);
	}
	
	/*
	 * StaticAnalysis - Computes a fingerprint of the union of two data chunk, using a fingerprint of
	 * 	the same precision from each chunk.
	 */
	public StaticAnalysis(StaticAnalysis fp1, StaticAnalysis fp2) {
		if (fp1.b != fp2.b) {
			throw new IllegalArgumentException("In function StaticAnalysis() : Different b attributes.");		
		}

		this.b = fp1.b;
		this.alpha = fp1.alpha;
		m = 1<<b;
		this.M = new byte[m];

		for (int i=0; i<m; i++) {
			if (fp1.M[i] >= fp2.M[i]) {
				this.M[i] = fp1.M[i];				
			} else {
				this.M[i] = fp2.M[i];
			}			
		}
	}

	public void compute (Data d) throws IOException {
			d.init();
			while(d.hasNext()) {
				newElement(d);
			}
	}

	
	/*
	 * similarity - Computes the similarity between the data chunk and another data chunk
	 * 	using a fingerprint of each data chunk
	 */
	public static double similarity(StaticAnalysis fp1, StaticAnalysis fp2) {
		StaticAnalysis fp_union = new StaticAnalysis (fp1, fp2);
		double thishll = fp1.hyperLogLog();
		double fphll = fp2.hyperLogLog();
		double union = fp_union.hyperLogLog();
		return (((thishll+fphll)/union) - 1);
	}


	public static StaticAnalysis[] groupStaticAnalysis(Data[] data, int b) throws IOException{
		int l = data.length;
		StaticAnalysis[] fgprints = new StaticAnalysis[l];
		for (int i=0;i<l;i++){
			fgprints[i] = new StaticAnalysis (data[i],b);
		}
		return fgprints;
	}

	/*
	 * groupSimilarities - compares a set of fingerprints two at a time.
	 */
	public static double [][] groupSimilarities(StaticAnalysis[] fp){
		int l = fp.length;
		double [][] sim = new double [l][l];
		for (int i=0;i<l;i++){
			sim[i][i]=1;
			for (int j=0;j<i;j++){
				sim[i][j]=similarity(fp[i], fp[j]);
				sim[j][i]=sim[i][j];
			}
		}

		return sim;
	}

	public void similarPairs(String directory, int b, double th) throws IOException{
		Data[] data = Data.extractData(directory);
		double[][] sim = groupSimilarities(groupStaticAnalysis(data, b));
		int l = sim.length;
		for (int i=0;i<l;i++){
			for(int j=0;j<i;j++){
				if (sim[i][j]>th){
					System.out.println(data[i].getName()+", "+data[j].getName()+"(similarity : "+sim[i][j]+")");
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		String name = "Texts/fravia/pages.txt";
		Data data = new Data(name, Data.FILE);
		long t0, t1;
		
		t0 = System.currentTimeMillis();
		System.out.printf("Exact number of different words : %d.\n", data.count());
		t1 = System.currentTimeMillis();
		System.out.printf("Execution took %fms\n", (double)(t1-t0));
		t0=t1;
		
		for (int b=13; b<=16; b++) {
			StaticAnalysis test = new StaticAnalysis(data, b);
			System.out.printf("Estimated number of different words for b=%d : %f.\n", b, test.hyperLogLog());		

			t1 = System.currentTimeMillis();
			System.out.printf("Execution took %fms\n", (double)(t1-t0));
			t0=t1;
		}	
	}
}
