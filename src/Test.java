import java.io.IOException;


public class Test {
	public static void main (String[] args) throws IOException, NoMoreElement {
		String name = "/home/michel/git/chaton/src/Test.java";
		Data shakespeare = new Data(name, Data.FILE);
		for (int i = 0; i<=30; i++){
			System.out.println(shakespeare.nextElement().GetContent());
		}
		System.out.printf("aaa");
	}
}


