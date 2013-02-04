package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one continent in the game Batmud.
 * 
 * @author Teppo Kankaanpää
 *
 */

class Area {
	private final Tradelanes tradeLanes;
	private final int thisContinent;
	
	private final short[] costs;
	private final char[][] data;
	private final int sizex, sizey;

	/**
	 * Basic initialization.
	 * 
	 * @param cont Continent identifier such as AreaContainer.LAENOR
	 * @param tl Tradelanes object for calculating costs on tradelanes.
	 * @param sx Width of the map.
	 * @param sy Height of the map.
	 * @param fileName File name containing the map data as a character array.
	 * @param costFileName File name containing cost data for different terrain types.
	 * @throws IOException When file loading fails.
	 */
	Area(int cont, Tradelanes tl, int sx, int sy, String fileName, String costFileName) throws IOException {
		thisContinent = cont;
		tradeLanes = tl;
		sizex = sx;
		sizey = sy;
		data = new char[sizex][sizey];
		costs = new short[255];

		loadContinent(fileName);
		loadCosts(costFileName);
	}

	/**
	 * Returns the terrain character at location x, y.
	 * @param x
	 * @param y
	 * @return Terrain character
	 */
	
	char getData(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return data[x][y];
	}

	/**
	 * Returns true if the parameter is a location inside the boundaries of the map.
	 * @param l
	 * @return true if l is inside the boundaries of the map.
	 */
	boolean validLocation(PlaneLocation l) {
		if (l.getX() < 0 || l.getY() < 0 || l.getX() >= sizex || l.getY() >= sizey) return false;
		return true;
	}

