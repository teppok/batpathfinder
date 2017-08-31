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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fi.iki.photon.batmud.ui.BatPathFinderUI;

/**
 * A simple class that implements the BPFApi interface and routes the
 * output to the terminal.
 * 
 * Used mainly as a debug class, tests are hard coded in the main().
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class ShellAPI implements BPFApi, WindowListener {

	private BatPathFinderUI bpfui;
	
	/** Basic constructor that initializes a bpfui. */
	
	ShellAPI() {
		bpfui = new BatPathFinderUI("C:\\Users\\Fizzl\\Batclient", this, true);
	}
	
	@Override
	public void output(String s) {
		System.out.print(s);
	}
	
	@Override
	public void doCommand(String s) {
		System.out.println("Command: " + s);
	}

	/**
	 * Just redirects processing parameters to bpfui.
	 * @param o
	 */
	
	public void process(Object o) {
		bpfui.process(o);
	}
	
	@Override
	public void createWindow(JPanel pathPanel) {
		JFrame jf = new JFrame("BPF");
		jf.add(pathPanel);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.addWindowListener(this);
//		jf.add(new ListPanel(a));
//		jf.add(new InfoPanel());
		jf.pack();
		jf.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// Auto-generated method stub
	}


	/**
	 * Main testing method.
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {

		try {
//			Registry registry = LocateRegistry.getRegistry();
//			registry.unbind("BPF");
			
			ShellAPI shell = new ShellAPI();

//			shell.process(new String[] { "mode", "ic" });
//			shell.process(new String[] { "laenor9", "ancient_temple_of_gods" });
			shell.process(new String[] { "mode", "walk" });
			shell.process(new String[] { "sc", "mithilstonedown" });
//			BatPathFinderUI test = new BatPathFinderUI("C:\\Documents and Settings\\Teppo\\Batclient", null);
//			BPF stub = (BPF) UnicastRemoteObject.exportObject(test, 0); 
//			System.err.println("1");
//			registry.bind("BPF", stub);

//			test.createPathPanel();
//			test.area = new Area("C:\\Documents and Settings\\Teppo\\Batclient\\BatPathFinderData", test);
//			test.area.load();
//			test.initValues();




//			System.err.println("Server ready");
//		} catch (Exception e) {
//			System.err.println("Server exception: " + e.toString());
//			e.printStackTrace();
//		}
		
		//		a.preCalculateCosts();
//		solve(a, a.getPlaneLocation(0, 0), a.getPlaneLocation(650,650));
//		solve(a, a.getPlaneLocation(236, 528), a.getPlaneLocation(615,100));
//		dijkstra(a, a.getPlaneLocation(236, 528), a.getPlaneLocation(615,100));
//		System.out.println(a.getNameLocation("church"));
//		System.out.println(a.getNameLocation("desodockc").getContinent());
		//		String result = solve(a, a.getNameLocation("orion"), a.getNameLocation("orionc"));
//		char[][] pattern = { { '~', '~', '~' },{ 'S', 'S', 'S' }, { 'b', 'b', 'b' } };  
//		BatPathFinderUI.findOnMap(a, 0, pattern);
//		Location start = test.area.getNameLocation("daerwon");
//		Location end = test.area.getNameLocation("desodockc");
//		boolean nav = true;
	
//		test.area.addLocationOnDisk(2, 3, 0, "test");
//		System.out.println(":"+test.walk("8 e ne 3 e 2 se 28 s", 3, true, false));
		
//		test.initValues();
//		String path = BatPathFinder.solve(test.area, start, test.exitNodes[start.getContinent()][end.getContinent()], nav);
//		path = path + " " + test.continentChange[start.getContinent()][end.getContinent()];
//		path = path + " " + BatPathFinder.solve(test.area, test.exitNodes[end.getContinent()][start.getContinent()], end, nav);
		
//	String result = solve(test.area, test.area.getNameLocation("lucentium1"), test.area.getNameLocation("votk"), true);
//		BatPathFinder b = new BatPathFinder(test, test.area, test.area.getNameLocation("desolathya1"), test.area.getNameLocation("desodock"), true, 140, 0);
//		BatPathFinder b = new BatPathFinder(test, test.area, test.area.getNameLocation("desodock"), test.area.getNameLocation("desolathya1"), true, 140, 0);

		
//		String result = solve(a, a.getNameLocation("orionc"), a.getNameLocation("mithil"), true, false);
//		String result = solve(a, a.getNameLocation("daerwon"), a.getPlaneLocation(376,295,0), true);
//		System.out.println(result);
	//	a.print(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
