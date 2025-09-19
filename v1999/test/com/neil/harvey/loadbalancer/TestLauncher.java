package com.neil.harvey.loadbalancer;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestLauncher {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testMainPrintsUsageOnTooFewArgs() throws Exception {
        Launcher.main(new String[] {"8080", "host:1234", "RoundRobin"});
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"));
    }

    @Test
    public void testMainPrintsUsageOnTooManyArgs() throws Exception {
        Launcher.main(new String[] {"8080", "host:1234", "RoundRobin", "1000", "extra"});
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"));
    }

    @Test(expected = NumberFormatException.class)
    public void testMainThrowsOnNonIntegerPort() throws Exception {
        Launcher.main(new String[] {"notaport", "host:1234", "RoundRobin", "1000"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainThrowsOnInvalidEndpoints() throws Exception {
        Launcher.main(new String[] {"8080", "badformat", "RoundRobin", "1000"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainThrowsOnInvalidAlgorithm() throws Exception {
        Launcher.main(new String[] {"8080", "host:1234", "NotARealAlgo", "1000"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainThrowsOnInvalidPortRange() throws Exception {
        Launcher.main(new String[] {"8080", "host:99999", "RoundRobin", "1000"});
    }

    @Test
    public void testParseEndPointsValid() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        List<EndPoint> eps = (List<EndPoint>) m.invoke(null, "host1:1234,host2:5678");
        assertEquals(2, eps.size());
        assertEquals("host1", eps.get(0).getHost());
        assertEquals(1234, eps.get(0).getPort());
        assertEquals("host2", eps.get(1).getHost());
        assertEquals(5678, eps.get(1).getPort());
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testParseEndPointsNull() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        m.invoke(null, (String) null);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testParseEndPointsEmpty() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        m.invoke(null, "");
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testParseEndPointsBadFormat() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        m.invoke(null, "host1-1234");
    }

    
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testParseEndPointsBadPort() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        m.invoke(null, "host1:notaport");
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testParseEndPointsPortOutOfRange() throws Exception {
        Method m = Launcher.class.getDeclaredMethod("parseEndPoints", String.class);
        m.setAccessible(true);
        m.invoke(null, "host1:99999");
    }

    @Ignore
    @Test
    public void testMainWithValidArgs() throws Exception {
        // This should not throw and should not print usage
        Launcher.main(new String[] {"8080", "host:1234", "RoundRobin", "1000"});
        String output = outContent.toString();
        assertFalse(output.contains("Usage:"));
    }
}