import java.io.IOException;



public class DynamicAnalysis extends FingerPrint {
	int W;
	int nbr;
	int mask;
	int currentDate;
	
	int[] date;	
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
	public DynamicAnalysis (int b, int W, int nbr) throws IOException {
		super(b);

		this.W = W;
		this.nbr = nbr;
		currentDate = 0;
		date = new int[m];
		memory = new double[nbr];
	}
	
	/*
	 * newElement - Function to call to inspect a new element. The fingerprint is
	 * 		updated.
	 */
	public void newElement (Data d) throws IOException {
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
}
