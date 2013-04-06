import java.io.IOException;


public class Test {
	public static void main (String[] args) throws IOException, EndFileException {
		String name = "G:/Documents/X/Scola/2012_2013/INF431/projet/test.txt";
		Data shakespeare = new Data(name,Data.FILE);
		for (int i = 0;i<=50;i++){
			System.out.println(shakespeare.nextElement().GetContent());
		}
		System.out.printf("aaa");
	}
}


