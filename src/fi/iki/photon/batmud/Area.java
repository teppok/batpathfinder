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
 * @author Teppo Kankaanp‰‰
 *
 */

class Area {
	private final Tradelanes tradeLanes;
	private final Costs costs;
	
	private final char[][] data;
	private final int sizex, sizey;


	/**
	 * Basic initialization.
	 * 
	 * @param tl Tradelanes object for calculating tradelanes.
	 * @param costs Costs object for calculating costs.
	 * @param sx Width of the map.
	 * @param sy Height of the map.
	 * @param fileName File name containing the map data as a character array.
	 * @throws IOException When file loading fails.
	 */
	Area(Tradelanes tl, Costs costs, int sx, int sy, String fileName) throws IOException {
		
		if (tl == null) throw new IOException("Null tradelanes");
		if (costs == null) throw new IOException("Null costs");
		if (fileName == null) throw new IOException("Null filename");
		if (sx < 0 || sy < 0) throw new IOException("Bad parameters");
		
		tradeLanes = tl;
		sizex = sx;
		sizey = sy;
		data = new char[sizex][sizey];
		this.costs = costs;

		loadContinent(fileName);
	}

	/**
	 * Returns the terrain character at location x, y.
	 * @param x
	 * @param y
	 * @return Terrain character
	 * @throws BPFException If x,y out of bounds.
	 */
	
	char getData(int x, int y) throws BPFException {
		if (! isValidLocation(x, y)) throw new BPFException("Out of map bounds");
		return data[x][y];
	}

	/**
	 * Returns true if the parameter is a location inside the boundaries of the map.
	 * @param x 
	 * @param y 
	 * @return true if l is inside the boundaries of the map.
	 */
	boolean isValidLocation(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return false;
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
	 * Returns the cost of movement at location x, y. If naval is true, returns
	 * naval costs, otherwise walking costs. Naval costs classes are
	 * 2 for tradelanes, 30 for slow movement, 10 for fast movement.
	 * @param x
	 * @param y
	 * @param naval
	 * @param lift
	 * @return Cost of movement or 10000 if impenetrable.
	 * @throws BPFException If x, y out of bounds.
	 */

	int getCost(int x, int y, boolean naval, int lift) throws BPFException {
		char c = getData(x, y);
		if (c == 'c') return 10000;
		if (c == 'C') return 10000;
		if (naval) {
			int minLift = costs.getMinLift(c);
			int maxLift = costs.getMaxLift(c);
			if (lift < minLift) {
				return 10000;
			}
			if (tradeLanes.isOnTradeLane(x, y)) {
				return 2;
			}
//			System.out.println(c + " m " + minLift + " x " + maxLift);
			if (lift < maxLift) {
				return 30;
			}
			return 10;
		}
		return costs.calcWeight(c);
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
	 * @throws BPFException if the algorithm bugs.
	 */
	
	List<TrueNode> getPlaneLocationNeighbors(TrueNode node, PlaneLocation planeEnd, boolean naval, int lift) throws BPFException {
		ArrayList<TrueNode> retVal = new ArrayList<>(20);
		TrueNode newNode;
		
		PlaneLocation loc = (PlaneLocation) node.getLoc();
		int continent = loc.getContinent();
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
			
			if (isValidLocation(newx, newy)) {
				int newCost = getCost(newx, newy, naval, lift);
				
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

					PlaneLocation newLocation = new PlaneLocation(newx, newy, continent);
					
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

		result[0] = result[1] = -1;

		boolean failed = false;
		for (int sy=0; sy<sizey - charArray.length + 1 && result[0] < 0; sy++) {
			for (int sx=0; sx<sizex - charArray[0].length + 1 && result[0] < 0; sx++) {
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
					result[0] = sx;
					result[1] = sy;
				}
			}
		}
		return result;
	}

	/**
	 * Returns an approximate cost from l1 to l2.
	 * The distance is multiplied by 9 so that the result is the approximately
	 * fastest travel time on roads or semi-tradelane.
	 * 
	 * @param l1 
	 * @param l2 
	 * @param naval
	 * @return Approximate cost from l1 to l2.
	 */
	
	static int approx(PlaneLocation l1, PlaneLocation l2, boolean naval) {
		int diff = getDiff(l1.getX(), l1.getY(), l2.getX(), l2.getY());
		if (naval) {
			// tradelane would be 2
			return diff * 5;
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
	
	static int getDiff(int sx, int sy, int dx, int dy) {
		int diff1 = Math.abs(dx - sx), diff2 = Math.abs(dy - sy);
		if (diff1 > diff2) return diff1;
		return diff2;
	}

}
