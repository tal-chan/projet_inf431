import java.io.IOException;

/*
 * Mice - giving an estimate of the proportion of k-mice, i.e. elements appearing exactly k times.
 */
public class Mice {
	Data input;
	int size;
	CountingBag bag;
	public Mice(Data d,int n){
		input = d;
		size = n;
		bag = new CountingBag (n);
	}
	public double getMiceProportion(int occurrences) throws IOException{
		double res=0;
		input.init();
		while(input.hasNext()) bag.tryInsert(input.nextElement());
		Element[] content = bag.getContent();
		for (int i=0;i<size;i++){
			if (content[i].getCount()+1==occurrences) res+=1;
		}
		return res /size;
	}
}
