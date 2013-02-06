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

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a named location. The location must be approximated by a PlaneLocation,
 * but the approximation doesn't have to be exact. If adjacent is set, then the PlaneLocation
 * is an immediate neighbor of this NameLocation (then AreaContainer.names should also contain
 * a backwards link to this NameLocation). 
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class NameLocation implements Location {
	private final String name;
	private final List<Link> neighbors = new ArrayList<>();
	private final PlaneLocation approxLocation;
	private final boolean isAdjacent;

	/**
	 * A standard constructor. approx parameter must be null or an exception is thrown.
	 * If adjacent is true, then approx is the same location as this NameLocation (ie. it can
	 * be reached from the NameLocation without any movement).
	 * 
	 * @param name Name of this location
	 * @param approx
	 * @param isAdjacent
	 * @throws BPFException
	 */
	NameLocation(String name, PlaneLocation approx, boolean isAdjacent) throws BPFException {
		if (name == null || "".equals(name)) throw new BPFException("Bug: NameLocation: null or empty name.");
		if (approx == null) throw new BPFException("Bug: NameLocation: Approximate location null.");
		this.name = name;
		approxLocation = approx;
		this.isAdjacent = isAdjacent;
	}

	/** Implements interface getter for continent. */

	@Override
	public byte getContinent() { return approxLocation.getContinent(); }

	/** Implements interface getter for approximate plane location. */ 

	@Override
	public PlaneLocation getPlaneLocation() {
		return approxLocation;
	}
	
	/**
	 * Basic getter for isAdjacent.
	 * @return isAdjacent
	 */
	
	boolean isAdjacent() {
		return isAdjacent;
	}
	
	/**
	 * Basic equality comparison by name.
	 */

	@Override
	public boolean equals(Object l) {
		if (l instanceof NameLocation) {
			if (((NameLocation) l).name.equals(name)) return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Returns true if this location is immediately reachable by the movement mode nav.
	 * Adjacent nodes can always be accessed, but if it's inside the NameLocation network,
	 * we can immediately say it's not reachable if there are no links to it in the queried mode.
	 */
	@Override
	public boolean isReachable(boolean nav) {
		if (isAdjacent) return true;
		for (Link l : neighbors) {
			if (l.getNaval() == nav) return true;
		}
		return false;
	}
	
	@Override
	public String toString() { return name; }

	/**
	 * Add a neighbor link.
	 * @param l
	 */
	
	void addNeighbor(Link l) {
		neighbors.add(l);
	}

	/**
	 * Return the list of neighbors.
	 * @return neighbors
	 */
	List<Link> getNeighbors() {
		return neighbors;
	}
}
