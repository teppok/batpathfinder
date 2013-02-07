package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CostsTest {

	@Test
	public void testCosts() {
		int ex;
		ex = 0;
		try {
			Costs c = new Costs(null, null);
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs", null);
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs(null, "testdata/costs.ship");
		} catch (IOException e) {
			ex = 1;
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs-doesntexist", "testdata/costs.ship");
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs", "testdata/costs.ship-doesntexist");
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs.badlyformed", "testdata/costs.ship");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.badlyformed")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs", "testdata/costs.ship.badlyformed");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs", "testdata/costs.ship.badlyformed2");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed2")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		ex = 0;
		try {
			Costs c = new Costs("testdata/costs", "testdata/costs.ship.badlyformed3");
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Malformed testdata/costs.ship.badlyformed3")) {
				ex = 1;
			}
		}
		assertEquals(ex, 1);

		try {
			Costs c = new Costs("testdata/costs", "testdata/costs.ship");
			assertEquals(c.getMinLift('h'), 28);
			assertEquals(c.getMaxLift('h'), 42);
			assertEquals(c.getMinLift('ö'), 10000);
			assertEquals(c.getMaxLift('('), 10000);
			
			assertEquals(c.calcWeight('-'), 9);
			assertEquals(c.calcWeight('h'), 131);
			assertEquals(c.calcWeight('('), 10000);
		} catch (Exception e) {
			fail("Exception");
		}

		
	}

}
