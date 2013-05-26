import java.io.IOException;

/*
 * Sampling - taking a sample of representative elements.
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
	public void printSample()throws IOException{
		input.init();
		while(input.hasNext()) bag.tryInsert(input.nextElement());
		Element[] sample = bag.getContent();
		for (int i=0;i<size;i++){
			System.out.println(sample[i].GetContent());
		}
	}
}
