package com.dev.apiviewer.handler;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPackagesHandler {

	@Test
	public void testNnumberOfSubstrings() {
		String target = "java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock";
		int n = PackagesHandler.numberOfSubstrings(target, ".");
		assertEquals(n, 5);
		
		n = PackagesHandler.numberOfSubstrings(target, "ock");
		assertEquals(n, 3);
		
		n = PackagesHandler.numberOfSubstrings(target, "Random");
		assertEquals(n, 0);
	}

}
