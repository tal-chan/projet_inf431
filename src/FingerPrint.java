import java.io.IOException;


public class FingerPrint {
	int b;
	byte[] M;
	double alpha;
	int m;
	
	/*
	 * fingerPrint - Computes the statistical fingerprint of a big data chunk.
	 * 
	 * b - fingerprint precision (4<=b<=16).
	 */
	public FingerPrint (Data d) throws IOException {
		this (d, 11);
	}
	public FingerPrint (Data d, int b) throws IOException {
		if ((b<4) || (b>16)) {
			throw new IllegalArgumentException("In function hyperLogLog : Parameter b out of range.");
		}
		
		this.b = b;
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
		
		d.Init();
		int mask=m-1;
		try {
			for(;;) {
				Element el = d.nextElement();
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
		if (fp1.b != fp2.b) {
			throw new IllegalArgumentException("In function FingerPrint(FingerPrint, FingerPrint) : Different b attributes.");		
		}
		
		this.b = fp1.b;
		
		int m = 1<<b;
		this.M = new byte[m];
		
		for (int i=0; i<m; i++) {
			if (fp1.M[i] >= fp2.M[i]) {
				this.M[i] = fp1.M[i];				
			} else {
				this.M[i] = fp2.M[i];
			}			
		}
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
		
		return ((this.hyperLogLog()+fp.hyperLogLog())/fp_union.hyperLogLog()) - 1;
	}

}
