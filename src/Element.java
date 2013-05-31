/*
 * Element - a representation of an element of the input data
 * Content - the element as read from the input data
 * ContentHash - a hash of the element
 * count - a counter, for statistical sampling purposes 
 */
public class Element {
	private String Content;
	private int ContentHash;
	private int count = 0;
	
    static{
        System.loadLibrary("Element");
    }
	
    static native int Hash (String str);
 
    protected Element (String Content) {
		this.Content = Content;
		this.ContentHash = Hash(Content);
	}
    
    protected String GetContent () {
		return Content;
	}
	
    protected int GetHash () {
		return ContentHash; 
	}
    protected int getCount() {
		return count;
	}
	protected void incrCount(){
		count ++;
	}
	protected void decrCount(){
		count --;
	}
}