	/**
	 * Loads the terrain from the filename into the data array.
	 * @param fileName
	 * @throws IOException
	 */
	private void loadContinent(String fileName) throws IOException {
		File f = new File(fileName);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
		
			int i=0, j=0;
	
			for (i=0; i<sizey; i++) {
				String line = reader.readLine();
				if (line == null) { 
					throw new IOException("Premature end of file.");
				}
	
				for (j=0; j<sizex; j++) {
					char c;
					try { 
						c = line.charAt(j);
					} catch (IndexOutOfBoundsException e) {
						throw new IOException("Premature end of file.");
					}
					data[j][i] = c;
				}
			}
		}
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
					char terrain = parts[0].charAt(0);
					short terrainCost = Short.parseShort(parts[1]);
					costs[terrain] = terrainCost;
				}
			}
		}
	}

	/**
	 * Returns a PlaneLocation object if it's inside this map,
	 * or null if it's outside the boundaries.
	 * @param x
	 * @param y
	 * @return PlaneLocation with the given x, y.
	 */
	
	PlaneLocation getPlaneLocation(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return null;
		return new PlaneLocation(x, y, thisContinent);
	}

	/**
	 * Returns the cost of movement at location x, y. If naval is true, returns
	 * naval costs, otherwise walking costs. Naval costs classes are
	 * 2 for tradelanes, 30 for slow movement, 10 for fast movement.
	 * @param x
	 * @param y
	 * @param naval
	 * @param lift
	 * @return Cost of movement or 10000 if impenetrable.
	 */

	private int getCost(int x, int y, boolean naval, int lift) {
		char c = getData(x, y);
		if (c == 'c' || c == 'C' || c == 0) return 10000;
		if (naval) {
			if ((c == '~' || c == 'S') && tradeLanes.isOnTradeLane(x, y, thisContinent)) {
//				System.out.println(x+ "."+y);
				return 2;
			}
			int minLift = tradeLanes.getMinLift(c);
			int maxLift = tradeLanes.getMaxLift(c);
//			System.out.println(c + " m " + minLift + " x " + maxLift);
			if (lift < minLift) {
				return 10000;
			}
			if (lift < maxLift) {
				return 30;
			}
			return 10;
		}
		return calcWeight(c);
	}

	/**
	 * Returns the movement cost for terrain type c.
	 * @param c
	 * @return Movement cost.
	 */
	
	private short calcWeight(char c) {
		short w = costs[c];
		if (w < 0) return 10000;
		return w;
	}

	/**
	 * Returns PlaneLocation neighbors for the Location inside the given TrueNode.
	 * Returns the PlaneLocations wrapped inside TrueNodes. The cost of movement
	 * to the new node and the value of the heuristic function at the neighboring locations
	 * are filled in the TrueNode.
	 * 
	 * @param node
	 * @param planeEnd
	 * @param naval
	 * @param lift
	 * @return List of TrueNodes containing PlaneLocations.
	 */
	
	List<TrueNode> getPlaneLocationNeighbors(TrueNode node, PlaneLocation planeEnd, boolean naval, int lift) {
		ArrayList<TrueNode> retVal = new ArrayList<>(20);
		TrueNode newNode;
		
		PlaneLocation loc = (PlaneLocation) node.getLoc();
		if (loc.getContinent() != planeEnd.getContinent()) { return retVal; }
		
		int x = loc.getX(), y = loc.getY();
		
		for (int i=0; i < 8; i++) {

			int newx=0, newy=0;
			switch (i) {
				case 0: newx = x - 1; newy = y - 1; break;
				case 1: newx = x; newy = y - 1; break;
				case 2: newx = x + 1; newy = y - 1; break;
				case 3: newx = x - 1; newy = y; break;
				case 4: newx = x + 1; newy = y; break;
				case 5: newx = x - 1; newy = y + 1; break;
				case 6: newx = x; newy = y + 1; break;
				case 7: newx = x + 1; newy = y + 1; break;
			}
			int newCost = getCost(newx, newy, naval, lift);

			if (newx >= 0 && newx < sizex && 
					newy >= 0 && newy < sizey) {

				if (naval) {
					// Prevent naval movement from water squares to bridges (you'll end up under the bridge).
					if (data[newx][newy] == '=' && (
							data[x][y] == 'r' ||
							data[x][y] == 'R' ||
							data[x][y] == '~' ||
							data[x][y] == 'l' ||
							data[x][y] == 'S') ) {
						newCost = 10000;
					}
				}

				if (newCost != 10000) {

					PlaneLocation newLocation = new PlaneLocation(newx, newy, thisContinent);
					
					int directionChange = 0;

					// Penalize direction changes a bit.
					if (naval) {
					  if (i != node.getDirection()) directionChange = 50; else directionChange = 0;
					}
					newNode = new TrueNode(newLocation, node, 
							node.getCost() + newCost + directionChange,
							approx(newLocation, planeEnd, naval));

					retVal.add(newNode);

				}
			}
		}
		return retVal;
	}

	/**
	 * Compares a small charArray to the map and returns the first coordinates where
	 * the charArray equals part of the map.  
	 * @param charArray
	 * @return int[2] containing x and y, or -1 and -1 if there was no match.
	 */
	
	int[] findOnMap(char[][] charArray) {
		int[] result = new int[2];
/*
		char[][] rollingArray = new char[charArray.length][charArray[0].length];
		for (int j=0; j<charArray.length; j++) {
			for (int i=0; i<charArray[0].length; i++) {
				rollingArray[j][i] = a.data[j][i];
			}
		}
		*/
//		int patternHash = initialHash(charArray);
//		int maskHash = initialHash(rollingArray);
//		bp.error(patternHash);
//		bp.error(maskHash);

		boolean failed = false;
		for (int sy=0; sy<sizey - charArray.length + 1; sy++) {
			for (int sx=0; sx<sizex - charArray[0].length + 1; sx++) {
				failed = false;
				for (int py=0; py<charArray.length && !failed; py++) {
					for (int px=0; px<charArray[0].length && !failed; px++) {
						if (charArray[py][px] != 0) {
							if (charArray[py][px] != data[sx+px][sy+py]) {
								failed = true;
							}
						}
					}
				}
				if (!failed) {
					if (result[0] == 0 && result[1] == 0) {
						result[0] = sx;
						result[1] = sy;

						//bp.error("Found! " +sx+ " "+sy);
					} else {
						result[0] = -1;
						result[1] = -1;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns an approximate cost from l1 to l2.
	 * The distance is multiplied by 9 so that the result is the approximately
	 * fastest travel time on roads.
	 * 
	 * @param l1 
	 * @param l2 
	 * @param naval
	 * @return Approximate cost from l1 to l2.
	 */
	
	static int approx(PlaneLocation l1, PlaneLocation l2, boolean naval) {
		int diff = getDiff(l1.getX(), l1.getY(), l2.getX(), l2.getY());
		if (naval) {
			return diff * 2;
		}
		return diff * 9;
	}

	/**
	 * Returns the shortest length between coordinates s, d, when the
	 * only allowed movements are the 8 primary directions.
	 * 
	 * @param sx
	 * @param sy
	 * @param dx
	 * @param dy
	 * @return Primary direction distance.
	 */
	
	static private int getDiff(int sx, int sy, int dx, int dy) {
		int diff1 = Math.abs(dx - sx), diff2 = Math.abs(dy - sy);
		if (diff1 > diff2) return diff1;
		return diff2;
	}

	
	
	/* setRoute: Merkitsee löydetyn reitin karttaan. Kun reitti on löydetty,
	   käydyt solmut voidaan merkitä setRoute-metodilla, jolloin niille
	   asetetaan hinnaksi -1. Käytännössä tällä on vähän merkitystä, koska
	   solmujen välisiä hyppyjä ei tallenneta minnekään, mutta visualisoinnin
	   kannalta on kätevää nähdä reitti.
		 */
	/*
		void setRoute(Location l, short val, int cont) {
			if (l instanceof PlaneLocation) {
				 PlaneLocation pl = (PlaneLocation) l;
				 if (pl.getX() < 0 || pl.getY() < 0 || pl.getX() >= sizex[cont] || pl.getY() >= sizey[cont]) return;
//				 checkLimits(pl.x, pl.y);

//				 plane[cont][pl.x][pl.y].baseCost = (short)-val;
				 data[cont][pl.getX()][pl.getY()] = (char) val;
			}
		}   
	*/
		
		
		
		/* print: Tulostetaan kartta niin, että (1) jos ruudun hinta on yli 10,
	     ruudussa on "*", (2) jos se on alle 10, ruudussa on "." ja (3) jos
	     se on merkitty reitiksi setRoute-metodilla, ruudussa on "-".
		 */

	/*
		void print(int cont) throws IOException {
			BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\My Documents\\path\\laenor.result"));
			int i, j;
			for (j=0; j<sizey[cont]; j++) {
				for (i=0; i<sizex[cont]; i++) {
					if (data[cont][i][j] != 0) {
						int cost = data[cont][i][j];
						if (cost < 20) { writer.write("*"); }//writer.write(""+(-data)); }
						else { writer.write(data[cont][i][j]); }
					} else {
						writer.write("X");
					}
				}
				writer.write("\n");
			}
			writer.close();
		}
	*/

		/*
		private void preCalculateCosts(int cont) {
			int currx, curry, radius, currCost;
			boolean cheaperHit;
			//sizey = 11; sizex = 11;
			bp.error("Precalculating...");
			for (curry=0; curry<sizey[cont]; curry++) {
				if (curry % 50 == 0) { bp.error(""+curry); }
				for (currx=0; currx<sizex[cont]; currx++) {
					currCost = getCost(currx, curry, cont);
					//plane[cont][currx][curry].baseCost;
					currCost = PRECALC_TRESHOLD;
					cheaperHit = false;
					for (radius=1; radius<50 & !cheaperHit; radius++) {
						if (!cheaperHit) {
							cheaperHit = scanLine(currx - radius, curry - radius, currx + radius, curry - radius, 1, 0, currCost, cont);
						}
						if (!cheaperHit) {
							cheaperHit = scanLine(currx + radius, curry - radius, currx + radius, curry + radius, 0, 1, currCost, cont);
						}
						if (!cheaperHit) {
							cheaperHit = scanLine(currx + radius, curry + radius, currx - radius, curry + radius, -1, 0, currCost, cont);
						}
						if (!cheaperHit) {
							cheaperHit = scanLine(currx - radius, curry + radius, currx - radius, curry - radius, 0, -1, currCost, cont);
						}
					}
//					plane[cont][currx][curry].radius = radius - 1;
					if (curry % 10 == 0 && currx == 400) {
						bp.error("x " + currx + " y " + curry + " r:" + radius);
					}
				}			
			}
		}

		private boolean scanLine(int startx, int starty, int endx, int endy, int modx, int mody, int currCost, int cont) {
			int scanx, scany;
			//   \ ylhäältä oikealle
			scany = starty;
			boolean cheaperHit = false;
			for (scanx = startx; (scanx != endx || scany != endy) && !cheaperHit; scanx += modx,scany += mody) {
				if (scanx >= 0 && scany >= 0 && scanx < sizex[cont] && scany < sizey[cont] && data[cont][scanx][scany] != 0) {
					//if (plane[cont][scanx][scany].baseCost < currCost) {
					if (getCost(cont,scanx,scany) < currCost) {
						cheaperHit = true;
					}
				}
				//bp.error(" x " + scanx + " y " + scany + " c "+cheaperHit);
			}
			return cheaperHit;
		}
	*/


}
