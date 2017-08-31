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

import fi.iki.photon.batmud.AreaContainer;
import fi.iki.photon.batmud.BPFException;
import fi.iki.photon.batmud.Location;
import fi.iki.photon.batmud.SolvedListener;
import fi.iki.photon.batmud.api.BPFApi;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bottom half of the UI for the AreaContainer / BatPathFinder.
 *
 * This UI handles the command line commands and feeds the queries to the
 * AreaContainer to be calculated.
 *
 * The top half of the UI is in BatPathFinderWindow which shows the graphical
 * widgets to the user.
 *
 * The program's setting memory is in BatPathfinderWindow, which is queried when
 * any setting (such as travel mode) is needed. This is done even if the window
 * is not visible.
 *
 * This class contains the variables needed for solving the queries and for
 * processing the command line.
 *
 * @author Teppo Kankaanp‰‰
 *
 */
public class BatPathFinderUI implements SolvedListener {

    private final String baseDir;

    private AreaContainer area;
    private final BPFApi plugin;
    private final BatPathFinderWindow window;

    private boolean initialized = false;

    private boolean openWindow = false;

    // Some variables to keep the data when solved() is
    // called from BatPathFinderThread.
    private boolean solving = false;
    private String path1, path2;
    private boolean pathSolved1, pathSolved2;
    private Location solveStart, solveEnd;

    // These are used to read the small map using successive calls from
    // BatClient to trigger(). We store the lines here.
    private int readMap = 0;
    private char[][] rawMap = new char[13][];
    private int mapW, mapH;

    private String walkString = "";

    /**
     * A standard constructor.
     *
     * @param bd Base directory of batclient.
     * @param p An initialized BPFApi
     * @param openWindow Whether we open a window at the beginning or not.
     */
    public BatPathFinderUI(String bd, BPFApi p, boolean openWindow) {
        baseDir = bd;
        plugin = p;
        this.openWindow = openWindow;
        window = new BatPathFinderWindow(this);
        loadArea();
        if (openWindow) {
            plugin.createWindow(window);
        }
    }

    /**
     * Loads stuff into area.
     */
    private void loadArea() {
        try {
            area = new AreaContainer(baseDir + "/BatPathFinderData");
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            plugin.output(e.toString() + "\n");
        }
    }

    /**
     * Given a sequence of commands or numbered commands, sends these commands
     * to the plugin.
     *
     * @param path Command string such as "2 e ne 3 e"
     * @param limit Limits the amount of steps taken
     * @param strictLimit Does something I don't remember
     * @param ship The movement is naval movement, so send also the appropriate
     * cruise commands.
     * @return The string that was not walked.
     */
    private String walk(String path, int limit, boolean strictLimit, boolean ship) {
        if (!initialized) {
            return "";
        }
        if (path == null || "".equals(path) || " ".equals(path)) {
            return "";
        }
        String[] parts = path.split(" ");
        int i = 0;
        int remaining = -1;
        if (!ship) {
            plugin.doCommand("set look_on_move off");

            int length = 0, prevI;
            String command = "";
            for (i = 0; i < parts.length && remaining == -1;) {
                if (parts[i].charAt(0) >= '0' && parts[i].charAt(0) <= '9') {
                    int currLength = Integer.parseInt(parts[i]);
                    if (length + currLength >= limit) {
                        remaining = length + currLength - limit;
                        currLength = limit - length;
                    }

                    length += currLength;
                    while (currLength > 20) {
                        command = "20 " + parts[i + 1].replace("~", " ") + " ";
                        System.out.println(command.trim());
                        plugin.doCommand(command.trim());
                        currLength -= 20;
                    }
                    command = currLength + " " + parts[i + 1].replace("~", " ") + " ";
                    System.out.println(command.trim());
                    plugin.doCommand(command.trim());

                    prevI = i;
                    i += 2;
                } else {
                    if (length + 1 >= limit) {
                        remaining = 0;
                    }

                    length++;
                    command = parts[i].replace("~", " ") + " ";
                    System.out.println(command.trim());
                    plugin.doCommand(command.trim());
                    prevI = i;
                    i++;
                }

//				System.out.println(length);
                if (!strictLimit) {
                    length = 0;

//					System.out.println(parts[0]);
/*
					plugin.doCommand(command.trim());
					length = 0;
					command = "";
                     */
                    if (remaining > 0) {
                        parts[prevI] = "" + remaining;
                        i = prevI;
                    }
                    remaining = -1;
                }
            }
//			System.out.println(command.trim());
//			plugin.doCommand(command.trim());
            plugin.doCommand("set look_on_move on");
            plugin.doCommand("look");
        } else {
            int length = 0;
            String command = "cruise ";
            boolean first = true;
            boolean sailed = false;
            for (i = 0; i < parts.length && length < limit - 1 && !sailed;) {
                if (parts[i].charAt(0) >= '0' && parts[i].charAt(0) <= '9') {
                    int currLength = Integer.parseInt(parts[i]);
                    length++;
                    if (first && parts[i + 1].startsWith("*")) {
                        command = "sail " + currLength + " " + parts[i + 1].substring(1);
                        sailed = true;
                    } else {
                        if (!first) {
                            command = command + ",";
                        }
                        command = command + currLength + " " + parts[i + 1].replace("~", " ");
                    }
                    i += 2;
                } else {
                    length++;
                    if (first && parts[i].startsWith("*")) {
                        command = "sail " + parts[i].substring(1);
                        sailed = true;
                    } else {
                        if (!first) {
                            command = command + ",";
                        }
                        if (parts[i].length() <= 3) {
                            command = command + "1 " + parts[i].replace("~", " ");
                        } else {
                            command = command + parts[i].replace("~", " ");
                        }
                    }
                    i++;
                }
                first = false;
            }

            if (!sailed) {
                System.out.println(command.trim());
                if (!first) {
                    command = command + ",";
                }
                command = command + "*secure";
            }
            plugin.doCommand("ship launch");
            plugin.doCommand(command.trim());
        }

        String resultString = "";
        if (remaining > 0) {
            resultString = remaining + " " + parts[i - 1] + " ";
        }
        for (int j = i; j < parts.length; j++) {
            resultString = resultString + parts[j] + " ";
        }
        return resultString.trim();
    }

