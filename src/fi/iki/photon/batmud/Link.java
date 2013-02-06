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
 * A class representing a link from a NameLocation node to another NameLocation node.
 * If naval is true, this is a naval link, otherwise it's a walking link. linkString is
 * the movement string connecting these nodes and cost is the cost of moving this link.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class Link {
	private final NameLocation dest;
	private final boolean naval;
	private final String linkString;
	private final int cost;

	/**
	 * A standard constructor with a forced cost.
	 * 
	 * @param destination
	 * @param linkString
	 * @param cost
	 * @param naval
	 * @throws BPFException if destination or linkstring is null.
	 */
	
	Link(NameLocation destination, String linkString, int cost, boolean naval) throws BPFException {
		if (linkString == null || destination == null) throw new BPFException("Bug in Link");
		this.dest = destination; this.linkString = linkString; this.cost = cost;
		this.naval = naval;
	}

	/**
	 * A standard costructor where cost is calculated from the
	 * length of the linkstring.
	 * 
	 * @param d
	 * @param l
	 * @param n
	 * @throws BPFException if 
	 */
	
	Link(NameLocation d, String l, boolean n) throws BPFException {
		this(d, l, getLength(l), n);
	}

	/**
	 * Basic getter for naval.
	 * @return naval
	 */
	
	boolean getNaval() {
		return naval;
	}

	/**
	 * Basic getter for linkString.
	 * @return linkString
	 */

	String getLinkString() {
		return linkString;
	}
	/**
	 * Basic getter for cost.
	 * @return cost
	 */

	int getCost() {
		return cost;
	}
	
	/**
	 * Basic getter for dest.
	 * @return dest
	 */

	NameLocation getDest() { return dest; }

	@Override
	public String toString() {
		return dest.toString();
	}
	
	/**
	 * Calculates the command length of the space delimited command string.
	 * If it contains integers, these mean that the next command is repeated
	 * as many times as specified.
	 * 
	 * @param p
	 * @return command length of p
	 */
	
	static private int getLength(String p) {
		if ("".equals(p)) return 0;
		String[] parts = p.split(" ");
//		boolean doParse = true;
		int i = 0;
		int length = 0;
		for (i = 0; i < parts.length; ) {
			if (parts[i].charAt(0) >= '0' && parts[i].charAt(0) <= '9') {
				length += Integer.parseInt(parts[i]);
				i += 2;
			} else {
				length++;
				i++;
			}
		}
		return length;
	}
	
}
