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
 * Represents a x,y location on a map on some continent.
 * Lots of these objects are created during the run of A* algorithm, so
 * we use as small data types as we can.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class PlaneLocation implements Location {
	private final short x, y; // x and y vary from 0 to about 1000
	private final byte continent; // continent varies from -1 to 4.
	
	/**
	 * A standard constructor.
	 * @param lx
	 * @param ly
	 * @param cont
	 */
	
	PlaneLocation(int lx, int ly, int cont) {
		x = (short) lx; y = (short) ly; continent = (byte) cont;
	}

	/**
	 * Implements the interface for returning the approximate location, which in this case is exact.
	 */
	
	@Override
	public PlaneLocation getPlaneLocation() {
		return this;
	}
	
	/**
	 * Basic getter for continent.
	 */
	
	@Override
	public byte getContinent() { return continent; }
	/**
	 * Basic getter for x.
	 * @return x
	 */
	short getX() { return x; }
	/**
	 * Basic getter for y.
	 * @return y
	 */
	short getY() { return y; }

	/*
	void setContinent(byte c) {
		continent = c;
	}
	void setX(short newX) {
		x = newX;
	}
	void setY(short newY) {
		y = newY;
	}
	*/
	
	@Override
	public boolean isReachable(boolean nav) {
		return true;
	}

	@Override
	public String toString() { return "L "+continent + " "+x+" "+y; }

	@Override
	public boolean equals(Object l) {
		if (l instanceof PlaneLocation) {
			if (((PlaneLocation) l).x == x && ((PlaneLocation) l).y == y && ((PlaneLocation) l).continent == continent) return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return continent*851*851+y*851+x;
	}
	
	/** Given a string representing a PlaneLocation, parse it and return the result
	 * as a Location. If parsing fails for any reason, return null.
	 * @param locStr
	 * @return Parsed location, or null if failed.
	 */
	
	static PlaneLocation parseLocation(String locStr) {
		if (locStr.startsWith("L ")) {
			String[] parts = locStr.split(" ");
			if (parts.length != 4) {
				return null;
			}
			int cont = Integer.parseInt(parts[1]);
			int x = Integer.parseInt(parts[2]);
			int y = Integer.parseInt(parts[3]);
			if (x < 0 || x >= 850 || y < 0 || y >= 850 || cont < 0 || cont >= 5) {
				return null;
			}
			return new PlaneLocation(x, y, cont);
		}
		return null;
	}

}
