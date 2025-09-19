package com.neil.harvey.loadbalancer.proxy;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Test;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPointRegistry;
import com.neil.harvey.loadbalancer.metrics.MetricsCollector;
import com.neil.harvey.loadbalancer.net.Proxy;
import com.neil.harvey.loadbalancer.net.ProxyFactory;

public class TestProxyFactory {
    static class MockEndPointRegistry implements EndPointRegistry {
        @Override public java.util.List getHealthyEndPoints() { return null; }
        @Override public boolean register(com.neil.harvey.loadbalancer.endpoint.EndPoint ep) { return false; }
        @Override public boolean deregister(com.neil.harvey.loadbalancer.endpoint.EndPoint ep) { return false; }
        @Override public boolean notifyFailure(com.neil.harvey.loadbalancer.endpoint.EndPoint ep) { return false; }
		@Override
		public boolean start() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean stop() {
			// TODO Auto-generated method stub
			return false;
		}
    }
    static class MockMetricsCollector implements MetricsCollector {
        @Override public void recordRequest(com.neil.harvey.loadbalancer.endpoint.EndPoint ep, long time, boolean success) {}
        @Override public void recordResponseTime(com.neil.harvey.loadbalancer.endpoint.EndPoint ep, long time) {}
    }
    static class MockAlgorithm implements Algorithm {
        @Override public com.neil.harvey.loadbalancer.endpoint.EndPoint getEndpoint(java.util.List eps, String host, int port) { return null; }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEndPointRegistry() {
        new ProxyFactory(null, new MockMetricsCollector(), new MockAlgorithm());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullMetricsCollector() {
        new ProxyFactory(new MockEndPointRegistry(), null, new MockAlgorithm());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAlgorithm() {
        new ProxyFactory(new MockEndPointRegistry(), new MockMetricsCollector(), null);
    }

    @Test
    public void testCreateProxyReturnsProxy() {
        ProxyFactory factory = new ProxyFactory(new MockEndPointRegistry(), new MockMetricsCollector(), new MockAlgorithm());
        Socket client = new Socket();
        Proxy proxy = factory.createProxy(client);
        assertNotNull(proxy);
        assertTrue(proxy instanceof Proxy);
    }
}