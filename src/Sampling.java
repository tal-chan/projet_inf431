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
		try {
			for(Element el = input.FirstElement(1);;el=input.nextElement()) bag.tryInsert(el);
		} catch (Data.NoMoreElement e) {}
		Element[] sample = bag.getContent();
		for (int i=0;i<size;i++){
			System.out.println(sample[i].GetContent());
		}
	}
}
