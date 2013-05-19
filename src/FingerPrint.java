import java.io.IOException;


public class FingerPrint {
	int b;
	byte[] M;
	double alpha;
	int m;
	int mask;

	/*
	 * FingerPrint - New fingerprint. No computation yet.
	 * 
	 * b - fingerprint precision (4<=b<=16).
	 */
	public FingerPrint () {
		this (11);
	}
	public FingerPrint (int b) {
		if ((b<4) || (b>16)) {
			throw new IllegalArgumentException("In function FingerPrint : Parameter b out of range.");
		}

		this.b = b;
		m = 1<<b;
		M = new byte[m];
		mask=m-1;
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
	}

	/*
	 * newElement - Function to call to inspect a new element. The fingerprint is
	 * 		updated.
	 */
	public void newElement (Data d) throws Data.NoMoreElement, IOException {
		Element el = d.nextElement();
		int hash = el.GetHash();

		int group = hash & mask;
		hash >>>= b;

		int tmp_mask = 1;
		byte num_zeros = 0;
		for (; (b+num_zeros<32) && ((tmp_mask&hash)==0); tmp_mask=(tmp_mask<<1)+1, num_zeros++) {}

		if ((M[group] < num_zeros + 1)) {
			M[group] = (byte) (num_zeros + 1);
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
}
