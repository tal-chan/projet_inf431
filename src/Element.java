
public class Element {
	private String Content;
	private long Hash;
	
	public Element (String Content) {
		this.Content = Content;
	}
	
	public String GetContent () {
		return Content;
	}
	
	public void SetHash (long Hash) {
		this.Hash = Hash;
	}
	
	public long GetHash () {
		return Hash; 
	}
}
