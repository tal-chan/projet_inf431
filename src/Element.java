
public class Element {
	private String Content;
	private int ContentHash;
	
    static{
        System.loadLibrary("Element");
    }

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
	
    private native int Hash (String str);
}
