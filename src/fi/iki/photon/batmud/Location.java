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
 * Interface for any location in the game, be it on the map or in cities etc.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public interface Location {
	/**
	 * Returns the continent of this Location.
	 * @return continent
	 */
	public byte getContinent();
	
	/**
	 * Returns true of this Location is reachable with the current nav setting.
	 * @param nav
	 * @return is this Location reachable.
	 */
	public boolean isReachable(boolean nav);

	/** Returns an exact or approximate PlaneLocation for this Location. Always non-null.
	 * 
	 * @return Approximate PlaneLocation
	 */
	public PlaneLocation getPlaneLocation();
	
}
