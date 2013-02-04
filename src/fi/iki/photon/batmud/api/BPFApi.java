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
package fi.iki.photon.batmud.api;

import javax.swing.JPanel;

/**
 * The basic UI API required by BatPathFinder.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public interface BPFApi {
	/**
	 * Output a string to some output.
	 * @param s
	 */
	public void output(String s);
	/**
	 * Perform a command.
	 * @param s
	 */
	public void doCommand(String s);
	/**
	 * Create a new window that displays program panels. It is assumed that
	 * a class that implements BPFApi has access to BatPathFinderUI class so that
	 * it can query it for the necessary panel objects.
	 * @param pathPanel 
	 */
	public void createWindow(JPanel pathPanel);
}
