import java.io.InputStream;
import java.util.Scanner;

/*
 * MyReader - parsing a data stream according to user settings.
 * prev - keeps in store the last element read.
 * isFirst - equals true if and only if next has never been called.
 * set - user settings for this data stream.
 */
public class MyReader {
	Scanner scan;
	String[] prev;
	Boolean isFirst;
	Settings set;
	
	protected MyReader(InputStream source, Settings s){
		scan = new Scanner(source);
		isFirst = true;
		set = s;
		prev = new String[set.k];
	}
	protected MyReader(Settings s){
		isFirst = true;
		set = s;
		prev = new String[set.k];
	}
	protected MyReader(){
		isFirst = true;
		set = new Settings();
		prev = new String[set.k];
	}
	/*
	 * init - Initializes the cursor position to the beginning of the file.
	 */
	protected void init(InputStream source){
		scan = new Scanner(source);
		isFirst = true;
		setDelimiters();
	}
	/*
	 * setDelimiters - sets element delimiters according to data type given in settings.
	 * NOTE : type LOG is not implemented, and will just set the delimiters to default value from Scanner class. 
	 */
	private void setDelimiters(){
		switch(set.type){
		case Settings.TEXT:
			scan.useDelimiter("[\\s!\"#\\$%\\&\'\\(\\)\\*\\+,-./:;\\<\\=\\>\\?@\\[\\]\\^_`\\{\\|\\}~]");
			break;
		default:
			break;
		}
	}
	protected boolean hasNext(){
		return scan.hasNext();
	}
	/*
	 * prevToString - returns a single String from prev contents.
	 */
	private String prevToString(){
		String s = new String();
		for (int i =0;i<prev.length;i++) s= s+prev[i];
		return s;
	}
	/*
	 * shiftPrev - Shifts prev content to the left, deleting the first element.
	 */
	private void shiftPrev(){
		for (int i=0;i<prev.length-1;i++) prev[i]=prev[++i];
	}
	/*
	 * next - returns the next element read in data stream
	 */
	protected Element next(){
		int k = set.k;
		if (isFirst){ //first time calling next(), initializing prev.
			int i=0;
			while(i<k&&scan.hasNext()){
				String tmp = scan.next();
				if (tmp.length()>0){
					prev[i]=tmp;
					i++;
				}
			}
			String s = prevToString().toLowerCase();
			isFirst=false;
			return new Element(s);
		}
		//next() has already been called once, only one element to read in the stream.
		String tmp = scan.next();
		while(tmp.length()==0&&scan.hasNext())tmp=scan.next();
		if(tmp.length()>0){
			shiftPrev();
			prev[k-1]=tmp;
		}
		tmp = prevToString().toLowerCase();
		return new Element(tmp);

	}

}
