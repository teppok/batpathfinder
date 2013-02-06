package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NameLocationTest {

	@Test
	public void testHashCode() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		NameLocation n = null;
		try {
			n = new NameLocation("asdf", p, true);
		} catch (Exception e) {
			fail("Exception " + e);
		}
		assertEquals(n.hashCode(), "asdf".hashCode());
		
	}

	@Test
	public void testToString() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		NameLocation n = null;
		try {
			n = new NameLocation("asdf", p, true);
		} catch (Exception e) {
			fail("Exception " + e);
		}
		assertEquals(n.toString(), "asdf");
	
	}

	@Test
	public void testNameLocationException() {
		int e = 0;
		NameLocation n = null;
		try {
			n = new NameLocation("asdf", null, true);
		} catch (BPFException ex) {
			e = 1;
		}
		assertNull(n);
		assertEquals(e, 1);

		e = 0;
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		try {
			n = new NameLocation("", p, true);
		} catch (BPFException ex) {
			e = 1;
		}
		assertNull(n);
		assertEquals(e, 1);

		e = 0;
		try {
			n = new NameLocation(null, p, true);
		} catch (BPFException ex) {
			e = 1;
		}
		assertNull(n);
		assertEquals(e, 1);

	}

	@Test
	public void testNameLocation() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(3, 4, 5);
		NameLocation n = null, n2 = null, n3 = null;
		try {
			n = new NameLocation("asdf", p, true);
			n2 = new NameLocation("asdf", p2, false);
			n3 = new NameLocation("asd", p, false);
		} catch (Exception e) {
			fail("Exception " + e);
		}
		assertNotNull(n);
		assertSame(n.getPlaneLocation(), p);
		assertEquals(n.getContinent(), 2);
		assertEquals(n.isAdjacent(), true);

		assertNotNull(n2);
		assertSame(n2.getPlaneLocation(), p2);
		assertNotSame(n2.getPlaneLocation(), p);
		assertEquals(n2.getContinent(), 5);
		assertEquals(n2.isAdjacent(), false);
	}

	
	@Test
	public void testEquals() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(3, 4, 5);
		NameLocation n = null, n2 = null, n3 = null;
		try {
			n = new NameLocation("asdf", p, true);
			n2 = new NameLocation("asdf", p2, false);
			n3 = new NameLocation("asd", p, false);
		} catch (Exception e) {
			fail("Exception " + e);
		}

		// Test .equals()
		assertTrue(n.equals(n2));
		assertFalse(n.equals(n3));
	
		Location l = n;
		assertTrue(n.equals(l));
		assertFalse(n.equals("moo"));
	}
	
	@Test
	public void testIsReachable() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(3, 4, 5);
		NameLocation n = null, n2 = null, n3 = null;
		try {
			n = new NameLocation("asdf", p, true);
			n2 = new NameLocation("asdf", p2, false);
			n3 = new NameLocation("asd", p, false);
		} catch (Exception e) {
			fail("Exception " + e);
		}
		
		try {
			assertTrue(n.isReachable(true));
			assertTrue(n.isReachable(false));
			
			assertFalse(n2.isReachable(true));
			assertFalse(n2.isReachable(false));
	
			assertFalse(n3.isReachable(true));
			assertFalse(n3.isReachable(false));
	
			Link l = new Link(n, "ghjk", 1, true);
	
			n2.addNeighbor(l);
			assertTrue(n2.isReachable(true));
			assertFalse(n2.isReachable(false));
	
			Link l2 = new Link(n, "ghjk", 1, false);
	
			n3.addNeighbor(l2);
			assertTrue(n3.isReachable(false));
			assertFalse(n3.isReachable(true));
		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void testAddNeighbor() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(3, 4, 5);
		NameLocation n = null, n2 = null, n3 = null;
		Link l = null;
		try {
			n = new NameLocation("asdf", p, true);
			n2 = new NameLocation("asdf", p2, false);
			n3 = new NameLocation("asd", p, false);
			l = new Link(n2, "ghjk", 1, false);
		} catch (Exception e) {
			fail("Exception " + e);
		}

		n.addNeighbor(l);
		List<Link> list = n.getNeighbors();
		assertNotNull(list);
		assertEquals(list.size(), 1);
		assertSame(list.get(0), l);
		
	}

}