    /**
     * Take a precalculated walk string and perform a walk on that string,
     * storing the remaining path in the walk string.
     *
     * @param turbo
     */
    void doGo(boolean turbo) {
        if (!initialized) {
            return;
        }

        if (window.isGoEnabled()) {
            window.setFrom("");
            int travel = window.getTravel();
            if (travel == 3 || travel == 4) {
                walkString = walk(walkString, (window.getNav() + 1) * 3, true, true);
                report("Path left: " + walkString);
            } else {
                if (travel == 2 || turbo) {
                    window.setGoEnabled(false);
                    walkString = walk(walkString, 200, false, false);
                    report("Path left: " + walkString);
                    window.setGoEnabled(true);
                } else {
                    window.setGoEnabled(false);
                    walkString = walk(walkString, 50, true, false);
                    report("Path left: " + walkString);
                    window.setGoEnabled(true);
                }
            }
            if ("".equals(walkString)) {
                window.setFrom(window.getTo());
                window.setTo("");
            }
        }
    }

    /**
     * Perform a search using the from and to fields in the window. First do
     * some sanity checking, and if everything seems fine, start a solver or two
     * using AreaContainer.
     *
     *
     */
    void doSearch() {
        if (!initialized) {
            return;
        }

        if ("".equals(window.getFrom()) || "".equals(window.getTo())) {
            return;
        }
        //		try {
        walkString = "";

        Location start, end;
        String fromStr = window.getFrom();
        String toStr = window.getTo();

        boolean nav = false, inter = false;
        int travel = window.getTravel();
        if (travel == 3) {
            nav = true;
            inter = false;
        }
        if (travel == 4) {
            nav = true;
            inter = true;
        }
        if (travel == 5) {
            nav = false;
            inter = true;
        }

        int lift = window.getLift();

        start = area.parseLocation(fromStr);
        if (start == null) {
            report("Start location unknown or malformed.");
            return;
        }
        end = area.parseLocation(toStr);
        if (end == null) {
            report("Destination unknown or malformed.");
            return;
        }
        if (start.getContinent() != end.getContinent() && !(inter)) {
            report("Locations on different continents. Choose IC ship or Esiris travel and try again.");
            return;
        }
        if ((!end.isReachable(nav))) {
            report("Destination unreachable.");
            return;
        }
        if (solving) {
            report("Still solving previous assignment. Use \"$w abort\" to abort.");
            return;
        }
        solving = true;
        solveStart = start;
        solveEnd = end;
        report("Processing...");
        if (start.getContinent() != end.getContinent() && (inter)) {

            if (nav) {
                path1 = null;
                path2 = null;
                pathSolved1 = false;
                pathSolved2 = false;

                Location exitNode = area.getExitnode(start.getContinent(), end.getContinent());

                Location[] startNodes = new Location[2];
                Location[] endNodes = new Location[2];
                int[] types = new int[2];
                startNodes[0] = start;
                endNodes[0] = exitNode;
                startNodes[1] = exitNode;
                endNodes[1] = end;
                types[0] = 1;
                types[1] = 2;

                //report("Start: " + startNodes);
                //report("End: " + endNodes);
                try {
                    area.solve(this, startNodes, endNodes, true, lift, types);
                } catch (BPFException e) {
                    error(e.toString());
                }

            } else {
                path1 = null;
                path2 = null;
                pathSolved1 = false;
                pathSolved2 = false;

                Location esirisExitnodeStart = area.getEsirisExitnode(start.getContinent());
                Location esirisExitnodeEnd = area.getEsirisExitnode(end.getContinent());

                Location[] startNodes = new Location[2];
                Location[] endNodes = new Location[2];
                int[] types = new int[2];
                startNodes[0] = start;
                endNodes[0] = esirisExitnodeStart;
                startNodes[1] = esirisExitnodeEnd;
                endNodes[1] = end;
                types[0] = 3;
                types[1] = 4;

                report("Solving with esiris " + esirisExitnodeStart + " " + esirisExitnodeEnd);

                try {
                    area.solve(this, startNodes, endNodes, false, lift, types);
                } catch (BPFException e) {
                    error(e.toString());
                }
            }
        } else {

            Location[] startNodes = new Location[1];
            Location[] endNodes = new Location[1];
            int[] types = new int[1];
            startNodes[0] = start;
            endNodes[0] = end;
            types[0] = 0;

            try {
                area.solve(this, startNodes, endNodes, nav, lift, types);
            } catch (BPFException e) {
                error(e.toString());
            }
        }
    }

