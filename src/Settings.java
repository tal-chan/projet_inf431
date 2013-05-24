/*
 * Settings - Defines reading settings.
 * type - type of data to process
 * k - size of shingles read
 */
public class Settings {
	public final static int TEXT=0;
	public final static int LOG=1;
	public int type;
	public int k;
	/*
	 * default constructor
	 */
	public Settings(){
		type = TEXT;
		k = 1;
	}
}
