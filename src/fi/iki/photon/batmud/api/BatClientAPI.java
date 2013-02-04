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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.iki.photon.batmud.ui.BatPathFinderUI;

/**
 * API wrapper for BatClient. Does BatClient specific initialization 
 * and handles calls to BPFApi and routes them to BatClient.
 * Implements interfaces required by BatClientPluginTrigger and
 * overrides some methods from BatClientPlugin.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class BatClientAPI extends BatClientPlugin implements BatClientPluginTrigger, BPFApi {


	private BatWindow pluginWindow;
	private JFrame standaloneWindow;
	private BatPathFinderUI bpfui;
	private boolean initialized = false;
	
	@Override
	public String getName() { return "BatPathfinder"; }
	
	/**
	 * Loads the plugin.
	 */
	
	@Override
	public void loadPlugin() {
		output("BPF: Loading!\n");
		output("BPF: See \"$w help\" for command reference and \"$w win\" to open program window\n");

		if (getMacroManager()==null) {
			// Do nothing.
		} else {
			bpfui = new BatPathFinderUI(getBaseDirectory(), this, false);
			initialized = true;
		}
	}

	@Override
	public void doCommand(String s) {
		if (initialized) {
			getClientGUI().doCommand(s);
		}
	}

	@Override
	public void process(Object o) {
		if (initialized) {
			bpfui.process(o);
		}
	}
	
	@Override
	public void output(String s) {
		if (initialized) {
			getClientGUI().printText("generic",s);
		}
	}
	
	@Override
	public void createWindow(JPanel pathPanel) {
		if (initialized) {
			if (pluginWindow != null) pluginWindow.close();
			if (standaloneWindow != null) standaloneWindow.setVisible(false);
			Dimension d = pathPanel.getPreferredSize();
			pluginWindow = getClientGUI().createBatWindow("BatPathFinder", 500, 20, d.width+5, d.height+5);
			if (pluginWindow != null) {
				pluginWindow.removeTabAt(0);
				pluginWindow.newTab("Find Path", pathPanel);
	/*			pluginWindow.newTab("Find Path", getClientGUI().createScrollPane(
						//bpfui.pathPanel,
						new JPanel(),
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
						
					));
	*/
				//			win.newTab("List", listPanel);
	//			win.newTab("Info", infoPanel);
				pluginWindow.setVisible(true);			
			} else {
				standaloneWindow = new JFrame("BPF");
				standaloneWindow.add(pathPanel);
				standaloneWindow.pack();
				standaloneWindow.setVisible(true);
			}
		}
	}

	@Override
	public ParsedResult trigger(ParsedResult a) {
		if (initialized) {
			boolean b = bpfui.trigger(a.getStrippedText());
			if (b) return a;
		}
		return null;
	}
}
