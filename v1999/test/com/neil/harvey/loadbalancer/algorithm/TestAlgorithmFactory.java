package com.neil.harvey.loadbalancer.algorithm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestAlgorithmFactory {

    private static class DummyAlgorithm implements Algorithm {
        private final String id;
        public DummyAlgorithm(String id) { this.id = id; }
        @Override
        public EndPoint getEndpoint(List<EndPoint> endPoints, String host, int port) {
            return endPoints == null || endPoints.isEmpty() ? null : endPoints.get(0);
        }
        @Override
        public String toString() { return id; }
    }

    @Before
    public void setUp() {
        // Clear and re-register for isolation if needed (not possible with current implementation)
        // So use unique names for each test
    }

    @Test
    public void testRegisterAndRetrieveAlgorithm() {
        Algorithm algo = new DummyAlgorithm("A1");
        AlgorithmFactory.registerAlgorithm("TestAlgo", algo);
        Algorithm retrieved = AlgorithmFactory.getAlgorithm("TestAlgo");
        assertSame(algo, retrieved);
    }

    @Test
    public void testRegisterAlgorithmCaseInsensitive() {
        Algorithm algo = new DummyAlgorithm("A2");
        AlgorithmFactory.registerAlgorithm("CaseAlgo", algo);
        Algorithm retrievedLower = AlgorithmFactory.getAlgorithm("casealgo");
        Algorithm retrievedUpper = AlgorithmFactory.getAlgorithm("CASEALGO");
        assertSame(algo, retrievedLower);
        assertSame(algo, retrievedUpper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownAlgorithmThrows() {
        AlgorithmFactory.getAlgorithm("NonExistentAlgo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullNameThrows() {
        AlgorithmFactory.registerAlgorithm(null, new DummyAlgorithm("A3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullAlgorithmThrows() {
        AlgorithmFactory.registerAlgorithm("NullAlgo", null);
    }

    @Test
    public void testGetAlgorithmWithNullNameReturnsException() {
        try {
            AlgorithmFactory.getAlgorithm(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}