package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class AreaTest {

	@Test
	public void testArea() {
		Tradelanes tl = null;
		Costs c = null;
		try {
			tl = new Tradelanes("testdata/tradelane.txt", 4000, 5000, 4000, 5000, -4097, -4097);
			c = new Costs("testdata/costs", "testdata/costs.ship");
		} catch (Exception e) {
			fail("Exception");
		}

		int ex = 0;
		try {
			Area a = new Area(tl, c, 10, 11, "testdata/test.map.illformed");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, c, 10, 11, "testdata/test.map.illformed2");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(null, c, 10, 11, "testdata/test.map");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, null, 10, 11, "testdata/test.map");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, c, -1, 11, "testdata/test.map");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, c, 10, -2, "testdata/test.map");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, c, -4, -2, "testdata/test.map");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Area a = new Area(tl, c, 10, 11, null);
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		
		try {
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void testGetData() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", 4000, 5000, 4000, 5000, -4097, -4097);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
			
			assertEquals(a.getData(0, 0), 'F');
			assertEquals(a.getData(2, 0), 'f');
			assertEquals(a.getData(0, 1), 'h');

			int ex = 0;
			try {
				assertEquals(a.getData(10, 11), 0);
			} catch (BPFException e) {
				ex = 1;
			}
			assertEquals(ex, 1);

			ex = 0;
			try {
				assertEquals(a.getData(-1, -1), 0);
			} catch (BPFException e) {
				ex = 1;
			}
			assertEquals(ex, 1);
			
		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void testGetCost() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.test.txt", 0, 10, 0, 11, 0, 0);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
			
			assertEquals(a.getCost(0, 0, false, 0), 201);
			assertEquals(a.getCost(6, 6, false, 0), 9);
			assertEquals(a.getCost(1, 3, false, 0), 10000);
			assertEquals(a.getCost(1, 4, false, 0), 10000);

			assertEquals(a.getCost(0, 0, true, 0), 10000);
			assertEquals(a.getCost(0, 0, true, 100), 30);
			assertEquals(a.getCost(0, 0, true, 113), 10);

			assertEquals(a.getCost(2, 2, true, 400), 10);
			assertEquals(a.getCost(3, 3, true, 400), 2);
			assertEquals(a.getCost(5, 5, true, 400), 2);
			assertEquals(a.getCost(6, 6, true, 400), 10);
			assertEquals(a.getCost(5, 6, true, 400), 10);

		} catch (Exception e) {
			fail("Exception");
		}
	}
	
	@Test
	public void testValidLocation() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", 4000, 5000, 4000, 5000, -4097, -4097);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
			
			assertTrue(a.isValidLocation(0, 0));
			assertTrue(a.isValidLocation(9, 0));
			assertTrue(a.isValidLocation(9, 10));
			assertTrue(a.isValidLocation(0, 10));
			assertFalse(a.isValidLocation(10, 0));
			assertFalse(a.isValidLocation(10, 11));
			assertFalse(a.isValidLocation(0, 11));
			assertFalse(a.isValidLocation(-1, 0));
			assertFalse(a.isValidLocation(0, -1));
			
		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void testGetPlaneLocationNeighbors() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.test.txt", 0, 10, 0, 11, 0, 0);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
			
			PlaneLocation p1 = new PlaneLocation(2, 2, 0);
			PlaneLocation p2 = new PlaneLocation(4, 4, 0);
			TrueNode n = new TrueNode(p1, null, 0, 0);
			
			List<TrueNode> list = a.getPlaneLocationNeighbors(n, p2, false, 0);
			assertEquals(list.size(), 7);
			assertEquals(list.get(0).getPlaneLocation().getX(), 1);
			assertEquals(list.get(0).getPlaneLocation().getY(), 1);
			assertEquals(list.get(0).getCost(), 131);
			
		} catch (Exception e) {
			fail("Exception");
		}
			
	}

	@Test
	public void testFindOnMap() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", 4000, 5000, 4000, 5000, -4097, -4097);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
	
			char[][] array = { { 'F', 'F' } , { 'h', 'h' } };
		
			int[] result = a.findOnMap(array);
			assertEquals(result[0], 0);
			assertEquals(result[1], 0);

			char[][] array2 = { { 'h', '-' } , { 'h', '-' } };
			
			result = a.findOnMap(array2);
			assertEquals(result[0], 5);
			assertEquals(result[1], 2);

			char[][] array3 = { { 'z', 'z' } , { 'h', '-' } };
			
			result = a.findOnMap(array3);
			assertEquals(result[0], -1);
			assertEquals(result[1], -1);

			char[][] array4 = { { '#', '#' } , { '#', 'a' } };
			
			result = a.findOnMap(array4);
			assertEquals(result[0], 8);
			assertEquals(result[1], 9);

			char[][] array5 = { { 0, 0 } , { 0, 'a' } };
			
			result = a.findOnMap(array5);
			assertEquals(result[0], 8);
			assertEquals(result[1], 9);

		} catch (Exception e) {
			fail("Exception");
		}
	}

	@Test
	public void testApprox() {
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", 4000, 5000, 4000, 5000, -4097, -4097);
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			Area a = new Area(tl, c, 10, 11, "testdata/test.map");
			
			assertEquals(a.getDiff(2, 2, 3, 3), 1);
			assertEquals(a.getDiff(2, 2, 4, 4), 2);
			assertEquals(a.getDiff(2, 2, 3, 5), 3);
			assertEquals(a.getDiff(2, 2, 1, 0), 2);
			assertEquals(a.getDiff(2, 2, 0, 5), 3);
			assertEquals(a.getDiff(2, 2, 6, 2), 4);
			

			PlaneLocation p1 = new PlaneLocation(2, 2, 0);
			PlaneLocation p2 = new PlaneLocation(3, 5, 0);

			assertEquals(a.approx(p1, p2, false), 27);
			assertEquals(a.approx(p1, p2, true), 15);
			
		} catch (Exception e) {
			fail("Exception");
		}
	}
}
