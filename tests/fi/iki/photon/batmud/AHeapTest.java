package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

public class AHeapTest {

	@Test
	public void testDisallowSameLocationsInsert() {
		AHeap a = new AHeap();
		
		int ex = 0;
		try {
			Location l = new PlaneLocation(1, 1, 0);
			Location l2 = new PlaneLocation(1, 1, 0);
			TrueNode n = new TrueNode(l, null, 0, 1);
			TrueNode n2 = new TrueNode(l2, null, 0, 1);
			
			a.insert(n);
			a.insert(n2);
		} catch (BPFException e) {
			ex = 1;
		}
		assertEquals(ex, 1);
	}

	@Test
	public void testInsert() {
		AHeap a = new AHeap();

		try {
			Location l = new PlaneLocation(1, 1, 0);
			Location l2 = new PlaneLocation(1, 2, 1);
			Location l3 = new PlaneLocation(1, 3, 1);
			Location l4 = new PlaneLocation(1, 4, 1);
			TrueNode n = new TrueNode(l, null, 3, 0);
			TrueNode n2 = new TrueNode(l2, null, 0, 1);
			TrueNode n3 = new TrueNode(l3, null, 0, 2);
			TrueNode n4 = new TrueNode(l4, null, 2, 2);
			
			a.insert(n);
			a.insert(n2);
			a.insert(n3);
			a.insert(n4);
			
			assertSame(a.getNode(l), n);
			assertSame(a.getNode(l2), n2);
			assertSame(a.getNode(l3), n3);
			assertSame(a.getNode(l4), n4);

			assertSame(a.deleteMin(), n2);
			assertSame(a.deleteMin(), n3);
			assertSame(a.deleteMin(), n);
			assertSame(a.deleteMin(), n4);
			assertSame(a.deleteMin(), null);

			a.insert(n);
			a.insert(n2);
			a.insert(n3);
			a.insert(n4);

			a.deleteNode(n3);
			
			assertNull(a.getNode(l3));
			
			assertSame(a.deleteMin(), n2);
			assertSame(a.deleteMin(), n);
			assertSame(a.deleteMin(), n4);
			assertSame(a.deleteMin(), null);
			
			
		} catch (Exception e) {
			fail("Exception");
		}
	}

}
