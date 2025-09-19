package com.neil.harvey.loadbalancer.algorithm.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestIPHashAlgorithm {
    private IPHashAlgorithm algo;
    private List<EndPoint> endpoints;
    private EndPoint ep1, ep2, ep3;

    @Before
    public void setUp() {
        algo = new IPHashAlgorithm();
        ep1 = new EndPoint("host1", 8080);
        ep2 = new EndPoint("host2", 8081);
        ep3 = new EndPoint("host3", 8082);
        endpoints = Arrays.asList(ep1, ep2, ep3);
    }

    @Test
    public void testReturnsNullIfEmptyList() {
        assertNull(algo.getEndpoint(Collections.emptyList(), "1.2.3.4", 1234));
    }

    @Test
    public void testStickyMappingForSameIP() {
        String ip = "10.0.0.1";
        EndPoint first = algo.getEndpoint(endpoints, ip, 1234);
        for (int i = 0; i < 5; i++) {
            assertSame(first, algo.getEndpoint(endpoints, ip, 1234));
        }
    }

    @Test
    public void testFallbackToAnotherEndpointIfUnhealthy() {
        String ip = "10.0.0.2";
        EndPoint first = algo.getEndpoint(endpoints, ip, 1234);
        // Simulate unhealthy
        setHealthy(first, false);
        EndPoint next = algo.getEndpoint(endpoints, ip, 1234);
        assertNotSame(first, next);
        assertTrue(next.isHealthy());
    }

    @Test
    public void testRoundRobinFallbackIfNoMapping() {
        String ip1 = "10.0.0.3";
        String ip2 = "10.0.0.4";
        EndPoint e1 = algo.getEndpoint(endpoints, ip1, 1234);
        EndPoint e2 = algo.getEndpoint(endpoints, ip2, 1234);
        assertNotNull(e1);
        assertNotNull(e2);
        // Should be different for different IPs (likely, but not guaranteed)
        // So just check both are in endpoints
        assertTrue(endpoints.contains(e1));
        assertTrue(endpoints.contains(e2));
    }

    @Test
    public void testDoesNotReturnUnhealthyEndpoints() {
        setHealthy(ep1, false);
        setHealthy(ep2, false);
        setHealthy(ep3, true);
        String ip = "10.0.0.5";
        EndPoint result = algo.getEndpoint(endpoints, ip, 1234);
        assertSame(ep3, result);
    }

    // Helper to set health using reflection (since setHealthy is package-private)
    private void setHealthy(EndPoint ep, boolean healthy) {
        try {
            java.lang.reflect.Method m = EndPoint.class.getDeclaredMethod("setHealthy", boolean.class);
            m.setAccessible(true);
            m.invoke(ep, healthy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}