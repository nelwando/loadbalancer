package com.neil.harvey.loadbalancer.endpoint;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEndPoint {

	@Test
	public void testEndPoint() {
		EndPoint ep = new EndPoint("localhost", 8080);
		assertEquals("localhost", ep.getHost());
		assertEquals(8080, ep.getPort());
		assertTrue(ep.isHealthy());
		assertEquals(0, ep.getActiveConnections());
	}

	@Test
	public void testGetHost() {
		EndPoint ep = new EndPoint("127.0.0.1", 1234);
		assertEquals("127.0.0.1", ep.getHost());
	}

	@Test
	public void testGetPort() {
		EndPoint ep = new EndPoint("host", 5555);
		assertEquals(5555, ep.getPort());
	}

	@Test
	public void testIncrementActiveConnections() {
		EndPoint ep = new EndPoint("host", 1);
		assertEquals(0, ep.getActiveConnections());
		ep.incrementActiveConnections();
		assertEquals(1, ep.getActiveConnections());
		ep.incrementActiveConnections();
		assertEquals(2, ep.getActiveConnections());
	}

	@Test
	public void testDecrementActiveConnections() {
		EndPoint ep = new EndPoint("host", 1);
		ep.incrementActiveConnections();
		ep.incrementActiveConnections();
		assertEquals(2, ep.getActiveConnections());
		ep.decrementActiveConnections();
		assertEquals(1, ep.getActiveConnections());
		ep.decrementActiveConnections();
		assertEquals(0, ep.getActiveConnections());
		// Should not go below zero
		ep.decrementActiveConnections();
		assertEquals(0, ep.getActiveConnections());
	}

	@Test
	public void testGetActiveConnections() {
		EndPoint ep = new EndPoint("host", 1);
		assertEquals(0, ep.getActiveConnections());
		ep.incrementActiveConnections();
		assertEquals(1, ep.getActiveConnections());
	}

	@Test
	public void testIsHealthy() {
		EndPoint ep = new EndPoint("host", 1);
		assertTrue(ep.isHealthy());
		// Use reflection to set health to false (since setHealthy is package-private)
		try {
			java.lang.reflect.Method m = EndPoint.class.getDeclaredMethod("setHealthy", boolean.class);
			m.setAccessible(true);
			m.invoke(ep, false);
		} catch (Exception e) {
			fail("Reflection failed: " + e.getMessage());
		}
		assertFalse(ep.isHealthy());
	}

	@Test
	public void testSetHealthy() {
		EndPoint ep = new EndPoint("host", 1);
		assertTrue(ep.isHealthy());
		try {
			java.lang.reflect.Method m = EndPoint.class.getDeclaredMethod("setHealthy", boolean.class);
			m.setAccessible(true);
			m.invoke(ep, false);
		} catch (Exception e) {
			fail("Reflection failed: " + e.getMessage());
		}
		assertFalse(ep.isHealthy());
	}

	@Test
	public void testToString() {
		EndPoint ep = new EndPoint("host", 1);
		String s = ep.toString();
		assertTrue(s.contains("host:1"));
		assertTrue(s.contains("active connections: 0"));
		assertTrue(s.contains("healthy = true"));
		// Change state and check again
		ep.incrementActiveConnections();
		try {
			java.lang.reflect.Method m = EndPoint.class.getDeclaredMethod("setHealthy", boolean.class);
			m.setAccessible(true);
			m.invoke(ep, false);
		} catch (Exception e) {
			fail("Reflection failed: " + e.getMessage());
		}
		s = ep.toString();
		assertTrue(s.contains("active connections: 1"));
		assertTrue(s.contains("healthy = false"));
	}
}