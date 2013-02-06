/* 
    Copyright 2008, 2009, 2010, 2011 Teppo Kankaanpaa teppo.kankaanpaa@iki.fi

	(except GridLayout2.java)

	This file is part of BatPathFinder.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.iki.photon.batmud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

/**
 * Main interfacing class for the program logic containing all the
 * needed data for calculations.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class AreaContainer {


	private BatPathFinderThread[] solvers;

	/**
	 * Continent: Laenor.
	 */
	public static final int CONT_LAENOR=0;
	/**
	 * Continent: Desolathya.
	 */
	public static final int CONT_DESO=1;
	/**
	 * Continent: Rothikgen.
	 */
	public static final int CONT_ROTH=2;
	/**
	 * Continent: Lucentium.
	 */
	public static final int CONT_LUC=3;
	/**
	 * Continent: Furnachia.
	 */
	public static final int CONT_FURN=4;
	
	// Mapping from strings to the NameLocations they represent.
	private final HashMap<String, NameLocation> cityNodes;
	
	// Mapping from PlaneLocations to their adjacent NameLocations.
	private final HashMap<PlaneLocation, List<NameLocation>> names;
	private String baseDir;

	private final Area[] areas;
	private final Tradelanes tradeLanes;
	
//	String[][] continentChange;
	
	private final NameLocation exitNodes[][];

	private final String[][] esirisContinentChange;
	
	private final NameLocation esirisExitNodes[];

	/**
	 * Basic constructor.
	 * @param baseDir Base directory where the data files are.
	 * @throws IOException When read fails.
	 * @throws BPFException When the data is logically invalid.
	 */
	
	public AreaContainer(String baseDir) throws IOException, BPFException {
		this.baseDir = baseDir;
		exitNodes = new NameLocation[5][5];
		esirisContinentChange = new String[5][5];
		esirisExitNodes = new NameLocation[5];
		names = new HashMap<>();
		areas = new Area[5];
		cityNodes = new HashMap<>();
		tradeLanes = new Tradelanes(baseDir + "/tradelane.txt", baseDir + "/costs.ship");
		
		load();
		
	}

	/**
	 * Loads the continents and tradelanes into memory.
	 * 
	 * @throws IOException
	 * @throws BPFException
	 */
	
	private void load() throws IOException, BPFException {
			
		load("laenor", CONT_LAENOR, 827, 781);
		load("roth", CONT_ROTH, 480, 480);
		load("furn", CONT_FURN, 440, 480);
		load("deso", CONT_DESO, 540, 530);
		load("luc", CONT_LUC, 700, 500);
		loadExtraEdges(baseDir + "/laenor.shipnodes", true);
		loadExtraEdges(baseDir + "/laenor.shipnodes.private", true);		
		
//		System.out.println(isOnTradeLane(531,95,CONT_DESO));
//		System.out.println(isOnTradeLane(531,100,CONT_DESO));
		
//		PlaneLocation pl = new PlaneLocation(433, 446, 1);
//		bp.error(isOnTradeLane(pl));
//		pl = new PlaneLocation(250, 308, 0);
//		bp.error(isOnTradeLane(pl));
		initValues();
			
	}

	/**
	 * Loads the named locations from fileName for continent cont.
	 * If fix is true, subtract x,y by 1 (required for some Ggr's data)
	 * 
	 * @param fileName
	 * @param cont
	 * @param fix
	 * @throws IOException
	 * @throws BPFException
	 */
	
	private void loadLocations(String fileName, int cont, boolean fix) throws IOException, BPFException {
			File f = new File(fileName);
			if (!f.exists()) return;
	
			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
				boolean readMore = true;
				while (readMore) {
					String line = reader.readLine();
					if (line == null) { readMore = false; } else {
						line = line.replaceAll("@", "");
						if (!line.equals("") && !line.startsWith("#")) {
							String parts[] = line.split(";");
							int locX = Integer.parseInt(parts[0].trim());
							int locY = Integer.parseInt(parts[1].trim());
							// Have to fix locations from Ggr repository, but not from local locations.
							if (fix) {
								locX = locX - 1;
								locY = locY - 1;
							}
							String flags = parts[2].trim();
		//					bp.error(locX + "--" + locY);
							if (flags.contains("c")) {
		//						length() >= 2 && ( flags.(1) == 'c' ||
		//					}
		//							flags.charAt(1) == 'C' )) {
								// clear city locations
		//						data[cont][locX][locY] = 0;
		//						if (flags.charAt(1) == 'c') { data[cont][locX][locY] = 'c'; }
		//						if (flags.charAt(1) == 'C') { data[cont][locX][locY] = 'C'; }
		//						data[cont][locX][locY] = 'c';
							} else if (flags.contains("C")) {
		//						data[cont][locX][locY] = 'C';
							} else {
		/*
								if (flags.contains("F")) {
										data[cont][locX][locY] = '?';
								}
								if (flags.contains("G")) {
									data[cont][locX][locY] = '?';
								}
								if (flags.contains("S")) {
									data[cont][locX][locY] = '%';
								}
								if (flags.contains("?")) {
									data[cont][locX][locY] = '?';
								}
								if (flags.contains("%")) {
									data[cont][locX][locY] = '%';
								}
		*/
								
								if (areas[cont].getData(locX, locY) == 0) {
								//	if (plane[cont][locX-4][locY] != null) System.out.print(plane[cont][locX-4][locY].data);
								//	if (plane[cont][locX-3][locY] != null) System.out.print(plane[cont][locX-3][locY].data);
								//	if (plane[cont][locX-2][locY] != null) System.out.print(plane[cont][locX-2][locY].data);
								//	if (plane[cont][locX-1][locY] != null) bp.error(plane[cont][locX-1][locY].data);
								//	if (plane[cont][locX+1][locY] != null) bp.error(plane[cont][locX+1][locY].data);
									throw new IOException("Strange coordinates " + locX + " " + locY);
								}
								
								String nameTmp = parts[3].trim();
								String name = nameTmp;
								if (nameTmp.contains("|")) {
									String nameTmp2[] = nameTmp.split("\\|");
									name = nameTmp2[0];
								}
								if ("".equals(name) || " ".equals(name)) { System.err.println("x"+name+"x"); }
								addLocation(locX, locY, cont, name);
								if (name.contains(" ")) {
									addLocation(locX, locY, cont, name.replace(" ",""));
									addLocation(locX, locY, cont, name.replace(" ","_"));
								}
							}
						}	
					}
				}
			}
		}

	/**
	 * Add a named location to the list of all NameLocations and mark it as
	 * adjacent to the location locx, locy.
	 * 
	 * @param locX
	 * @param locY
	 * @param cont
	 * @param name
	 * @throws BPFException
	 */
	
	private void addLocation(int locX, int locY, int cont, String name) throws BPFException {
			NameLocation nl_orig = cityNodes.get(name.toLowerCase());
			if (nl_orig != null) {
				throw new BPFException("Location " + name + " already exists.");
			}
			
			PlaneLocation pl = areas[cont].getPlaneLocation(locX, locY);
			NameLocation nl = new NameLocation(name, pl, true);
			
			List<NameLocation> namesList = names.get(pl);
			if (namesList == null) {
				namesList = new ArrayList<>(2);
				names.put(pl, namesList);
			}
			namesList.add(nl);
	//		plane[cont][locX][locY].addName(nl);
	/*
			Link l = new Link(pl, "", false);
			nl.addNeighbor(l);
			l = new Link(pl, "", true);
			nl.addNeighbor(l);
	*/
			cityNodes.put(name.toLowerCase(), nl);
	}

	/**
	 * Load a list of extra nodes and edges that will be used to construct
	 * NameLocation network. 
	 * Lines beginning with ! describe a new location, and some close by Location that's
	 * either PlaneLocation or adjacent to a PlaneLocation. This will be used for the
	 * approximation function.
	 * Other lines describe a link between already described NameLocations.
	 * 
	 * @param fileName
	 * @param nav
	 * @throws IOException
	 * @throws BPFException
	 */
	
	private void loadExtraEdges(String fileName, boolean nav) throws IOException, BPFException {
			File f = new File(fileName);
			if (!f.exists()) return;
	
			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	
				boolean readMore = true;
				while (readMore) {
					String line = reader.readLine();
					if (line == null) { readMore = false; } else {
						if (!line.equals("") && !line.startsWith("#")) {
							if (line.startsWith("!")) {
								String parts[] = line.split(" ");
								NameLocation node = cityNodes.get(parts[1]);
								NameLocation planeNode = cityNodes.get(parts[2]);
								
		//						int newCont = cont;
		//						if (parts.length > 2 && parts[3] != null) {
		//							newCont = Integer.parseInt(parts[3]);
		//						}
		//						bp.error(parts[1] + " " + parts[2]);
								if (node != null) {
									throw new BPFException("Error, redefined node " + parts[1] + ", " + node);
								}
								if (planeNode == null) {
									throw new BPFException("Error, unknown plane node " + parts[2]);
								}
								if (planeNode.getPlaneLocation() == null) {
									throw new BPFException("Error, supposed plane node not on a plane, " + node + " " + planeNode);
								}
								
								node = new NameLocation(parts[1], planeNode.getPlaneLocation(), false);
								cityNodes.put(parts[1].toLowerCase(), node);
							} else {
								String leftright[] = line.split("::");
								String left[] = leftright[0].split(" ");
		//						bp.error(left[0] + "--" + left[1]);
		//						if (leftright.length > 1) { bp.error("--"+leftright[1]); }
								NameLocation startCity = cityNodes.get(left[0]);
								NameLocation endCity = cityNodes.get(left[1]);
								if (startCity == null) {
									throw new BPFException("Malformed input, " + left[0] + " not found.");
								}
								if (endCity == null) {
									throw new BPFException("Malformed input, " + left[1] + " not found.");
								}
								if (leftright.length == 1) {
									startCity.addNeighbor(new Link(endCity, "", 5, nav));
								}
								if (leftright.length == 2) {
	//								System.out.println("Added link from " + startCity + " to " + endCity + " " + leftright[1]);
									startCity.addNeighbor(new Link(endCity, leftright[1], nav));
								}
							}
						}	
					}
				}
			}
		}

	/**
	 * Initializes the exitNodes, esiris exit nodes and content change strings for esiris.
	 */
	
	private void initValues() {
	//		continentChange = new String[5][5];
	
			exitNodes[AreaContainer.CONT_LAENOR][AreaContainer.CONT_ROTH] = getNameLocation("laenor10");
			exitNodes[AreaContainer.CONT_LAENOR][AreaContainer.CONT_DESO] = getNameLocation("laenor9");
			exitNodes[AreaContainer.CONT_LAENOR][AreaContainer.CONT_LUC] = getNameLocation("laenor9");
			exitNodes[AreaContainer.CONT_LAENOR][AreaContainer.CONT_FURN] = getNameLocation("laenor6");
			exitNodes[AreaContainer.CONT_ROTH][AreaContainer.CONT_LAENOR] = getNameLocation("rothikgen1");
			exitNodes[AreaContainer.CONT_ROTH][AreaContainer.CONT_DESO] = getNameLocation("rothikgen1");
			exitNodes[AreaContainer.CONT_ROTH][AreaContainer.CONT_LUC] = getNameLocation("rothikgen1");
			exitNodes[AreaContainer.CONT_ROTH][AreaContainer.CONT_FURN] = getNameLocation("rothikgen1");
			exitNodes[AreaContainer.CONT_FURN][AreaContainer.CONT_LAENOR] = getNameLocation("furnachia1");
			exitNodes[AreaContainer.CONT_FURN][AreaContainer.CONT_DESO] = getNameLocation("furnachia1");
			exitNodes[AreaContainer.CONT_FURN][AreaContainer.CONT_ROTH] = getNameLocation("furnachia1");
			exitNodes[AreaContainer.CONT_FURN][AreaContainer.CONT_LUC] = getNameLocation("furnachia1");
			exitNodes[AreaContainer.CONT_LUC][AreaContainer.CONT_LAENOR] = getNameLocation("lucentium1");
			exitNodes[AreaContainer.CONT_LUC][AreaContainer.CONT_ROTH] = getNameLocation("lucentium1");
			exitNodes[AreaContainer.CONT_LUC][AreaContainer.CONT_DESO] = getNameLocation("lucentium1");
			exitNodes[AreaContainer.CONT_LUC][AreaContainer.CONT_FURN] = getNameLocation("lucentium1");
			exitNodes[AreaContainer.CONT_DESO][AreaContainer.CONT_LAENOR] = getNameLocation("desolathya2");
			exitNodes[AreaContainer.CONT_DESO][AreaContainer.CONT_ROTH] = getNameLocation("desolathya1");
			exitNodes[AreaContainer.CONT_DESO][AreaContainer.CONT_FURN] = getNameLocation("desolathya2");
			exitNodes[AreaContainer.CONT_DESO][AreaContainer.CONT_LUC] = getNameLocation("desolathya2");
	
			esirisExitNodes[AreaContainer.CONT_LAENOR] = getNameLocation("church");
			esirisExitNodes[AreaContainer.CONT_ROTH] = getNameLocation("skeep");
			esirisExitNodes[AreaContainer.CONT_FURN] = getNameLocation("rilynttar");
			esirisExitNodes[AreaContainer.CONT_LUC] = getNameLocation("lorenchia");
			esirisExitNodes[AreaContainer.CONT_DESO] = getNameLocation("caly");
	
			esirisContinentChange[AreaContainer.CONT_LAENOR][AreaContainer.CONT_DESO] = "portal~esiris 24 w enter~portal";
			esirisContinentChange[AreaContainer.CONT_LAENOR][AreaContainer.CONT_ROTH] = "portal~esiris 15 n enter~portal";
			esirisContinentChange[AreaContainer.CONT_LAENOR][AreaContainer.CONT_LUC] = "portal~esiris 15 s enter~portal";
			esirisContinentChange[AreaContainer.CONT_LAENOR][AreaContainer.CONT_FURN] = "portal~esiris 24 e enter~portal";
			esirisContinentChange[AreaContainer.CONT_ROTH][AreaContainer.CONT_LAENOR] = "portal~esiris 15 s enter~portal";
			esirisContinentChange[AreaContainer.CONT_ROTH][AreaContainer.CONT_DESO] = "portal~esiris 15 s 24 w enter~portal";
			esirisContinentChange[AreaContainer.CONT_ROTH][AreaContainer.CONT_LUC] = "portal~esiris 15 s 15 s enter~portal";
			esirisContinentChange[AreaContainer.CONT_ROTH][AreaContainer.CONT_FURN] = "portal~esiris 15 s 24 e enter~portal";
			esirisContinentChange[AreaContainer.CONT_DESO][AreaContainer.CONT_LAENOR] = "portal~esiris 24 e enter~portal";
			esirisContinentChange[AreaContainer.CONT_DESO][AreaContainer.CONT_ROTH] = "portal~esiris 24 e 15 n enter~portal";
			esirisContinentChange[AreaContainer.CONT_DESO][AreaContainer.CONT_LUC] = "portal~esiris 24 e 15 s enter~portal";
			esirisContinentChange[AreaContainer.CONT_DESO][AreaContainer.CONT_FURN] = "portal~esiris 24 e 24 e enter~portal";
			esirisContinentChange[AreaContainer.CONT_LUC][AreaContainer.CONT_LAENOR] = "portal~esiris 15 n enter~portal";
			esirisContinentChange[AreaContainer.CONT_LUC][AreaContainer.CONT_DESO] = "portal~esiris 15 n 24 w enter~portal";
			esirisContinentChange[AreaContainer.CONT_LUC][AreaContainer.CONT_ROTH] = "portal~esiris 15 n 15 n enter~portal";
			esirisContinentChange[AreaContainer.CONT_LUC][AreaContainer.CONT_FURN] = "portal~esiris 15 n 24 e enter~portal";
			esirisContinentChange[AreaContainer.CONT_FURN][AreaContainer.CONT_LAENOR] = "portal~esiris 24 w enter~portal";
			esirisContinentChange[AreaContainer.CONT_FURN][AreaContainer.CONT_DESO] = "portal~esiris 24 w 24 w enter~portal";
			esirisContinentChange[AreaContainer.CONT_FURN][AreaContainer.CONT_ROTH] = "portal~esiris 24 w 15 n enter~portal";
			esirisContinentChange[AreaContainer.CONT_FURN][AreaContainer.CONT_LUC] = "portal~esiris 24 w 15 s enter~portal";
		}

	/**
	 * Loads a continent named "continent" into the areas array, and loads the locations and
	 * namelocation network edges from the files.
	 * 
	 * @param continent
	 * @param contNum
	 * @param sx
	 * @param sy
	 * @throws IOException
	 * @throws BPFException
	 */
	
	private void load(String continent, int contNum, int sx, int sy) throws IOException, BPFException {
		areas[contNum] = new Area(contNum, tradeLanes, sx, sy, baseDir + "/" + continent + ".map", baseDir + "/costs");
		
		loadLocations(baseDir + "/" + continent + ".loc", contNum, true);
		loadLocations(baseDir + "/" + continent + ".loc.extra", contNum, false);
		loadLocations(baseDir + "/" + continent + ".loc.private", contNum, false);
		loadExtraEdges(baseDir + "/" + continent + ".nodes", false);
		loadExtraEdges(baseDir + "/" + continent + ".nodes.private", false);
	}

	/**
	 * Given a NameLocation, returns an adjacent PlaneLocation.
	 * @param n
	 * @return PlaneLocation, or null if not adjacent to any.
	 */
