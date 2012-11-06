package ambar;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Dict {
	private static Dict instance = null;
	private DataInputStream idx, dict, ifo;

	private Dict() {
	}

	public static Dict getDict() {
		if (instance == null) {
			instance = new Dict();
			return instance;
		} else {
			return instance;
		}
	}

	private void openResource(char letter) {
		letter = Character.toLowerCase(letter);
		idx = new DataInputStream(getClass().getResourceAsStream(
				"/" + letter + ".idx"));
		dict = new DataInputStream(getClass().getResourceAsStream(
				"/" + letter + ".dict"));
		ifo = new DataInputStream(getClass().getResourceAsStream(
				"/" + letter + ".ifo"));
	}

	public String trans(String search) throws WordNotFoundException {
		openResource(search.charAt(0));
		String word = "";
		StringBuffer wordBuffer;
		search = search.toUpperCase();
		int wordCount = 0;
		int offset = -1;
		int size = -1;
		char c = 0;

		byte[] bites = new byte[4];
		try {
			ifo.read(bites);
			wordCount = Integer.parseInt(new String(bites, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("word count: " + wordCount);
		for (int i = 0; i < wordCount; i++) {
			try {

				if (idx == null) {
					System.out.println("idx is null");
				}

				// System.out.println(idx.available());
				wordBuffer = new StringBuffer(16);
				while ((c = (char) idx.readByte()) != '\0') {
					wordBuffer.append(c);
				}
				word = wordBuffer.toString();
				// System.out.println(word);

				offset = idx.readInt();
				size = idx.readInt();

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (search.equals(word.trim())) {
				break;
			}

			word = "";
			size = 0;
			offset = 0;
		}

		try {
			idx.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!word.equals("")) {
			return getWord(offset, size);
		} else {
			throw new WordNotFoundException();
		}
	}

	private String getWord(int offset, int size) {
		String s = "";
		byte[] bytes = new byte[size];

		try {
			dict.skipBytes(offset);

			for (int i = 0; i < size; i++) {
				bytes[i] = dict.readByte();
			}

			dict.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			s = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
}
