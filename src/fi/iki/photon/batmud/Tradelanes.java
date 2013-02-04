package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that contains the logic pertaining to tradelanes and naval travel.
 * 
 * Tradelanes are loaded at program start and other functions query
 * whether a specified point is on a tradelane or not.
 * 
 * Only tradelanes inside one continent are loaded - cross-continent
 * tradelanes are not supported, their travel is handled via
 * NameLocation network.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class Tradelanes {

	public static final int VERT=0;
	public static final int HORIZ=1;
	public static final int UP=2;
	public static final int DOWN=3;
	
	/**
	 * A class representing one tradelane from PlaneLocation start to
	 * PlaneLocation end. Direction is used when determining if a point
	 * is on a tradelane.
	 * 
	 * @author Teppo Kankaanp‰‰
	 *
	 */
	
	class TradeLane {
		final PlaneLocation start, end;
		final int direction;
		
		TradeLane(PlaneLocation p1, PlaneLocation p2) {
			
			if (p1.getX() == p2.getX() && p1.getY() < p2.getY()) {
				start = p1; end = p2;
				direction = VERT;
			}
			else if (p1.getX() == p2.getX() && p1.getY() > p2.getY()) {
				start = p2; end = p1;
				direction = VERT;
			}
			else if (p1.getX() < p2.getX() && p1.getY() == p2.getY()) {
				start = p1; end = p2;
				direction = HORIZ;
			}
			else if (p1.getX() > p2.getX() && p1.getY() == p2.getY()) {
				start = p2; end = p1;
				direction = HORIZ;
			}
			else if (p1.getX() < p2.getX() && p1.getY() < p2.getY()) {
				start = p1; end = p2;
				direction = DOWN;
			}
			else if (p1.getX() < p2.getX() && p1.getY() > p2.getY()) {
				start = p1; end = p2;
				direction = UP;
			}
			else if (p1.getX() > p2.getX() && p1.getY() < p2.getY()) {
				start = p2; end = p1;
				direction = UP;
			}
			else if (p1.getX() > p2.getX() && p1.getY() > p2.getY()) {
				start = p2; end = p1; 
				direction = DOWN;
			} else { start = null; end = null; direction = -1; }
		}
	}

	private short[] navalCosts1;
	private short[] navalCosts2;
	private final ArrayList<ArrayList<TradeLane>> tradeLaneArray;

	/**
	 * A basic constructor.
	 */
	
	Tradelanes() {
		tradeLaneArray = new ArrayList<>();
		tradeLaneArray.add(0, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(1, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(2, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(3, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(4, new ArrayList<TradeLane>(5));
	}

	/**
	 * Given x and y in the Ggr tradelane format, finds out the
	 * continent this point is on. -1 if not on any.
	 * 
	 * @param x
	 * @param y
	 * @return continent number like AreaContainer.CONT_LAENOR
	 */
	
	static private int findContinent(int x, int y) {
		
		if (x > 4000 && x < 5000 && y > 4000 && y < 5000) return AreaContainer.CONT_LAENOR;
		
		if (x > 2900 && x < 3600 && y > 4900 && y < 5500) return AreaContainer.CONT_DESO;

		if (x > 3400 && x < 4500 && y > 6400 && y < 7000) return AreaContainer.CONT_LUC;
		
		if (x > 5300 && x < 5600 && y > 5000 && y < 6000) return AreaContainer.CONT_FURN;
		
		if (x > 5300 && x < 6000 && y > 2800 && y < 3300) return AreaContainer.CONT_ROTH;

		return -1;
	}

	/**
	 * Given x in the Ggr tradelane format, and a continent,
	 * returns the x value inside that continent.
	 * @param continent
	 * @param x
	 * @return fixed x
	 */
	
	static private int fixX(int continent, int x) {
		if (continent == AreaContainer.CONT_LAENOR) {
			return x - 4097;
		}
		if (continent == AreaContainer.CONT_DESO) {
			return x - 4097 + 1211;
		}
		if (continent == AreaContainer.CONT_FURN) {
			return x - 4097 - 1211;
		}
		if (continent == AreaContainer.CONT_LUC) {
			return x - 4097 + 634;
		}
		if (continent == AreaContainer.CONT_ROTH) {
			return x - 4097 - 1311;
		}
		return 0;
	}

	/**
	 * Given y in the Ggr tradelane format, and a continent,
	 * returns the y value inside that continent.
	 * @param continent
	 * @param y
	 * @return fixed y
	 */
	

	
	static private int fixY(int continent, int y) {
		if (continent == AreaContainer.CONT_LAENOR) {
			return y - 4097;
		}
		if (continent == AreaContainer.CONT_DESO) {
			return y - 4097 - 819;
		}
		if (continent == AreaContainer.CONT_FURN) {
			return y - 4097 - 1155;
		}
		if (continent == AreaContainer.CONT_LUC) {
			return y - 4097 - 2345;
		}
		if (continent == AreaContainer.CONT_ROTH) {
			return y - 4097 + 1255;
		}
		return 0;
	}

	/**
	 * Load tradelanes from the file into memory.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	
	void loadTradeLanes(String fileName) throws IOException {
		File f = new File(fileName);
		if (!f.exists()) return;
		
		HashMap<String, PlaneLocation> markers = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			boolean readMore = true;
			while (readMore) {
				String line = reader.readLine();
				if (line == null) { readMore = false; } else {
					if (!"".equals(line) && !line.startsWith("#")) {
						if (line.startsWith("!")) {
							String parts[] = line.split(" ");
							byte cont = Byte.parseByte(parts[1].trim());
							String name1 = parts[2].trim();
							String name2 = parts[3].trim();
							PlaneLocation p1 = markers.get(name1);
							PlaneLocation p2 = markers.get(name2);
							
							TradeLane tl = new TradeLane(p1, p2);
							
							ArrayList<TradeLane> tla = tradeLaneArray.get(cont);
							tla.add(tl);
						} else {
							//bp.error(line);
							String parts[] = line.split("\t");
							int locX = Integer.parseInt(parts[0].trim());
							int locY = Integer.parseInt(parts[1].trim());
							int continent = findContinent(locX, locY);
							String name = parts[2].trim();
							
							if (continent >= 0) {
								locX = fixX(continent, locX);
								locY = fixY(continent, locY);
						
								PlaneLocation pl = new PlaneLocation(locX, locY, continent);
								markers.put(name,pl);
							}
						}
					}
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
	
	void loadCosts(String fileName) throws IOException {
		navalCosts1 = new short[255];
		navalCosts2 = new short[255];
		for (int i=0; i<255; i++) { navalCosts1[i] = -1; navalCosts2[i] = -1; }
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			boolean readMore = true;
			while (readMore) {
				String line = reader.readLine();
				if (line == null) { readMore = false; } else {
					String parts[] = line.split(" ");
					char terrain = parts[0].charAt(0);
					short minCost = Short.parseShort(parts[1]);
					short maxCost = Short.parseShort(parts[2]);
					navalCosts1[terrain] = minCost;
					navalCosts2[terrain] = maxCost;
				}
			}
		}
	}

	/**
	 * Returns true if the specified point is on a tradelane.
	 * 
	 * @param x
	 * @param y
	 * @param cont
	 * @return true if x,y,cont is on a trade lane
	 */
	
	boolean isOnTradeLane(int x, int y, int cont) {
		ArrayList<TradeLane> tla = tradeLaneArray.get(cont);
		for (int i=0; i < tla.size(); i++) {
			TradeLane tl = tla.get(i);
			//bp.error(tl.start + ".." + tl.end + "-" + tl.direction);
			if (tl.direction == VERT) {
				if ( tl.start.getX() == x && tl.start.getY() <= y &&  y <= tl.end.getY()) {
					return true;
				}
			}
			else if (tl.direction == HORIZ) {
				if (tl.start.getX() <= x && x <= tl.end.getX() && tl.start.getY() == y) {
					return true;
				}
			}
			else if (tl.direction == DOWN) {
				if (tl.start.getX() <= x && x <= tl.end.getX()) {
					int dx = x - tl.start.getX();
					int dy = y - tl.start.getY();
					if (dx == dy) { return true; }
				}
			}
			else if (tl.direction == UP) {
				if (tl.start.getX() <= x && x <= tl.end.getX()) {
					int dx = x - tl.start.getX();
					int dy = - y + tl.start.getY();
					if (dx == dy) { return true; }
				}
			}
		}
		return false;
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
