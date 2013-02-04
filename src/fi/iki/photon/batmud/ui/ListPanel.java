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

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import fi.iki.photon.batmud.AreaContainer;

import java.awt.Dimension;
import java.util.Arrays;

/**
 * A panel which shows all possible NameLocations.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class ListPanel extends JPanel {
	static final long serialVersionUID = 0;
	AreaContainer area;
	
	ListPanel(AreaContainer a) {
		area = a;
		JTextArea areaNames = new JTextArea(3, 50);

		String areaString = "";
		int i = 0;
		Object[] areas = area.getCityNodes().toArray();
		Arrays.sort(areas);
		for (i=0; i<areas.length; i++) {
			areaString += areas[i] + "\n";
		}
		areaNames.setRows(i);
		areaNames.setText(areaString);
		areaNames.setEditable(false);
		JScrollPane areaScrollPane = new JScrollPane(areaNames);
		areaScrollPane.setPreferredSize(new Dimension(500,400));
		this.add(areaScrollPane);
	}
}
