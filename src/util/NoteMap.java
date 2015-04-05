package util;

import java.util.TreeMap;

public class NoteMap {
	public static TreeMap<String, String> getInstance() {
		if (map == null) {
			NoteMap.map = new TreeMap<>();
			String[] notas = { "pad", "a,,,", "b,,,", "c,,", "d,,", "e,,",
					"f,,", "g,,", "a,,", "b,,", "c,", "d,", "e,", "f,", "g,",
					"a,", "b,", "c", "d", "e", "f", "g", "a", "b", "c'", "d'",
					"e'", "f'", "g'", "a'", "b'", "c''", "d''", "e''", "f''",
					"g''", "a''", "b''", "c'''", "d'''", "e'''", "f'''",
					"g'''", "a'''", "b'''", "c''''", "d''''", "e''''", "f''''",
					"g''''", "a''''", "b''''", "c'''''" };

			for (int i = 1; i <= 52; i++) {
				NoteMap.map.put(String.valueOf(i), notas[i]);

			}
		}
		return map;
	}

	private static TreeMap<String, String> map;

	private NoteMap() {
	}
}
