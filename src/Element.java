
public class Element {
	private String Content;
	private int ContentHash;
	private int NbWords;
	
    static{
        System.loadLibrary("libElement");
    }

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
	
    private native int Hash (String str);
    
    protected String removeFirstWord() throws IllegalArgumentException{
    	if (NbWords<2){throw new IllegalArgumentException("In function removeFirstWord : no word to remove");}
    	int i = 0;
    	while(Word.isLetter(Content.charAt(i))){i++;}
    	return Content.substring(i+1);    	
    }
}
