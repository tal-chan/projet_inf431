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
	/*
	 * getMiceProportion - extracts a sample from input and returns the proportion of mice
	 * occurrences - number of occurrences of the mice
	 */
	public double getMiceProportion(int occurrences) throws IOException{
		double res=0;
		input.init();
		bag.init();
		while(input.hasNext()) bag.tryInsert(input.nextElement());
		Element[] content = bag.getContent();
		for (int i=0;i<size;i++){
			if (content[i].getCount()+1==occurrences) res+=1;
		}
		return res /size;
	}
	public static void main(String[] args) throws IOException{
		String name = "Texts/Shakespeare/other_pieces/";
		String txt = "hamlet.txt";
		Data input = new Data(name+txt,Data.FILE);
		Mice mice = new Mice(input,1000);
		for (int i = 1;i<10;i++){
			System.out.println(mice.getMiceProportion(i));
		}
	}
}
