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
package fi.iki.photon.batmud.ui;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.Dimension;

/**
 * A small info panel.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 0;
	/** Basic constructor. */
	InfoPanel() {
		JTextArea text = new JTextArea(
				"This utility finds paths from place to place. " +
				"Find place names from the list tab, input them to " + 
				"the from and to fields, press find button to get the " +
				"path, review it briefly and press go button to walk " +
				"approximately the first 50 steps. Press again to walk " +
				"the next steps until you arrive at the destination. " + 
				"Type \"$w win\" to bring this window up again.");

		text.setPreferredSize(new Dimension(500,400));
		text.setEditable(false);
		text.setLineWrap(true);
        text.setWrapStyleWord(true);
        this.add(text);
	}
}
