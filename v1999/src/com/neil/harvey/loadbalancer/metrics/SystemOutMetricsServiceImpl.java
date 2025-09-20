package com.neil.harvey.loadbalancer.metrics;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Implementation of MetricsCollector that outputs metrics to the system console.
 * 
 * In reality this would link to a proper metrics collection system. 
 */
public class SystemOutMetricsServiceImpl implements MetricsService {

	@Override
	public void recordRequest(final EndPoint endPoint, final long responseTimeMillis, final boolean success) {
		System.out.println("Recorded request to " + endPoint + " with response time " + responseTimeMillis + "ms, success: " + success);
	}

	@Override
	public void recordResponseTime(final EndPoint endPoint, final long responseTimeMillis) {
		System.out.println("Recorded response time for " + endPoint + ": " + responseTimeMillis + "ms");
	}
}
