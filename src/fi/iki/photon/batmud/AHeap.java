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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.PriorityQueue;

/** A wrapper for a priority queue and hashmap for fast heap operations
 * and a fast get().
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

class AHeap {


	private final AbstractMap<Location, TrueNode> hash;
	private final PriorityQueue<TrueNode> queue;

	/** Initializes an empty AHeap.
	 * 
	 */
	
	AHeap() {
		this.queue = new PriorityQueue<>(3000);
		this.hash = new HashMap<>(3000);
	}

	/*
	String size() {
		return this.queue.size() + "," + this.hash.size();
	}
	*/

	/** Deletes a node in the heap.
	 * 
	 * @param n	A node to be deleted.
	 */
	
	void deleteNode(TrueNode n) {
		this.queue.remove(n); // O(n)
		this.hash.remove(n.getKey()); // O(1)
	}

	/** Gets a node from the heap containing the Location.
	 * 
	 * @param l Location to be fetched.
	 * @return The TrueNode containing the location l.
	 */

	TrueNode getNode(Location l) {
		return this.hash.get(l); // O(1)
	}


	/** Takes element with the smallest key from the heap.
	 * 
	 * @return The element with the smallest key.
	 */

	TrueNode deleteMin() {
		TrueNode tn = this.queue.poll(); // O(log n)
		if (tn != null) {
			this.hash.remove(tn.getKey()); // O(1)
			return tn;
		}
		return null;
	}

	/** Inserts an element in the heap. */
	
	void insert(TrueNode n) {
		this.queue.add(n); // O(log n)
		this.hash.put(n.getKey(), n); // O(1)
	}
}

