import java.io.IOException;

/*
 * Sampling - taking a sample of representative elements.
 * size - number of elements in the sample
 */

public class Sampling {
	Data input;
	int size;
	SamplingBag bag;
	public Sampling(Data d,int k){
		input = d;
		size = k;
		bag = new SamplingBag (k);
	}
	/*
	 * printSample - prints a sample on standard output.
	 */
	public void printSample()throws IOException{
		input.init();
		bag.init();
		while(input.hasNext()) bag.tryInsert(input.nextElement());
		Element[] sample = bag.getContent();
		for (int i=0;i<size;i++){
			System.out.println(sample[i].GetContent());
		}
	}
	public static void main(String [] args) throws IOException{
		String name = "Texts/fravia/";
		String txt = "protect.txt";
		Data input = new Data(name+txt,Data.FILE);
		Sampling sample = new Sampling (input,10);
		sample.printSample();
	}
}
