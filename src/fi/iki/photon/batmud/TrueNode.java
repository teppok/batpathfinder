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

/**
 * A wrapper class for Location, logically a subclass of Location, 
 * or both PlaneLocation and NameLocation.
 * 
 * Contains information relevant to the A* algorithm, such as the 
 * parent node when finding a route, the cost from start to here and
 * the heuristic cost from start to end through this node.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class TrueNode implements Comparable<TrueNode> {

	private final TrueNode parent;
	private final int heuristic;
	private final int cost;
	private final Location loc;

	/**
	 * A basic constructor. 
	 * @param l
	 * @param p
	 * @param c
	 * @param h
	 */
	
	TrueNode(Location l, TrueNode p, int c, int h) {
		this.loc = l;
		this.parent = p;
		this.cost = c;
		this.heuristic = c + h;
	}

	/**
	 * A basic getter for cost.
	 * @return cost
	 */
	
	int getCost() { 
		return cost; 
	}
	
	/**
	 * A basic getter for parent.
	 * @return parent
	 */
	TrueNode getParent() {
		return parent;
	}
	
	/**
	 * A basic getter for loc.
	 * @return loc
	 */
	Location getLoc() {
		return loc;
	}
	
	/**
	 * A basic getter for PlaneLocation.
	 * @return planelocation
	 */
	PlaneLocation getPlaneLocation() {
		return loc.getPlaneLocation();
	}
	
	/**
	 * Returns the key value of this node.
	 * @return key value, which is loc
	 */
	public Location getKey() {
		return loc;
	}

	/**
	 * Compares the node to its parent. If both are PlaneLocation nodes,
	 * returns a value representing the movement direction from the parent to this node.
	 * The values are:
	   <pre>
	   0 1 2
	   3   4
	   5 6 7
	   </pre> 
	 * And -1 if there's a problem, for example one of the locations is not PlaneLocation.
	 * @return direction
	 */
	
	public int getDirection() {
		if (parent == null) return -1;
		if (!(parent.loc instanceof PlaneLocation) || ! (loc instanceof PlaneLocation)) return -1;
		int oldx = ((PlaneLocation) parent.loc).getX();
		int newx = ((PlaneLocation) loc).getX();
		int oldy = ((PlaneLocation) parent.loc).getY();
		int newy = ((PlaneLocation) loc).getY();
		int dir = -1;
		if (newx == oldx-1 && newy == oldy-1) { dir = 0; }
		if (newx == oldx && newy == oldy - 1) { dir = 1; }
		if (newx == oldx+1 && newy == oldy-1) { dir = 2; }
		if (newx == oldx-1 && newy == oldy) { dir = 3; }
		if (newx == oldx+1 && newy == oldy) { dir = 4; }
		if (newx == oldx-1 && newy == oldy + 1) { dir = 5; }
		if (newx == oldx && newy == oldy + 1) { dir = 6; }
		if (newx == oldx+1 && newy == oldy + 1) { dir = 7; }
		return dir;
	}

	
//	void setPathFromParent(String s) {
//		this.pathFromParent = s;
//	}

	@Override
	public String toString() { return this.loc.toString() + " " + this.heuristic + " " + this.cost; }

	/**
	 * Gets a hash code for the location to be used in the hashmaps.
	 */

	@Override
	public int hashCode() {
		return this.loc.hashCode();
	}

	/**
	 * Compares this node to the supplied node. Returns -1 if this node's heuristic value is smaller than the other etc,
	 * 1 if larger and 0 if equal.
	 */

	@Override
	public int compareTo(TrueNode n) {
		if (this.heuristic < n.heuristic) return -1;
		if (this.heuristic == n.heuristic) return 0;
		return 1;
	}
}

