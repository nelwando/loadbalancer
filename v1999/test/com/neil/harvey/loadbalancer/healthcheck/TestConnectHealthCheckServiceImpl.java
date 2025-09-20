package com.neil.harvey.loadbalancer.healthcheck;

import static org.junit.Assert.*;

import java.net.ServerSocket;

import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestConnectHealthCheckServiceImpl {

    @Test
    public void testReachableEndpoint() throws Exception {
        // Start a server socket on a random port
        try (ServerSocket server = new ServerSocket(0)) {
            int port = server.getLocalPort();
            EndPoint ep = new EndPoint("127.0.0.1", port);
            ConnectHealthCheckServiceImpl checker = new ConnectHealthCheckServiceImpl();
            assertTrue("Should be reachable", checker.checkHealth(ep));
        }
    }

    @Test
    public void testUnreachableEndpoint() throws Exception {
        // Pick a port that is likely to be closed (use a random high port)
        int port = 65534;
        EndPoint ep = new EndPoint("127.0.0.1", port);
        ConnectHealthCheckServiceImpl checker = new ConnectHealthCheckServiceImpl();
        assertFalse("Should be unreachable", checker.checkHealth(ep));
    }

    @Test
    public void testInvalidHost() {
        EndPoint ep = new EndPoint("invalid.host.name", 80);
        ConnectHealthCheckServiceImpl checker = new ConnectHealthCheckServiceImpl();
        assertFalse("Should be unreachable due to invalid host", checker.checkHealth(ep));
    }
}