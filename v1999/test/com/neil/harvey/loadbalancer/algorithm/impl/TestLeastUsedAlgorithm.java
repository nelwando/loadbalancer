package com.neil.harvey.loadbalancer.algorithm.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestLeastUsedAlgorithm {
    private LeastUsedAlgorithm algo;
    private EndPoint ep1, ep2, ep3;
    private List<EndPoint> endpoints;

    @Before
    public void setUp() {
        algo = new LeastUsedAlgorithm();
        ep1 = new EndPoint("host1", 8080);
        ep2 = new EndPoint("host2", 8081);
        ep3 = new EndPoint("host3", 8082);
        endpoints = Arrays.asList(ep1, ep2, ep3);
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
    public void testReturnsEndpointWithFewestActiveConnections() {
        // ep1: 2, ep2: 1, ep3: 3
        setActiveConnections(ep1, 2);
        setActiveConnections(ep2, 1);
        setActiveConnections(ep3, 3);
        assertSame(ep2, algo.getEndpoint(endpoints, "host", 1234));
    }

    @Test
    public void testReturnsFirstEndpointOnTie() {
        setActiveConnections(ep1, 1);
        setActiveConnections(ep2, 1);
        setActiveConnections(ep3, 2);
        // Should return ep1 (first with min)
        assertSame(ep1, algo.getEndpoint(endpoints, "host", 1234));
    }

    private void setActiveConnections(EndPoint ep, int count) {
        // Reset to 0
        while (ep.getActiveConnections() > 0) ep.decrementActiveConnections();
        for (int i = 0; i < count; i++) ep.incrementActiveConnections();
    }
}