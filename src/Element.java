
public class Element {
	private String Content;
	private long Hash;
	
    static{
        System.loadLibrary("Element");
    }

    public Element (String Content) {
		this.Content = Content;
		Hash();
	}
	
	public String GetContent () {
		return Content;
	}
	
	public long GetHash () {
		return Hash; 
	}
	
    private native void Hash();
}
