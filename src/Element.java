
public class Element {
	private String Content;
	private int ContentHash;
	
    static{
        System.loadLibrary("Element");
    }
	
    static native int Hash (String str);
 
    public Element (String Content) {
		this.Content = Content;
		this.ContentHash = Hash(Content);
	}
    
	public String GetContent () {
		return Content;
	}
	
	public int GetHash () {
		return ContentHash; 
	}
}
