import java.io.IOException;


public class Test {
	public static void main (String[] args) throws IOException {
		String name = "/home/michel/Dropbox/INF431_Projet_chaton/Complete.txt";
		Data shakespeare = new Data(name, Data.FILE);
		System.out.printf("precise count : %d\n", shakespeare.count());
		for (int b=4; b<=16; b++) {
			System.out.printf("hyper log log count, b=%d : %f\n", b, new FingerPrint(shakespeare, b).hyperLogLog());
		}
		/*
		for (int i=0; i<=30; i++){
			System.out.println(shakespeare.nextElement().GetContent());
		}
		System.out.printf("aaa");*/
	}
}


