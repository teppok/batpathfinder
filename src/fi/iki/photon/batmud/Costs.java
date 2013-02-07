package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class that will tell the costs of movement in the terrain.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class Costs {
	private final short[] navalCosts1;
	private final short[] navalCosts2;

	private final short[] costs;

	/**
	 * Standard constructor.
	 * @param costFileName File with costs
	 * @param navalCostFileName File with naval costs
	 * @throws IOException If file reading fails.
	 */
	
	Costs(String costFileName, String navalCostFileName) throws IOException {
		navalCosts1 = new short[255];
		navalCosts2 = new short[255];
		costs = new short[255];
		if (costFileName == null) throw new IOException();
		if (navalCostFileName == null) throw new IOException();
		
		loadCosts(costFileName);
		loadNavalCosts(navalCostFileName);
	}
	
	/**
	 * Loads the costs from the filename into the costs array.
	 * @param fileName
	 * @throws IOException
	 */
	private void loadCosts(String fileName) throws IOException {
		for (int i=0; i<255; i++) { costs[i] = -1; }
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			boolean readMore = true;
			while (readMore) {
				String line = reader.readLine();
				if (line == null) { readMore = false; } else {
					String parts[] = line.split(" ");
					if (parts.length != 2) throw new IOException("Malformed " + fileName);
					char terrain = parts[0].charAt(0);
					short terrainCost = Short.parseShort(parts[1]);
					costs[terrain] = terrainCost;
				}
			}
		}
	}

	/**
	 * Load the naval costs.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	
	private void loadNavalCosts(String fileName) throws IOException {
		for (int i=0; i<255; i++) { navalCosts1[i] = -1; navalCosts2[i] = -1; }
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			boolean readMore = true;
			while (readMore) {
				String line = reader.readLine();
				if (line == null) { readMore = false; } else {
					String parts[] = line.split(" ");
					if (parts.length != 3) throw new IOException("Malformed " + fileName);
					
					char terrain = parts[0].charAt(0);
					short minCost, maxCost;
					try {
						minCost = Short.parseShort(parts[1]);
						maxCost = Short.parseShort(parts[2]);
					} catch (NumberFormatException e) {
						throw new IOException("Malformed " + fileName);
					}
					navalCosts1[terrain] = minCost;
					navalCosts2[terrain] = maxCost;
				}
			}
		}
	}


	/**
	 * Returns the movement cost for terrain type c.
	 * @param c
	 * @return Movement cost.
	 */
	
	short calcWeight(char c) {
		short w = costs[c];
		if (w < 0) return 10000;
		return w;
	}


	/**
	 * Returns the minimum lift where movement is possible on terrain c.
	 * @param c
	 * @return minimum possible lift
	 */
	
	short getMinLift(char c) {
		short w = navalCosts1[c];
		if (w < 0) return 10000;
		return w;
	}
	/**
	 * Returns the lift where movement is unhindered on terrain c.
	 * @param c
	 * @return minimum unhindered lift
	 */
	
	short getMaxLift(char c) {
		short w = navalCosts2[c];
		if (w < 0) return 10000;
		return w;
	}


}
