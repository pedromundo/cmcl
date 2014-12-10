package util;

import java.util.TreeMap;

public class NoteMap {
	private TreeMap<String, String> map;
	
	public NoteMap() {
		this.map = new TreeMap<>();
		String[] notas = {"pad","a,,,", "b,,,", "c,,", "d,,", "e,,", "f,,", "g,,",
		                  "a,,", "b,,", "c,", "d,", "e,", "f,", "g,",
		                  "a,", "b,", "c",  "d", "e", "f", "g",  
		                  "a", "b", "c'", "d'", "e'", "f'", "g'",
		                  "a'", "b'", "c''", "d''", "e''", "f''", "g''",
		                  "a''", "b''", "c'''", "d'''", "e'''", "f'''", "g'''",
		                  "a'''", "b'''", "c''''", "d''''", "e''''", "f''''", "g''''",
		                  "a''''", "b''''", "c'''''"  };
		
		for (int i = 1; i <= 52; i++) {
			this.map.put(String.valueOf(i), notas[i]);
			
		}
	}

	public TreeMap<String, String> getMap() {
		return map;
	}
}
