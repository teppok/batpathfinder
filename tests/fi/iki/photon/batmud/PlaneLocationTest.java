package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlaneLocationTest {

	@Test
	public void testHashCode() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(0, 1, 2);
		PlaneLocation p3 = new PlaneLocation(1, 1, 2);
		assertEquals(p.hashCode(), p2.hashCode());
		assertTrue(p.hashCode() != p3.hashCode());
	}

	@Test
	public void testPlaneLocation() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		
		assertEquals(p.getX(), 0);
		assertEquals(p.getY(), 1);
		assertSame(p.getPlaneLocation(), p);
		assertEquals(p.getContinent(), 2);
		
		PlaneLocation p2 = new PlaneLocation(-1, -2, -3);
		assertEquals(p2.getX(), -1);
		assertEquals(p2.getY(), -2);
		assertSame(p2.getPlaneLocation(), p2);
		assertEquals(p2.getContinent(), -3);

		// Maximum size of maps plus some
		PlaneLocation p3 = new PlaneLocation(1500, 1501, 4);
		assertEquals(p3.getX(), 1500);
		assertEquals(p3.getY(), 1501);
		assertSame(p3.getPlaneLocation(), p3);
		assertEquals(p3.getContinent(), 4);

	}

	@Test
	public void testIsReachable() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		assertTrue(p.isReachable(true));
		assertTrue(p.isReachable(false));
	}

	@Test
	public void testToString() {
		PlaneLocation p = new PlaneLocation(1, 2, 3);
		assertEquals(p.toString(), "L 3 1 2");
	}

	@Test
	public void testEqualsObject() {
		PlaneLocation p = new PlaneLocation(0, 1, 2);
		PlaneLocation p2 = new PlaneLocation(3, 1, 2);
		PlaneLocation p3 = new PlaneLocation(0, 3, 2);
		PlaneLocation p4 = new PlaneLocation(0, 1, 3);
		PlaneLocation p5 = new PlaneLocation(0, 1, 2);
		
		assertTrue(p.equals(p5));
		assertFalse(p.equals(p2));
		assertFalse(p.equals(p3));
		assertFalse(p.equals(p4));
		assertFalse(p2.equals(p));
		assertFalse(p2.equals(p3));
		assertFalse(p2.equals(p4));
		assertTrue(p.equals(p));
		assertFalse(p.equals("moo"));
	}

	@Test
	public void testParseLocation() {
		Location loc = PlaneLocation.parseLocation("L 3 1 2");
		
		assertTrue(loc instanceof PlaneLocation);
		PlaneLocation p = (PlaneLocation) loc;
		assertEquals(p.getContinent(), 3);
		assertEquals(p.getX(), 1);
		assertEquals(p.getY(), 2);
		
		loc = PlaneLocation.parseLocation("a b c d");
		assertNull(loc);

		loc = PlaneLocation.parseLocation("L 3 1");
		assertNull(loc);

		loc = PlaneLocation.parseLocation("L 3 1 2 3");
		assertNull(loc);

		loc = PlaneLocation.parseLocation("L -1 1 2");
		assertNull(loc);
		loc = PlaneLocation.parseLocation("L 3 -1 2");
		assertNull(loc);
		loc = PlaneLocation.parseLocation("L 3 1 -1");
		assertNull(loc);
		loc = PlaneLocation.parseLocation("L 5 1 2");
		assertNull(loc);
		loc = PlaneLocation.parseLocation("L 3 1000 2");
		assertNull(loc);
		loc = PlaneLocation.parseLocation("L 3 1 1000");
		assertNull(loc);

	}

}
