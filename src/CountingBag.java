/*
 * CountingBag - data structure implementing sampling algorithm,
 * while keeping track of the number of occurrences of each element in the sample,
 * using the count field in Element class. 
 */
public class CountingBag extends SamplingBag {
	public CountingBag(int k){
		super(k);
	}
	
	@Override
	protected boolean insertSorted(Element e){
		int hash = e.GetHash();
		if (ns==0) { //dealing with empty array
			sorted[0]=e;
			return true;
		}
		int i=0;
		while (i<ns&&hash>sorted[i].GetHash()){
			i++;
		}
		if (i==ns){;
			sorted[i]=e;
			return true;
		}
		if (hash==sorted[i].GetHash()) {
			sorted[i].incrCount();
			return false;
		}
		if(i==size) return false;
		shiftSorted(i);
		sorted[i]=e;
		return true;
	}
	
	@Override
	protected void insertUnsorted (Element e){
		for (int i=0;i<nu;i++){
			if (e.GetHash()==unsorted[i].GetHash()){
				unsorted[i].incrCount();
				return;
			}
		}
		unsorted[nu++]=e;
	}
	
}
