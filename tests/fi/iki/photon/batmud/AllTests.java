package fi.iki.photon.batmud;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AHeapTest.class, AreaTest.class, 
	BPFExceptionTest.class, CostsTest.class, 
	LinkTest.class,
	NameLocationTest.class,
	PlaneLocationTest.class, RoutePartTest.class, TradelanesTest.class,
	TrueNodeTest.class })
public class AllTests {

}
