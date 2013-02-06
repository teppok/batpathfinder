package fi.iki.photon.batmud;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AHeapTest.class, BPFExceptionTest.class, LinkTest.class,
	NameLocationTest.class,
	PlaneLocationTest.class, RoutePartTest.class, TradelanesTest.class,
	TrueNodeTest.class })
public class AllTests {

}
