package com.neil.harvey.loadbalancer.endpoint;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.alert.AlertService;
import com.neil.harvey.loadbalancer.healthcheck.HealthCheckService;

public class TestInMemoryEndPointRegistryImpl {
    private static class MockAlertService implements AlertService {
        public List<EndPoint> failures = new ArrayList<>();
        public List<EndPoint> recoveries = new ArrayList<>();
        @Override public void alertFailure(EndPoint ep) { failures.add(ep); }
        @Override public void alertRecovery(EndPoint ep) { recoveries.add(ep); }
    }
    private static class MockHealthCheck implements HealthCheckService {
        private final Map<EndPoint, Boolean> health = new HashMap<>();
        public void setHealth(EndPoint ep, boolean isHealthy) { health.put(ep, isHealthy); }
        @Override public boolean checkHealth(EndPoint ep) {
            return health.getOrDefault(ep, true);
        }
    }

    private MockAlertService alertService;
    private MockHealthCheck healthCheck;
    private InMemoryEndPointRegistryImpl registry;
    private EndPoint ep1, ep2;

    @Before
    public void setUp() {
        alertService = new MockAlertService();
        healthCheck = new MockHealthCheck();
        registry = new InMemoryEndPointRegistryImpl(alertService, healthCheck, 1000);
        ep1 = new EndPoint("host1", 8080);
        ep2 = new EndPoint("host2", 8081);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAlertService() {
        new InMemoryEndPointRegistryImpl(null, healthCheck, 1000);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullHealthCheck() {
        new InMemoryEndPointRegistryImpl(alertService, null, 1000);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidTimeToLive() {
        new InMemoryEndPointRegistryImpl(alertService, healthCheck, 0);
    }

    @Test
    public void testRegisterAndDeregister() {
        assertTrue(registry.register(ep1));
        assertFalse(registry.register(ep1)); // duplicate
        assertTrue(registry.register(ep2));
        assertTrue(registry.deregister(ep1));
        assertFalse(registry.deregister(ep1)); // already removed
    }
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNull() {
        registry.register(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeregisterNull() {
        registry.deregister(null);
    }

    @Test
    public void testGetHealthyEndPoints() {
        registry.register(ep1);
        registry.register(ep2);
        setHealthy(ep1, true);
        setHealthy(ep2, false);
        List<EndPoint> healthy = registry.getHealthyEndPoints();
        assertTrue(healthy.contains(ep1));
        assertFalse(healthy.contains(ep2));
    }

    @Test
    public void testNotifyFailure() {
        registry.register(ep1);
        setHealthy(ep1, true);
        assertTrue(registry.notifyFailure(ep1));
        assertFalse(ep1.isHealthy());
        assertTrue(alertService.failures.contains(ep1));
    }
    @Test
    public void testNotifyFailureNotRegistered() {
        assertFalse(registry.notifyFailure(ep2));
        assertFalse(alertService.failures.contains(ep2));
    }

    @Test
    public void testStartAndStop() throws Exception {
        assertTrue(registry.start());
        assertTrue(registry.stop());
    }

    @Test
    public void testHealthCheckAndAlertIntegration() throws Exception {
        registry.register(ep1);
        setHealthy(ep1, true);
        healthCheck.setHealth(ep1, false); // will become unhealthy
        registry.start();
        Thread.sleep(150); // allow health check thread to run
        assertFalse(ep1.isHealthy());
        assertTrue(alertService.failures.contains(ep1));
        healthCheck.setHealth(ep1, true); // will recover
        Thread.sleep(1500); // allow health check thread to run
        assertTrue(ep1.isHealthy());
        assertTrue(alertService.recoveries.contains(ep1));
        registry.stop();
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