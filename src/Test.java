import java.io.IOException;


public class Test {
	public static void main (String[] args) throws IOException {
		String name = "C:/Users/Chantal Ding/Dropbox/INF431_Projet_chaton/Texts/";
		/*String f1 = "1henryiv.txt";
		String f2 = "1henryvi.txt";
		Data henryiv = new Data (name+f1,Data.FILE);
		Data henryvi = new Data (name+f2,Data.FILE);
		FingerPrint fp = new FingerPrint(henryiv,3);
		FingerPrint fp2 = new FingerPrint(henryvi,3);

		System.out.println(fp.similarity(fp2));*/
		
		
		Data[] data = FingerPrint.extractData(name);
		double[][] sim = FingerPrint.similarities(FingerPrint.fingerprints(data,1,11));
		int l = sim.length;
		for(int i=0;i<l;i++){
			for (int j=0;j<l;j++){
				System.out.print(sim[i][j]+"\t");
			}
			System.out.println();
		}
		/*
		for (int i=0; i<=30; i++){
			System.out.println(shakespeare.nextElement().GetContent());
		}
		System.out.printf("aaa");*/
	}
}


