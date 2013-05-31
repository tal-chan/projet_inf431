import java.io.IOException;

/*
 * Icebergs - Extracting elements that appear with higher frequency than a given threshold  
 */
public class Icebergs {
	/*
	 * shiftRight - shifts the elements of the array to the right,
	 * in order to insert a new element at index i
	 */
	private static void shiftRight(Element[] array, int i){
		for (int j=array.length-1;j>i;j--) array[j]=array[j-1];
	}
	/*
	 * shiftLeft - shifts the elements of the array to the left,
	 * deleting the element at index i.
	 */
	private static void shiftLeft(Element[] array, int i){
		for (int j = i;j<array.length-1;j++) array[j] = array[j+1];
		array[array.length-1]=null;
	}
	/*
	 * offerElement - inserts an element in a sorted Element array.
	 * returns true if number of distinct elements has increased after insertion,
	 * i.e. the element was not already in the array.
	 */
	private static boolean offerElement(Element[] array, Element e,int size){
		if (size==0){ //dealing with empty array
			array[0] = e;
			return true;
		}
		int i = 0;
		while(i<size&&e.GetHash()>array[i].GetHash()){
			i++;
		}
		if (i<size&&e.GetHash()==array[i].GetHash()){
			array[i].incrCount();
			return false;
		}
		shiftRight(array,i);
		array[i]=e;
		return true;
	}
	/*
	 * extract - implementing Karp et alii algorithm to extract icebergs
	 * theta - minimal frequency desired for the icebergs
	 */
	private static Element [] extract(Data input, double theta) throws IOException{
		int capacity = (int) Math.ceil(1/theta);
		int size = 0;
		Element[] bag = new Element[capacity];
		input.init();
		while(input.hasNext()){
			Element e = input.nextElement();
			if(size<capacity){
				if(offerElement(bag,e,size))size ++;
			}
			else{
				int i = 0;
				while(i<size){
					if (bag[i].getCount()==0){
						shiftLeft(bag,i);
						size--;
					}
					else {
						bag[i].decrCount();
						i++;
					}
				}
			}
		}
		return bag;
	}
	public static void printIcebergs(Data input, double theta)throws IOException{
		Element[] res = extract(input,theta);
		for (Element e : res){
			if (e!=null)System.out.println(e.GetContent());
		}
	}
	public static void main(String[] args) throws IOException{
		Data data = new Data("Texts/fravia/pages.txt",Data.FILE);
		Icebergs.printIcebergs(data,0.005);
	}
}
