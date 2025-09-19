package com.neil.harvey.loadbalancer.algorithm.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestRoundRobinAlgorithm {
    private RoundRobinAlgorithm algo;
    private EndPoint ep1, ep2, ep3;
    private List<EndPoint> endpoints;

    @Before
    public void setUp() {
        algo = new RoundRobinAlgorithm();
        ep1 = new EndPoint("host1", 8080);
        ep2 = new EndPoint("host2", 8081);
        ep3 = new EndPoint("host3", 8082);
        endpoints = Arrays.asList(ep1, ep2, ep3);
        setHealthy(ep1, true);
        setHealthy(ep2, true);
        setHealthy(ep3, true);
    }

    @Test
    public void testReturnsNullForEmptyList() {
        assertNull(algo.getEndpoint(Collections.emptyList(), "host", 1234));
    }

    @Test
    public void testReturnsOnlyEndpoint() {
        List<EndPoint> single = Collections.singletonList(ep1);
        assertSame(ep1, algo.getEndpoint(single, "host", 1234));
    }

    @Test
    public void testCyclesThroughEndpoints() {
        // Should cycle ep2, ep3, ep1, ep2, ...
        List<EndPoint> order = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            order.add(algo.getEndpoint(endpoints, "host", 1234));
        }
        // The algorithm starts at rrIndex=0, so first call returns ep2, then ep3, then ep1, then repeats
        assertEquals(Arrays.asList(ep2, ep3, ep1, ep2, ep3, ep1), order);
    }

    @Test
    public void testSkipsUnhealthyEndpoints() {
        setHealthy(ep2, false);
        // Should skip ep2 and cycle ep3, ep1
        assertSame(ep3, algo.getEndpoint(endpoints, "host", 1234));
        assertSame(ep1, algo.getEndpoint(endpoints, "host", 1234));
        assertSame(ep3, algo.getEndpoint(endpoints, "host", 1234));
        assertSame(ep1, algo.getEndpoint(endpoints, "host", 1234));
    }

    @Test
    public void testReturnsNullIfAllUnhealthy() {
        setHealthy(ep1, false);
        setHealthy(ep2, false);
        setHealthy(ep3, false);
        assertNull(algo.getEndpoint(endpoints, "host", 1234));
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