    /**
     * Abort solving.
     */
    private void abort() {
        if (!initialized) {
            return;
        }

        area.abort();
        solving = false;
    }

    /**
     * Process the terminal input. If we see a trigger message, we start
     * recording a map. After the map has been recorded, call area.findOnMap to
     * find the x,y coordinates of where we are currently, and run doSearch just
     * in case we already have the to field set and can run somewhere right now.
     *
     *
     * @param strippedraw
     * @return true if we did something with the input, false otherwise
     */
    public boolean trigger(String strippedraw) {
        if (!initialized) {
            return false;
        }
        // Whereami
        try {
            Pattern whereAmIPattern = Pattern.compile("^You are in \\'(.*)\\', which is on the continent of (.*)\\. \\(Coordinates: (\\d+)x, (\\d+)y; Global: (\\d+)x, (\\d+)y\\)");
            Matcher m = whereAmIPattern.matcher(strippedraw.trim());
            if (m.find()) {
                String location = m.group(1);
                String continen = m.group(2);
                int x = Integer.parseInt(m.group(3));
                int y = Integer.parseInt(m.group(4));
                int gx = Integer.parseInt(m.group(5));
                int gy = Integer.parseInt(m.group(6));
                int currCont = window.getContinent();
                window.setFrom("L " + currCont + " " + x + " " + y);
                report("Location " + window.getFrom() + " found.");
                doSearch();
                return true;
            }
        } catch (Exception e) {
            report(e.toString());
            return false;
        }
        return false;
    }

