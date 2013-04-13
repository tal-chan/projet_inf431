/*
 * Defines whether or not a char represents a letter
 * in the broad sense (i.e : as opposed to a separator)
 */
public class Word {
	public static boolean isLetter(char c){
		return (c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='-';
	}
}
