/*
 * SamplingBag - auxiliary data structure implementing
 */
public class SamplingBag {
	int size;
	int zeroes;
	int mask;
	Element[] sorted;
	Element[] unsorted;
	int ns;
	int nu;
	public SamplingBag(int k){
		size = k;
		sorted = new Element[size];
		unsorted = new Element[size];
		zeroes=mask=ns=nu= 0;
	}
	/*
	 * incrMask - increments mask
	 */
	private void incrMask(){
		mask = (mask<<1) + 1;
	}
	/*
	 * shiftSorted - 
	 */
	private void shiftSorted(int i){
		for(int j = ns-1;j>i;j--){
			sorted[j]=sorted[--j];
		}
	}
	private void shiftUnsorted(int i){
		for(int j=i;j<nu-1;j++) unsorted[j]=unsorted[++j];
	}
	/*
	 * sortedInsert - 
	 */
	private boolean insertSorted(Element e){
		int hash = e.GetHash();
		int i=0;
		while (i<ns&&hash>sorted[i].GetHash()){
			i++;
		}
		if (hash==sorted[i].GetHash()||i==size) return false;
		shiftSorted(i);
		sorted[i]=e;
		return true;
	}
	/*
	 * sortedTryInsert -
	 */
	private void tryInsertSorted(Element e){
		int hash = e.GetHash();
		int maxHash = sorted[size-1].GetHash();
		if (ns<size&&insertSorted(e)) ns++;
		else if (hash<maxHash) insertSorted(e);
	}
	
	private void purgeAndTransfer(){
		ns=0;
		for(int i=0;i<size;i++){
			Element e=unsorted[i];
			if ((e.GetHash()&(mask+1))!=0){
				tryInsertSorted(e);
				shiftUnsorted(i);
				nu--;
			}
		}
	}
	/*
	 * insertUnsorted - inserts e in unsorted. Assumes that unsorted is not full, i.e. nu<size.
	 */
	private void insertUnsorted (Element e){
		for (int i=0;i<nu;i++){
			if (e.GetHash()==unsorted[i].GetHash())return;
		}
		unsorted[nu++]=e;
	}
	public void tryInsert(Element e){
		int hash = e.GetHash();
		if ((hash&mask)==0){
			if ((hash&(mask+1))!=0) tryInsertSorted(e);
			else if (nu<size) insertUnsorted(e);
			else {
				zeroes++;
				incrMask();
				purgeAndTransfer();
				tryInsert(e);
			}
		}
	}
}
