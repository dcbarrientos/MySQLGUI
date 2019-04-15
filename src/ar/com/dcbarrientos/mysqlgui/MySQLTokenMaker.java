package ar.com.dcbarrientos.mysqlgui;

import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.modes.SQLTokenMaker;

public class MySQLTokenMaker extends SQLTokenMaker{
	static TokenMap extraTokens;
	
	public MySQLTokenMaker() {
		extraTokens = getKeyWords();
	}
	
	
	static public TokenMap getKeyWords() {
		if(extraTokens == null) {
			extraTokens = new TokenMap(false);
			
			//HashMap<String, Integer> keywords = 
		}
		
		return extraTokens;
	}
}
