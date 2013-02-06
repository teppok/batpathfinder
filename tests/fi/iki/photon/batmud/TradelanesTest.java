package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TradelanesTest {

	@Test
	public void test() {
		int ex;
		
		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelanes-notexists", "testdata/costs.ship");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", "testdata/costs.ship-notexists");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed2", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed2")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed3", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed3")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);
		
		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed4", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed4")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);
		
		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed5", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed tradelane")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed6", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed6")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed7", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/tradelane.badlyformed7")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.badlyformed8", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed tradelane")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", "testdata/costs.ship.badlyformed");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", "testdata/costs.ship.badlyformed2");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed2")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", "testdata/costs.ship.badlyformed3");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed3")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		try {
			Tradelanes tl = new Tradelanes("testdata/tradelane.txt", "testdata/costs.ship");
			
			assertEquals(tl.getMinLift('h'), 28);
			assertEquals(tl.getMaxLift('h'), 42);
			assertEquals(tl.getMinLift('ö'), 10000);
			assertEquals(tl.getMaxLift('('), 10000);
			
			assertTrue(tl.isOnTradeLane(4500 - 4097, 4500 - 4097, 0));

			assertTrue(tl.isOnTradeLane(4600 - 4097, 4500 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4601 - 4097, 4500 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4600 - 4097, 4500 - 4097, 1));
			assertFalse(tl.isOnTradeLane(4500 - 4097, 4601 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4601 - 4097, 4601 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4399 - 4097, 4601 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4399 - 4097, 4500 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4399 - 4097, 4399 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4500 - 4097, 4399 - 4097, 0));
			assertFalse(tl.isOnTradeLane(4601 - 4097, 4399 - 4097, 0));

			assertTrue(tl.isOnTradeLane(4550 - 4097, 4550 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4551 - 4097, 4551 - 4097, 0));
			
			assertTrue(tl.isOnTradeLane(4500 - 4097, 4520 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4490 - 4097, 4510 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4470 - 4097, 4500 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4460 - 4097, 4460 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4500 - 4097, 4445 - 4097, 0));
			assertTrue(tl.isOnTradeLane(4585 - 4097, 4415 - 4097, 0));

			
		} catch (IOException e) {
			fail ("Exception");
		}
		
		
		
	}

}