/*
	private PlaneLocation getAdjacentPlaneLocation(NameLocation n) {
		if (n == null) return null;
		for (Link l : n.getNeighbors()) {
			if (l.getDest() instanceof PlaneLocation) {
				return (PlaneLocation) l.getDest();
			}
		}
		return null;
	}
	*/

	/**
	 * Given a string, returns a NameLocation with that name.
	 * @param name
	 * @return NameLocation with that name.
	 */
	
	private NameLocation getNameLocation(String name) {
		return cityNodes.get(name.toLowerCase());
	}

	/**
	 * Returns all the NameLocations.
	 * @return All the NameLocations.
	 */
	
	public Set<String> getCityNodes() {
		return cityNodes.keySet();
	}

	/**
	 * Given two continents, returns the ship exit node that
	 * is inbetween continent and continent2.
	 * @param continent
	 * @param continent2
	 * @return Ship exit node.
	 */
	
	public Location getExitnode(int continent, int continent2) {
		return exitNodes[continent][continent2];
	}

	/**
	 * Given a continent, returns the esiris exit node for that continent.
	 * @param continent
	 * @return Esiris exit node.
	 */
	
	public Location getEsirisExitnode(int continent) {
		return esirisExitNodes[continent];
	}

	/**
	 * Given two continents, returns the content change string that will
	 * walk you from esiris exit node on continent to esiris exit node
	 * on continent2.
	 * @param continent
	 * @param continent2
	 * @return Esiris continent change commands.
	 */
	
	public String getEsirisContinentChange(int continent, int continent2) {
		return esirisContinentChange[continent][continent2];
	}


	// ------------------------- Action functions ---------------------------------
	/**
	 * Given an array of startNode, exitNode, types, and naval and lift parameters,
	 * start solver threads for each row in the arrays. The solver will call
	 * solvedListener.solved() when it has finished solving the problem.
	 * 
	 * @param solvedListener
	 * @param startNode
	 * @param exitNode
	 * @param naval
	 * @param lift
	 * @param types
	 * @throws BPFException
	 */
	
	public void solve(SolvedListener solvedListener, Location[] startNode, Location[] exitNode, boolean naval, int lift, int[] types) throws BPFException {
		if (startNode == null || exitNode == null || types == null || startNode.length != exitNode.length || startNode.length != types.length) {
			throw new BPFException("Bug in solve routine.");
		}
		solvers = new BatPathFinderThread[startNode.length];
		for (int i = 0; i < startNode.length; i++) {
			solvers[i] = new BatPathFinderThread(solvedListener, this, startNode[i], exitNode[i], naval, lift, types[i]);
		}
	}

	/**
	 * Aborts the solving.
	 */
	
	public void abort() {
		if (solvers != null) {
			for (int i = 0; i < solvers.length; i++) {
				solvers[i].abort();
			}
			solvers = null;
		}
	}
	/**
	 * Finds the charArray on the map of cont.
	 * Given an array (preferably fixed width), searches for a match on
	 * the continent data and returns x, y of this match in a two-place array.
	 * @param cont
	 * @param charArray
	 * @return int[2] containing x, y of a match, or -1, -1 if no match found.
	 */
	
	public int[] findOnMap(int cont, char[][] charArray) {
		return areas[cont].findOnMap(charArray);
	}

	/**
	 * Given a string (either a descriptor of a namelocation or a parseable PlaneLocation),
	 * adds a new alias name for this string and stores it on the private data file.
	 * 
	 * @param locationString
	 * @param name
	 * @throws IOException
	 * @throws BPFException
	 */
	
	public void addLocationToFile(String locationString, String name) throws IOException, BPFException {
		
		Location l2 = getNameLocation(name);
		if (l2 != null) {
			throw new BPFException("Location " + name +" already exists.\n");
		}

		Location l = parseLocation(locationString);
		if (l == null) return;

		PlaneLocation pl = l.getPlaneLocation();
		if (pl != null) {
			addLocationToFile(pl, name);
		} else {
			throw new BPFException("Location not on outside map.");
		}
	}

	/**
	 * Stores the data in PlaneLocation l under name in the user's private location files.
	 * 
	 * @param l
	 * @param name
	 * @throws IOException
	 * @throws BPFException
	 */
	
	private void addLocationToFile(PlaneLocation l, String name) throws IOException, BPFException {
		if (l == null) return;
		int continent = l.getContinent();
		// Throws exception if already exists.
		addLocation(l.getX(), l.getY(), continent, name);		
		File f = null;
		if (continent == AreaContainer.CONT_LAENOR) { f = new File(baseDir + "/laenor.loc.private"); }
		if (continent == AreaContainer.CONT_DESO) { f = new File(baseDir + "/deso.loc.private"); }
		if (continent == AreaContainer.CONT_ROTH) { f = new File(baseDir + "/roth.loc.private"); }
		if (continent == AreaContainer.CONT_LUC) { f = new File(baseDir + "/luc.loc.private"); }
		if (continent == AreaContainer.CONT_FURN) { f = new File(baseDir + "/furn.loc.private"); }
//		bp.error(f.toString());
		if (f == null) return;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, true))) {
			writer.write(l.getX() + " ; " + l.getY() + " ; 0 ;" + name + ";;;;\n");
			writer.flush();
		}
	}


	/**
	 * Parses a string and returns a Location that matches it.
	 * @param s
	 * @return Parsed location, or null if invalid.
	 */
	
	public Location parseLocation(String s) {
		Location retVal = PlaneLocation.parseLocation(s);
		if (retVal != null) {
			if (! areas[((PlaneLocation) retVal).getContinent()].validLocation((PlaneLocation) retVal)) { return null; }
		} else {
			retVal = getNameLocation(s);
		}
		return retVal;
	}
	
	
	// ------------------------ ALGORITHM RELATED FUNCTIONS ------------------------

	/**
	 * Returns the approximate distance from l1 to l2, possibly using naval travel.
	 * @param l1
	 * @param l2
	 * @param naval
	 * @return Approximate distance.
	 */
	
	static int approx(PlaneLocation l1, PlaneLocation l2, boolean naval) {
		return Area.approx(l1, l2, naval);
	}

	/**
	 * Returns all the neighbors of a TrueNode, be it NameLocations or PlaneLocations.
	 * Location inside TrueNode is used to calculate the neighbors, and
	 * the results are wrapped inside TrueNodes that'll contain the
	 * parent node, cost data and heuristic data used with A*.
	 * planeEnd is used to calculate the heuristic data.
	 * 
	 * If the Location is a NameLocation which is adjacent to some PlaneLocation,
	 * this PlaneLocation will be one of NameLocation's neighbors.
	 * 
	 * @param node
	 * @param planeEnd
	 * @param naval
	 * @param lift
	 * @return List of TrueNode-wrapped neighbor Locations.
	 * @throws BPFException if the algorithm bugs
	 */
	
	List<TrueNode> getNeighbors(TrueNode node, PlaneLocation planeEnd, boolean naval, int lift) throws BPFException {
		if (node.getLoc() instanceof PlaneLocation) {
			List<TrueNode> a = areas[node.getLoc().getContinent()].getPlaneLocationNeighbors(node, planeEnd, naval, lift);
			a.addAll(getNLNeighborsOfPlaneLocation(node, planeEnd, naval));
			return a;
		}
		if (node.getLoc() instanceof NameLocation) {
			return getNameLocationNeighbors(node, planeEnd, naval);
		}
		return null;
	}	

	/**
	 * Given a PlaneLocation inside a TrueNode, returns all the NameLocations that are adjacent
	 * to that PlaneLocation, wrapped inside TrueNodes.
	 * @param node
	 * @param planeEnd Used to calculate the heuristic cost.
	 * @param naval
	 * @return List of TrueNode-wrapped neighbor Locations.
	 */
	
	private List<TrueNode> getNLNeighborsOfPlaneLocation(TrueNode node, PlaneLocation planeEnd, boolean naval) throws BPFException {
		ArrayList<TrueNode> retVal = new ArrayList<>(5);

		List<NameLocation> nameList = names.get(node.getLoc());
	
		if (nameList != null) {
			for (NameLocation l : nameList) {
				TrueNode t = new TrueNode(l, node, node.getCost(), approx(l.getPlaneLocation(), planeEnd, naval));
				retVal.add(t);
			}
		}
		return retVal;
	}

	/**
	 * Given a NameLocation inside a TrueNode, returns all the neighbors it
	 * has.
	 * @param n
	 * @param planeEnd Used to calculate the heuristic cost.
	 * @param naval
	 * @return List of TrueNode wrapped neighbor locations.
	 * @throws BPFException if the algorithm bugs.
	 */
	
	static private List<TrueNode> getNameLocationNeighbors(TrueNode n, PlaneLocation planeEnd, boolean naval) throws BPFException {
//		System.out.println(n + " " + planeEnd + " " + naval);
		NameLocation loc = (NameLocation) n.getLoc();
		ArrayList<TrueNode> v = new ArrayList<>();
		// First add the NameLocation neighbors.
		for (Link link : loc.getNeighbors()) {
			if (!link.getNaval() && !naval) {
				TrueNode t = new TrueNode(link.getDest(), n, n.getCost() + link.getCost(), 
						approx(link.getDest().getPlaneLocation(), planeEnd, naval));
				v.add(t);
			} else if (link.getNaval() && naval) {
				TrueNode t = new TrueNode(link.getDest(), n, n.getCost() + link.getCost(),
						approx(link.getDest().getPlaneLocation(), planeEnd, naval));
				v.add(t);
			}
		}
		// If the NameLocation is adjacent to a PlaneLocation, add this PlaneLocation too.
		if (loc.isAdjacent()) {
			v.add(new TrueNode(loc.getPlaneLocation(), n, n.getCost(), 
				approx(loc.getPlaneLocation(), planeEnd, naval)));
		}
		return v;
	}

}

