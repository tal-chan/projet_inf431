/*
 * SamplingBag - auxiliary data structure implementing sampling algorithm.
 * size - desired sampling size
 * zeroes - minimum number of zeroes on the right for hash values in the bag
 * sorted - first half of the bag, containing elements with exactly zeroes zeroes.
 * This is a sorted array.
 * ns - number of elements stored in sorted.
 * unsorted - second half of the bag, containing elements with strictly more than zeroes zeroes.
 * nu - number of elements stored in unsorted.
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
	 * init - initializing the bag.
	 */
	public void init(){
		zeroes=mask=ns=nu= 0;
	}
	/*
	 * incrMask - increments mask
	 */
	protected void incrMask(){
		mask = (mask<<1) + 1;
	}
	/*
	 * shiftSorted - shifts all elements of sorted with index >=i to the right
	 */
	protected void shiftSorted(int i){
		for(int j = size-1;j>i;j--){
			sorted[j]=sorted[j-1];
		}
	}
	/*
	 * shiftUnsorted - shifts all elements of unsorted with index >i to the left,
	 * deleting unsorted[i]
	 */
	protected void shiftUnsorted(int i){
		for(int j=i;j<nu-1;j++) unsorted[j]=unsorted[j+1];
	}
	/*
	 * insertSorted - inserts e in the sorted array, deleting the last element if necessary.
	 */
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
		if (i==ns){
			sorted[i]=e;
			return true;
		}
		if (hash==sorted[i].GetHash()||i==size) return false;
		shiftSorted(i);
		sorted[i]=e;
		return true;
	}
	/*
	 * sortedTryInsert - if sorted is not full (i.e. ns<size), inserts e in sorted.
	 * Otherwise, e is inserted only if its hash is lower than the maximum hash stored in sorted,
	 * deleting the last element of the array.
	 */
	protected void tryInsertSorted(Element e){
		int hash = e.GetHash();
		if (ns<size){
			if(insertSorted(e)) ns++;
		}
		else {
			int maxHash = sorted[size-1].GetHash();
			if (hash<maxHash) insertSorted(e);
		}
	}
	/*
	 * purgeAndTransfer - empties sorted and transfers eligible elements from unsorted to sorted
	 */
	protected void purgeAndTransfer(){
		ns=0;
		int i = 0;
		while(i<nu){
			Element e=unsorted[i];
			if ((e.GetHash()&(mask+1))!=0){
				tryInsertSorted(e);
				shiftUnsorted(i);
				nu--;
			}
			else i++;
		}
	}
	/*
	 * insertUnsorted - inserts e in unsorted. Assumes that unsorted is not full, i.e. nu<size.
	 */
	protected void insertUnsorted (Element e){
		for (int i=0;i<nu;i++){
			if (e.GetHash()==unsorted[i].GetHash())return;
		}
		unsorted[nu++]=e;
	}
	/*
	 * tryInsert - tries to insert an element in the bag. If the bag is full (i.e. if nu=size)
	 * then zeroes and mask are incremented, and the bag purged accordingly. 
	 */
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
	public Element[] getContent(){
		Element[] res = new Element[size];
		for (int i=0;i<nu;i++){
			res[i]=unsorted[i];
		}
		for (int j=0;j<size-nu&&j<ns;j++){
			res[j+nu]=sorted[j];
		}
		return res;
	}
}
