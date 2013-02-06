package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class RoutePartTest {

	@Test
	public void testRoutePart() {
		RoutePart a = new RoutePart(1, "a");
		RoutePart b = new RoutePart(2, "b");
		RoutePart c = new RoutePart(3, "c");
		RoutePart d = new RoutePart(1, "d");
		RoutePart e = new RoutePart(5, "e");
		
		ArrayList<RoutePart> route = new ArrayList<>(10);

		assertEquals(a.getDirection(), "a");
		
		route.add(a);
		route.add(b);
		route.add(c);
		route.add(d);
		route.add(e);
		
		c.incrementCount();
		
		assertEquals(RoutePart.parseRoute(route), "5 e d 4 c 2 b a");
		route = new ArrayList<>(10);
		assertEquals(RoutePart.parseRoute(route), "");
		assertEquals(RoutePart.parseRoute(null), "");
	}

}
