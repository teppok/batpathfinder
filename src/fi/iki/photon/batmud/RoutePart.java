package fi.iki.photon.batmud;

import java.util.ArrayList;

/**
 * A small class for constructing routes.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class RoutePart {
	private int count;
	private final String direction;
	/**
	 * A basic constructor.
	 * @param c
	 * @param d
	 */
	RoutePart(int c, String d) {
		this.count = c; this.direction = d;
	}
	/**
	 * Increments the count of this direction.
	 */
	void incrementCount() {
		count++;
	}
	/**
	 * Basic getter for the direction.
	 * @return direction
	 */

	String getDirection() { return direction; }
	
	/**
	 * Given a list of route parts, constructs a space separated string of 
	 * the counts and directions. Eg. "2 e ne 2 e"
	 * 
	 * @param route
	 * @return route as a command string
	 */
	
	static String parseRoute(ArrayList<RoutePart> route) {
		String routeString = "";
		for (int i = 0; i < route.size(); i++) {
			RoutePart r = route.get(i);
			if (r.count > 1) { 
				routeString = String.valueOf(r.count) + " " + r.direction + " " + routeString;
			} else {
				routeString = r.direction + " " + routeString;
			}
		}
		return routeString.trim();
	}
}