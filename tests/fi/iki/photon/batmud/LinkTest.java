package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

public class LinkTest {

	@Test
	public void testLinkNameLocationStringIntBoolean() {
		try {
			int ex = 0;
			PlaneLocation p = new PlaneLocation(1, 2, 0);
			NameLocation n = new NameLocation("asdf", p, true);

			ex = 0;
			try {
				Link l10 = new Link(null, null, 1, false);
			} catch (BPFException e) {
				ex = 1;
			}
			assertEquals(ex, 1);

			ex = 0;
			try {
				Link l10 = new Link(null, "asdf", 1, false);
			} catch (BPFException e) {
				ex = 1;
			}
			assertEquals(ex, 1);
			
			ex = 0;
			try {
				Link l10 = new Link(n, null, 1, false);
			} catch (BPFException e) {
				ex = 1;
			}
			assertEquals(ex, 1);

			
			Link l = new Link(n, "2 e 3 f", 1, false);
			
			assertEquals(l.getNaval(), false);
			assertEquals(l.getLinkString(), "2 e 3 f");
			assertEquals(l.getCost(), 1);
			assertEquals(l.getDest(), n);
			assertEquals(l.toString(), "asdf");
			
			Link l2 = new Link(n, "2 e 3 f", false);
			assertEquals(l2.getCost(), 5);
			Link l3 = new Link(n, "e f", false);
			assertEquals(l3.getCost(), 2);
			Link l4 = new Link(n, "e 3 f", false);
			assertEquals(l4.getCost(), 4);
			Link l5 = new Link(n, "2 e 3 f 5 e a a f", false);
			assertEquals(l5.getCost(), 13);
			Link l6 = new Link(n, "", false);
			assertEquals(l6.getCost(), 0);
			Link l7 = new Link(n, "+ a b", false);
			assertEquals(l7.getCost(), 3);
			
		} catch (Exception e) {
			fail ("Exception");
		}
		
		
		
	}
}
