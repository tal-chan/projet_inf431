
public class Element {
	private String Content;
	private int ContentHash;
	private int NbWords;
	
    static{
        System.loadLibrary("Element");
    }
	
    static native int Hash (String str);
 
    public Element (String Content) {
		this.Content = Content;
		this.ContentHash = Hash(Content);
	}
    
    public Element(String s, int k){
    	Content = s;
    	NbWords = k;
    	ContentHash = Hash(Content);
    }
    
    public int GetNbWords(){
    	return NbWords;
    }
	
	public String GetContent () {
		return Content;
	}
	
	public int GetHash () {
		return ContentHash; 
	}
}
