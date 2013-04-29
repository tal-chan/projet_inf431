import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HashTest {

	/*
	 *  In construction...
	 */

	public static void main(String[] args) throws IOException {
		String name = "C:/Users/Chantal Ding/Dropbox/INF431_Projet_chaton/Complete.txt";
		Data data = new Data(name,Data.FILE);
		Map <Integer,Integer> count = new HashMap<Integer,Integer>();
		try {
			for(Element el = data.FirstElement(1);;el=data.nextElement()) {
				int hash = el.GetHash();
			}

		} catch (Data.NoMoreElement e) {}
		
	}
}
