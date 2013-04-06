import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;



public class Data {
	public static final int FILE = 0;
	public static final int URL = 1;
	private final int type;
	private BufferedReader stream;
	
	
	public Data (String name, int type) throws IOException {
		this.type = type;
		if (this.type==FILE){
		stream = new BufferedReader(new FileReader(name));
		}
		else{
			URL url = new URL(name);
			stream = new BufferedReader(new InputStreamReader(url.openStream()));
		}
	}

	public Element nextElement () throws EndFileException {
		String content = new String();
		try{
			char c = (char)stream.read();
			while(!((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-')){c=(char)stream.read();}
			while((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-'){
				content = content+c;
				c = (char)stream.read();
			}
			
		} catch(IOException e){throw new EndFileException("reached end of file");}
		return new Element(content);
	}
}
