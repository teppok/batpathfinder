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

	/** Tradelane is vertical. */
	static final int VERT=0;
	/** Tradelane is horizontal. */
	static final int HORIZ=1;
	/** Tradelane is diagonally northeast. */
	static final int UP=2;
	/** Tradelane is diagonally southeast. */
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
		/** Tradelane starting point. */
		final PlaneLocation start;
		/** Tradelane ending point. */
		final PlaneLocation end;
		/** Tradelane direction. */
		final int direction;

		/**
		 * Basic constructor for TradeLane.
		 * Tradelane must be horizontal, vertical, or at exactly 45 degree tilt.
		 * @param p1
		 * @param p2
		 * @throws IOException if planelocations specify a faulty Tradelane.
		 */
		TradeLane(PlaneLocation p1, PlaneLocation p2) throws IOException {
			
			if (p1 == null || p2 == null) throw new IOException("Malformed tradelane");
			
			if (p1.getX() == p2.getX()) {
				if (p1.getY() <= p2.getY()) {
					start = p1; end = p2;
					direction = VERT;
				} else {
					start = p2; end = p1;
					direction = VERT;
				}
			}
			else if (p1.getY() == p2.getY()) {
				if (p1.getX() < p2.getX()) {
					start = p1; end = p2;
					direction = HORIZ;
				} else {
					start = p2; end = p1;
					direction = HORIZ;
				}
			}
			else if (p1.getX() < p2.getX()) {
				if (p1.getY() < p2.getY()) {
					start = p1; end = p2;
					direction = DOWN;
				} else {
					start = p1; end = p2;
					direction = UP;
				}
			}
			else // Now (p1.getX() > p2.getX()) {
				if (p1.getY() < p2.getY()) {
					start = p2; end = p1;
					direction = UP;
				} else {
					start = p2; end = p1; 
					direction = DOWN;
				}
			
			if (direction == DOWN || direction == UP) {
				if (Math.abs(end.getX() - start.getX()) != Math.abs(end.getY() - start.getY())) {
					throw new IOException("Malformed tradelane");
				}
			}

		}
	}

//	private final ArrayList<ArrayList<TradeLane>> tradeLaneArray;

	private final ArrayList<TradeLane> tradeLanes;
	
	/**
	 * A basic constructor. Specify files from where to read tradelanes
	 * and naval costs.
	 * @param fileName
	 * @param minx 
	 * @param maxx 
	 * @param miny 
	 * @param maxy 
	 * @param fixx 
	 * @param fixy 
	 * @throws IOException if file reading fails due to errors or faulty data.
	 */
	
	Tradelanes(String fileName, int minx, int maxx, int miny, int maxy, int fixx, int fixy) throws IOException {
		tradeLanes = new ArrayList<>(5);
/*
		tradeLaneArray.add(0, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(1, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(2, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(3, new ArrayList<TradeLane>(5));
		tradeLaneArray.add(4, new ArrayList<TradeLane>(5));
*/		
		loadTradelanes(fileName, minx, maxx, miny, maxy, fixx, fixy);
	}

	/**
	 * Given x and y in the Ggr tradelane format, finds out the
	 * continent this point is on. -1 if not on any.
	 * 
	 * @param x
	 * @param y
	 * @return continent number like AreaContainer.CONT_LAENOR
	 */
/*	
	static private int findContinent(int x, int y) {
		
		if (x > 4000 && x < 5000 && y > 4000 && y < 5000) return AreaContainer.CONT_LAENOR;
		
		if (x > 2900 && x < 3600 && y > 4900 && y < 5500) return AreaContainer.CONT_DESO;

		if (x > 3400 && x < 4500 && y > 6400 && y < 7000) return AreaContainer.CONT_LUC;
		
		if (x > 5300 && x < 5600 && y > 5000 && y < 6000) return AreaContainer.CONT_FURN;
		
		if (x > 5300 && x < 6000 && y > 2800 && y < 3300) return AreaContainer.CONT_ROTH;

		return -1;
	}
*/
	/**
	 * Given x in the Ggr tradelane format, and a continent,
	 * returns the x value inside that continent.
	 * @param continent
	 * @param x
	 * @return fixed x
	 */
/*	
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
		// continent == AreaContainer.CONT_ROTH
		return x - 4097 - 1311;
	}
*/
	/**
	 * Given y in the Ggr tradelane format, and a continent,
	 * returns the y value inside that continent.
	 * @param continent
	 * @param y
	 * @return fixed y
	 */
	

	/*
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
		// continent == AreaContainer.CONT_ROTH
		return y - 4097 + 1255;
	}
*/
	/**
	 * Load tradelanes from the file into memory.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	
	private void loadTradelanes(String fileName, int minx, int maxx, int miny, int maxy, int fixx, int fixy) throws IOException {
		File f = new File(fileName);
		if (!f.exists()) throw new IOException();
		
		HashMap<String, PlaneLocation> markers = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			boolean readMore = true;
			while (readMore) {
				String line = reader.readLine();
				if (line == null) { readMore = false; } else {
					if (!"".equals(line) && !line.startsWith("#")) {
						if (line.startsWith("!")) {
							String parts[] = line.split(" ");
							if (parts.length != 3) throw new IOException("Malformed " + fileName);
							if ("".equals(parts[1])) throw new IOException("Malformed " + fileName);
							String name1 = parts[1].trim();
							String name2 = parts[2].trim();
							PlaneLocation p1 = markers.get(name1);
							PlaneLocation p2 = markers.get(name2);
							
							if (p1 != null && p2 != null) {
								TradeLane tl = new TradeLane(p1, p2);
								tradeLanes.add(tl);
							}
						} else {
							String parts[] = line.split("\t");
							if (parts.length != 3) throw new IOException("Malformed " + fileName);
							
							int locX, locY;
							try {
								locX = Integer.parseInt(parts[0].trim());
								locY = Integer.parseInt(parts[1].trim());
							} catch (NumberFormatException e) {
								throw new IOException("Malformed " + fileName);
							}
							if (locX >= minx && locX < maxx && locY >= miny && locY < maxy) {
//							int continent = findContinent(locX, locY);
								String name = parts[2].trim();
							
//							if (continent >= 0) {
								locX += fixx;
								locY += fixy;
//								locX = fixX(continent, locX);
//								locY = fixY(continent, locY);
						
								PlaneLocation pl = new PlaneLocation(locX, locY, 0);
								markers.put(name,pl);
							}
						}
					}
				}
			}		
		}
	}

	/**
	 * Returns true if the specified point is on a tradelane.
	 * 
	 * @param x
	 * @param y
	 * @return true if x,y,cont is on a trade lane
	 */
	
	boolean isOnTradeLane(int x, int y) {
		
//		ArrayList<TradeLane> tla = tradeLaneArray.get(cont);
		for (int i=0; i < tradeLanes.size(); i++) {
			TradeLane tl = tradeLanes.get(i);
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
			// direction == UP
			if (tl.start.getX() <= x && x <= tl.end.getX()) {
				int dx = x - tl.start.getX();
				int dy = - y + tl.start.getY();
				if (dx == dy) { return true; }
			}
		}
		return false;
	}
	

}
