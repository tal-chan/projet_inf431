import java.io.IOException;



public class DynamicAnalysis {
	Data d;
	int b;
	int W;
	int nbr;
	int m;
	int mask;
	int currentDate;
	
	int[] date;
	byte[] M;
	
	double[] memory;
	
	/*
	 * DynamicAnalysis - Initializes a dynamic statistical fingerprint of a big data stream. A sudden
	 * 		change in that finderprint is an indicator of an attack. The object stores a history of
	 * 		a measure on that dynamic fingerprint (the estimated cardinal of the different elements
	 * 		encountered within the window) in order to detect such changes.
	 * 
	 * b - fingerprint precision (4<=b<=16).
	 * W - size of the window
	 * nbr - number of points in memory. Must be a divisor of window
	 * 
	 * Note : with this implementation, the stream must not exceed 2^31-1 elements.
	 */
	public DynamicAnalysis (Data d, int b, int W, int nbr) throws IOException {
		if ((b<4) || (b>16)) {
			throw new IllegalArgumentException("In function DynamicAnalysis : Parameter b out of range.");
		}
		
		this.b = b;
		this.d = d;
		this.W = W;
		this.nbr = nbr;
		currentDate = 0;
		m = 1<<b;
		mask=m-1;
		date = new int[m];
		M = new byte[m];
		for (int i=0; i<m; i++) {
			M[i] = -1;
		}
		memory = new double[nbr];
		
		d.init();
	}
	
	/*
	 * newElement - Function to be called when a new element is available. The dynamic fingerprint is
	 * 		updated.
	 */
	public void newElement () throws Data.NoMoreElement, IOException {
		Element el = d.nextElement();
		int hash = el.GetHash();

		int group = hash & mask;
		hash >>>= b;

		int tmp_mask = 1;
		byte num_zeros = 0;
		for (; (b+num_zeros<32) && ((tmp_mask&hash)==0); tmp_mask=(tmp_mask<<1)+1, num_zeros++) {}

		if ((M[group] < num_zeros + 1)||(currentDate-date[group]>W)) {
			M[group] = (byte) (num_zeros + 1);
			date[group] = currentDate;
		}
		
		currentDate++;
		
		if (currentDate%(W/nbr) == 0) {
			memory[(currentDate/(W/nbr))%nbr] = hyperLogLog();
		}
	}
	
	
	/*
	 * hyperLogLog - Quick count of the number of distinct elements of the data chunk from its fingerprint.
	 */
	public double hyperLogLog () {
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
