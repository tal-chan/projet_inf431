public class Word {
	
	public static boolean isLetter(char c){
		return (c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-'||(c>=128&&c<=154)||(c>=160&&c<=165);
	}
}
