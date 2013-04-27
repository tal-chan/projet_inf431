import java.io.File;
import java.io.IOException;


public class FingerPrint {
	int b;
	byte[] M;
	double alpha;
	int m;
	int k;
	
	/*
	 * fingerPrint - Computes the statistical fingerprint of a big data chunk.
	 * 
	 * b - fingerprint precision (4<=b<=16).
	 * 
	 * k - number of words in shingles
	 */
	public FingerPrint (Data d, int k) throws IOException {
		this (d, 11,k);
	}
	public FingerPrint (Data d, int b, int k) throws IOException {
		if ((b<4) || (b>16)) {
			throw new IllegalArgumentException("In function hyperLogLog : Parameter b out of range.");
		}
		
		this.b = b;
		this.k = k;
		m = 1<<b;
		M = new byte[m];
		for (int i=0; i<m; i++) {
			M[i] = -1;
		}
		
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

		int mask=m-1;
		try {
			for(Element el = d.FirstElement(k);;el=d.nextElement()) {
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
		} catch (Data.NoMoreElement e) {}
	}
	
	/*
	 * FingerPrint - Computes a fingerprint of the union of two data chunk, using a fingerprint of
	 * 	the same precision from each chunk.
	 */
	public FingerPrint(FingerPrint fp1, FingerPrint fp2) {
		if (fp1.b != fp2.b && fp1.k != fp1.k) {
			throw new IllegalArgumentException("In function FingerPrint(FingerPrint, FingerPrint) : Different b or k attributes.");		
		}
		
		this.b = fp1.b;
		this.k = fp1.k;
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
		
		for (int i=0; i<m; i++) {
			System.out.printf("(%d,%d,%d)", fp1.M[i], fp2.M[i], this.M[i]);
		}
		System.out.printf ("\n");
	}
	
	/*
	 * hyperLogLog - Quick count of the number of distinct elements of the data chunk from its fingerprint.
	 */
	public double hyperLogLog () {
		float tmp = 0;
		for (int i=0; i<m; i++) {
			if (M[i] >= 0) {
				tmp += Math.pow(2, -M[i]);
			}
		}
		
		return alpha * (double)m * (double)m / tmp;
	}
	
	
	/*
	 * similarity - Computes the similarity between the data chunk and another data chunk
	 * 	using a fingerprint of each data chunk
	 */
	public double similarity(FingerPrint fp) {
		FingerPrint fp_union = new FingerPrint (this, fp);
		double thishll = this.hyperLogLog();
		double fphll = fp.hyperLogLog();
		double union = fp_union.hyperLogLog();
		if ((((thishll+fphll)/union) - 1)<0)  {
			System.out.println ("***************************************************************");
			System.out.printf ("this : %f, fp : %f, union : %f\n", thishll, fphll, union);
		}
		return (((thishll+fphll)/union) - 1);
	}

		
	public static FingerPrint[] fingerprints(Data[] data, int k, int b) throws IOException{
		int l = data.length;
		FingerPrint[] fgprints = new FingerPrint[l];
		for (int i=0;i<l;i++){
			fgprints[i] = new FingerPrint (data[i],b,k);
		}
		return fgprints;
	}
	
	/*
	 * similarities - compares a set of fingerprints two at a time.
	 */
	
	public static double [][] similarities(FingerPrint[] fp){
		int l = fp.length;
		double [][] sim = new double [l][l];
		for (int i=0;i<l;i++){
			sim[i][i]=1;
			for (int j=0;j<i;j++){
				sim[i][j]=fp[i].similarity(fp[j]);
				sim[j][i]=sim[i][j];
			}
		}
				
		return sim;
		
	}
	
	public void similarPairs(String directory,int k, int b, double th) throws IOException{
		Data[] data = Data.extractData(directory);
		double[][] sim = similarities(fingerprints(data,k,b));
		int l = sim.length;
		for (int i=0;i<l;i++){
			for(int j=0;j<i;j++){
				if (sim[i][j]>th){
					System.out.println(data[i].getName()+", "+data[j].getName()+"(similarity : "+sim[i][j]+")");
				}
			}
		}
	}

}