    /**
     * Process the command line input from BatClient.
     *
     * Do whatever we need to do with the command line parameters.
     *
     * @param o
     */
    public void process(Object o) {
        if (!initialized) {
            return;
        }
        if (!(o instanceof String[])) {
            return;
        }
        String[] cmds = (String[]) o;
        if (cmds.length == 0) {
            return;
        }
        if ("get".equals(cmds[0])) {
            if (cmds.length == 2) {
                window.setTo(cmds[1]);
            }
            if (cmds.length == 3) {
                window.setFrom(cmds[1]);
                window.setTo(cmds[2]);
            }
            if (openWindow) {
                plugin.createWindow(window);
            }
            doSearch();
        } else if ("go".equals(cmds[0])) {
            doGo(false);
        } else if ("gogo".equals(cmds[0])) {
            doGo(true);
        } else if ("win".equals(cmds[0])) {
            plugin.createWindow(window);
        } else if ("abort".equals(cmds[0]) || "cancel".equals(cmds[0]) || "clear".equals(cmds[0])) {
            abort();
            window.setFrom("");
            window.setTo("");
            walkString = "";
        } else if ("reload".equals(cmds[0])) {
            plugin.output("BPF: Reloading data.\n");
            loadArea();
//		} else if (("cont".equals(cmds[0]) || "map".equals(cmds[0])) && cmds.length > 1) {
        } else if (("map".equals(cmds[0])) && cmds.length > 1) {
            if (cmds[1].startsWith("lae")) {
                window.setContinent("Laenor");
            }
            if (cmds[1].startsWith("luc")) {
                window.setContinent("Lucentium");
            }
            if (cmds[1].startsWith("des")) {
                window.setContinent("Desolathya");
            }
            if (cmds[1].startsWith("rot")) {
                window.setContinent("Rothikgen");
            }
            if (cmds[1].startsWith("fur")) {
                window.setContinent("Furnachia");
            }

            report("Auto-searching location on " + window.getContString() + ".");
        } else if ("mode".equals(cmds[0]) && cmds.length > 1) {
            if (cmds[1].startsWith("wal")) {
                window.setTravel(1);
                report("Walk mode.");
            }
            if (cmds[1].startsWith("mou")) {
                window.setTravel(2);
                report("Mounted mode.");
            }
            if (cmds[1].startsWith("ship")) {
                window.setTravel(3);
                report("Ship mode.");
            }
            if (cmds[1].startsWith("ic")) {
                window.setTravel(4);
                report("Intercontinental ship mode.");
            }
            if (cmds[1].startsWith("es")) {
                window.setTravel(5);
                report("Intercontinental esiris mode.");
            }

        } else if ("alias".equals(cmds[0]) && cmds.length > 1) {
            try {
                area.addLocationToFile(window.getFrom(), cmds[1]);
                report("Added " + window.getFrom() + " as " + cmds[1] + "\n");
            } catch (Exception e) {
                error(e.toString() + "\n");
            }
        } else if ("help".equals(cmds[0])) {
            plugin.output("BatPathFinder accepted commands are:\n"
                    + "$w win                       (Open program window)\n"
                    + "$w get <dest>\n"
                    + "$w get <source> <dest>\n"
                    + "$w abort                     (Abort solver if it's taking too long, or stop current trip)\n"
                    + "$w cancel                    (The same)\n"
                    + "$w clear                     (The same)\n"
                    + "$w go                        (Go next 50 steps)\n"
                    + "$w gogo                      (Go all the way)\n"
                    + "$w map <continent>           (Set location auto-find continent)\n"
                    + "$w map                       (Force read map on ships etc. where it can't be triggered)\n"
                    + "$w reload\n"
                    + "$w alias <alias>             (Create an alias for the current from field)\n"
                    + "$w mode <walk|mount|ship|ic>\n"
                    + "$w <dest>                    (Like get, but only if none of the other commands match)\n"
                    + "$w <source> <dest>           (Likewise)\n");
        } else if ("map".equals(cmds[0])) {
            readMap = -1;
            plugin.doCommand("@@map");
        } else {
            if (cmds.length == 1) {
                window.setTo(cmds[0]);
            }
            if (cmds.length == 2) {
                window.setFrom(cmds[0]);
                window.setTo(cmds[1]);
            }
//			if (!"".equals(window.getFrom()) && !"".equals(window.getTo())) {
            doSearch();
//			}
        }
    }

    /**
     * Output an error.
     *
     * @param s
     */
    public void error(String s) {
        plugin.output("BPF ERROR: " + s + "\n");
    }

    /**
     * Output a standard report.
     *
     * @param s
     */
    public void report(String s) {
        plugin.output("BPF: " + s + "\n");
        //resultArea.setText(resultArea.getText() + "BPF: " + s + "\n");
        window.setReport("BPF: " + s + "\n");
    }

    /**
     * Implements SolvedListener.solved. Based on type we store the paths into
     * variables and when we have seen and stored both parts of the paths, we
     * compile them together and do a walk on them.
     */
    @Override
        public void solved(String solvedPath, int type) {
//		report("Called "+ type + " " + solvedPath + " " + 
//				solveStart + " " + solveEnd);
        if (type == -1) {
            walkString = "";
            report("Aborted.");
            abort();
            return;
        }
        if (type == 0) {
            walkString = solvedPath;
            if (walkString == null) {
                walkString = "";
                report("Destination unreachable.");
            } else {
                report(solveStart + " -> " + solveEnd + ": " + walkString);
            }
            abort();
            doGo(false);
            return;
        }
        boolean walking = false;

        if (type == 1) {
            path1 = solvedPath;
            pathSolved1 = true;
        }
        if (type == 2) {
            path2 = solvedPath;
            pathSolved2 = true;
        }
        if (type == 3) {
            path1 = solvedPath;
            pathSolved1 = true;
            walking = true;
        }
        if (type == 4) {
            path2 = solvedPath;
            pathSolved2 = true;
            walking = true;
        }

        if (pathSolved1 && pathSolved2) {
            if (path1 == null || path2 == null) {
                walkString = "";
                report("Destination unreachable.");
            } else {
                if (walking) {
                    walkString = (path1 + " "
                            + area.getEsirisContinentChange(solveStart.getContinent(), solveEnd.getContinent())
                            + " " + path2).trim();
                    report(solveStart + " -> " + solveEnd + ": " + walkString);
                    doGo(false);
                } else {
                    walkString = (path1 + " "
                            + //continentChange[solveStart.getContinent()][solveEnd.getContinent()] +
                            " " + path2).trim();
                    report(solveStart + " -> " + solveEnd + ": " + walkString);
                }
            }
            abort();
        }
    }
}
