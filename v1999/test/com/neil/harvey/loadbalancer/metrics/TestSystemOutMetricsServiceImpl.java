package com.neil.harvey.loadbalancer.metrics;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestSystemOutMetricsServiceImpl {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private SystemOutMetricsServiceImpl collector;
    private EndPoint ep;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        collector = new SystemOutMetricsServiceImpl();
        ep = new EndPoint("host", 1234);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testRecordRequestPrintsToSystemOut() {
        collector.recordRequest(ep, 42L, true);
        String output = outContent.toString();
        assertTrue(output.contains("Recorded request to"));
        assertTrue(output.contains("host"));
        assertTrue(output.contains("42ms"));
        assertTrue(output.contains("success: true"));
    }

    @Test
    public void testRecordResponseTimePrintsToSystemOut() {
        collector.recordResponseTime(ep, 99L);
        String output = outContent.toString();
        assertTrue(output.contains("Recorded response time for"));
        assertTrue(output.contains("host"));
        assertTrue(output.contains("99ms"));
    }
}