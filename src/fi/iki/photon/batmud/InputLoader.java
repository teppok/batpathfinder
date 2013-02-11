package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for loading external files.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class InputLoader {
	
	/**
	 * Load a file into a List<String>, ignoring any empty lines or
	 * lines beginning with # (comments).
	 * 
	 * @param fileName
	 * @param ignoreCommentLines 
	 * @return List containing the lines or null if input can't be read.
	 * @throws IOException When something weird happens.
	 */
	
	@SuppressWarnings("resource")
	static List<String> loadInput(String fileName, boolean ignoreCommentLines) throws IOException {
		File f = new File(fileName);
		if (!f.canRead()) return null;

		ArrayList<String> retVal = new ArrayList<>(20);

		BufferedReader reader = null;
		
		reader = new BufferedReader(new FileReader(f));
		
		boolean readMore = true;
		while (readMore) {
			String line = reader.readLine();
			if (line == null) { 
				readMore = false;
			} else {
				if (line.equals("")) {
					continue;
				}
				if (ignoreCommentLines && line.startsWith("#")) {
					continue;
				}

				retVal.add(line);
			}
		}
		reader.close();
		return retVal;
	}

}
