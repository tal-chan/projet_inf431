public class Word {
	
	/*
	 * isLetter - Defines what constitutes a letter in the broad sense,
	 * ie a character found in word. All other characters are considered separators.
	 * The definition of a word will depend on the type of input data (e.g. numbers,accented letters or periods
	 * may or may not be considered as letters)
	 */
	
	public static boolean isLetter(char c){
		//return (c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-'||(c>=128&&c<=154)||(c>=160&&c<=165);
		return (c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>=128&&c<=154)||(c>=160&&c<=165);
	}
}
