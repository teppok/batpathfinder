package fi.iki.photon.batmud;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class that implements the main bulk of the A* algorithm, running in a thread.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class BatPathFinderThread extends Thread {
		private static final boolean DEBUG = false;
		
		private final SolvedListener caller;
		private final AreaContainer areaContainer;
		private final Location start, end;
		private final boolean naval;
		private final int lift;
		private final int type;
		
		private boolean doContinue;

		private PlaneLocation approxEndLocation;
		

		private final AHeap open;
		private final AbstractMap<Location, TrueNode> closed;

		/**
		 * Basic constructor.
		 * @param c Called when the thread has finished solving.
		 * @param ac AreaContainer, queried for costs etc.
		 * @param s Start location.
		 * @param e Ending location.
		 * @param n Naval travel.
		 * @param l Lift for naval travel.
		 * @param t Type that will be used when calling SolvedListener.
		 */
		
		BatPathFinderThread(SolvedListener c, AreaContainer ac, Location s, Location e, boolean n, int l, int t) {
			caller = c; areaContainer = ac; start = s; end = e; naval = n; lift = l; type = t;
			open = new AHeap();
			closed = new HashMap<>(100000);
			start();
		}

		/**
		 * Do the solving and after having finished, call SolvedListener with the resulting path and the
		 * original type.
		 */
		@Override
		public void run() {
			try {
				String path = solve();
				if (doContinue == false) {
					caller.solved(null, -1);
				} else {
					if (caller != null) {
						caller.solved(path, type);
					}
				}
			} catch (BPFException e) {
				caller.solved(e.toString(), -2);
			}
		}

		/**
		 * Abort solving.
		 */
		void abort() {
			doContinue = false;
		}

		/**
		 * Sets the approximate end location used for the heuristic.
		 * @param l
		 */
		
		private void setApproxEnd(Location l) {
			approxEndLocation = l.getPlaneLocation();
		}

		/**
		 * Reads the class fields and solves the A* problem.
		 * Return value is a space separated string of commands that might have 
		 * integer values n inbetween, which means that the next command must be
		 * repeated n times.
		 * 
		 * @return A string containing a string of commands from start node to end node.
		 */
		
		private String solve() throws BPFException {
			TrueNode testNode;

			if (start == null || end == null) return null;
		
			setApproxEnd(end);
			
			if (DEBUG) System.out.println("Start: " + start);
			if (DEBUG) System.out.println("End: " + end);
			
			open.insert(new TrueNode(start, null, 0,
					AreaContainer.approx(start.getPlaneLocation(), approxEndLocation, naval)));

			if (DEBUG) System.out.println("Ratkaistaan...\n");

			ArrayList<RoutePart> route = new ArrayList<>();

			/* Repeat the next loop until open-heap is dry or we have reached the end.
			 * 
			 * Open heap contains the nodes that must be tested.
			 * Closed hash contains the Locations that already have been tested at
			 * least once. These Locations may still be replaced if a better route is
			 * found.
			 */
			
			doContinue = true;

			@SuppressWarnings("unused")
			int steps = 0;
			
			
			while ((testNode = open.deleteMin()) != null && doContinue) {
				steps++;
//				if (DEBUG) System.out.println(testNode);
				
				if (DEBUG) {
					if (steps % 1000 == 0) {
						System.out.println(testNode + " c " + closed.size() + " s " + steps);
					}
				}


				/* When we have reached the end, compile the route from RoutePart objects
				 * and exit solve().
				 */
				
				if (testNode.getLoc().equals(end)) {
//					if (DEBUG) System.out.println(steps);
//					if (DEBUG) System.out.println(testNode.cost);
					TrueNode tmp = testNode;
//					int steps2=0;
					while (tmp != null) {
//						steps2++;
//						System.out.println(tmp.loc);
//						area.setRoute(tmp.loc, (steps2 % 9) + 1);

						if (tmp.getParent() != null) {
							int dir = tmp.getDirection();
							
							if (dir != -1) {
								String step = "";
								switch (dir) {
								case 0: step = "nw"; break;
								case 1: step = "n"; break;
								case 2: step = "ne"; break;
								case 3: step = "w"; break;
								case 4: step = "e"; break;
								case 5: step = "sw"; break;
								case 6: step = "s"; break;
								case 7: step = "se"; break;
								}
								if (route.size()>0) {
									RoutePart rlast = route.get(route.size()-1);
									if (rlast.getDirection().equals(step)) {
										rlast.incrementCount();
									} else {
										RoutePart rnew = new RoutePart(1, step);
										route.add(rnew);
									}
								} else {
									RoutePart rnew = new RoutePart(1, step);
									route.add(rnew);
								}
							} else {
								Location tmpLoc = tmp.getLoc();
								Location tmpParentLoc = tmp.getParent().getLoc();
								
								/* Add a RoutePart entry if tmpLoc is of same type than tmpParentLoc.
								 * When changing from NameLocation to PlaneLocation or back, we don't add an entry,
								 * because this we only do when swapping from NameLocation to its adjacent
								 * PlaneLocation or vice versa.
								 */
								
								if (tmpLoc instanceof NameLocation && tmpParentLoc instanceof NameLocation) {
									List<Link> n = ((NameLocation) tmpParentLoc).getNeighbors();
									for (Link l : n) {
										if (l.getDest() == tmpLoc) {
											RoutePart r = new RoutePart(1, l.getLinkString());
											route.add(r);
										}
									}
								}
							}
						}
						tmp = tmp.getParent();
					}

//					area.steps = steps;
//					area.routeCost = (int) testNode.cost;

					return RoutePart.parseRoute(route);
				}

				// Nyt, kun t‰m‰ alkio on k‰yty l‰pi, se lis‰t‰‰n closed-
				// rakenteeseen.
				closed.put(testNode.getLoc(), testNode);

				// Lis‰t‰‰n open-kekoon kaikki alkiot, jotka toteuttavat
				// tietyt ehdot (ks. t‰m‰n funktion kommenteista).
				pushSuccessors(testNode);

//				System.out.println(closed);
//				if (DEBUG2) {
//				printf("closed:");
//				closed.print();
//				printf("open:");
//				open.print();
//				}
			}
			return null;
		}


		/**
		 * Adds some of the nodes next to the current node to the open heap.
		 * If other conditions are met, make changes to the data structures.
		 * 
		 * @param node
		 */
		
		private void pushSuccessors(TrueNode node) throws BPFException {

//			if (DEBUG) System.out.println(node);
			List<TrueNode> neighbors = areaContainer.getNeighbors(node, approxEndLocation, naval, lift);
			int counter = 0;
			while (counter < neighbors.size()) {
				TrueNode newNode = neighbors.get(counter);
				counter++;

				TrueNode oldClosedNode = closed.get(newNode.getLoc());
//				System.out.println("OC: " +oldClosedNode);
				
				// If there is already a closed node at this location
				// and the new node path is of shorter length than the old one,
				// replace the old one and consider the new node as open.
				if (oldClosedNode != null) {

					if (oldClosedNode.compareTo(newNode) > 0) {
						closed.remove(oldClosedNode.getLoc());
						open.insert(newNode);
					}
				} else {

					TrueNode oldOpenNode = open.getNode(newNode.getLoc());
//					System.out.println("OO: "+oldOpenNode);
					
					// If there is an open node at this location and the
					// new path is shorter, remove the old open node and add
					// the new one.

					if (oldOpenNode != null) {
						if (oldOpenNode.compareTo(newNode) > 0) {
							open.deleteNode(oldOpenNode);
							open.insert(newNode);
						}
					} else {
						open.insert(newNode);
					}
				}
			}
		}

	}

