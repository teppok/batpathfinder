package fi.iki.photon.batmud;

import static org.junit.Assert.*;

import org.junit.Test;

public class BPFExceptionTest {

	@Test
	public void test() {
		BPFException e = new BPFException("test");
		assertEquals(e.toString(), "test");
	}

}
