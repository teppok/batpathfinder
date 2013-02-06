package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrueNodeTest {

	@Test
	public void testTrueNode() {
		int ex = 0;
		try {
			TrueNode n = new TrueNode(null, null, 1, 2);
		} catch (BPFException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		try {
			Location l = new PlaneLocation(2, 2, 0);
			Location l2 = new PlaneLocation(1, 1, 0);
			TrueNode n = new TrueNode(l, null, 1, 2);
			TrueNode n2 = new TrueNode(l2, n, 3, 4);
			TrueNode n3 = new TrueNode(l2, n2, 5, 6);
			TrueNode n4 = new TrueNode(l2, n3, 5, 6);
			
			assertEquals(n.getCost(), 1);
			assertEquals(n.getParent(), null);
			assertSame(n.getLoc(), l);
			assertSame(n.getPlaneLocation(), l);
			assertEquals(n.getKey(), l);
	
			assertEquals(n.toString(), "L 0 2 2 3 1");
			
			assertEquals(n.hashCode(), l.hashCode());
	
			assertSame(n2.getParent(), n);
	
			assertTrue(n.compareTo(n2) < 0);
			assertTrue(n3.compareTo(n) > 0);
			assertTrue(n3.compareTo(n4) == 0);
	
			assertEquals(n.getDirection(), -1);
			l2 = new PlaneLocation(1, 1, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 0);
			l2 = new PlaneLocation(2, 1, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 1);
			l2 = new PlaneLocation(3, 1, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 2);
			l2 = new PlaneLocation(1, 2, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 3);
			l2 = new PlaneLocation(3, 2, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 4);
			l2 = new PlaneLocation(1, 3, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 5);
			l2 = new PlaneLocation(2, 3, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 6);
			l2 = new PlaneLocation(3, 3, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), 7);
			
			l2 = new PlaneLocation(4, 3, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);
			l2 = new PlaneLocation(2, 2, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);
			l2 = new PlaneLocation(0, 0, 0);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);
			l2 = new PlaneLocation(1, 1, 1);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);
	
			l2 = new NameLocation("asdf", (PlaneLocation) l, true);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);

			l = new NameLocation("asdf", (PlaneLocation) l, true);
			n = new TrueNode(l, n, 1, 2);
			l2 = new PlaneLocation(1, 1, 1);
			n2 = new TrueNode(l2, n, 3, 4);
			assertEquals(n2.getDirection(), -1);
		} catch (Exception e) {
			fail("Exception");
		}
	}
}
