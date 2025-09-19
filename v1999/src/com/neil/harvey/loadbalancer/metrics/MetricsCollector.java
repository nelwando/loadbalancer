package com.neil.harvey.loadbalancer.metrics;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Interface for collecting and recording metrics related to load balancing
 * operations.
 * 
 * In production, this would typically integrate with a metrics collection
 * system and could be used to understand the health of the system.
 */
public interface MetricsCollector {
	/**
	 * Records a request made to a backend endpoint.
	 * 
	 * @param endPoint           The backend endpoint
	 * @param responseTimeMillis The response time in milliseconds
	 * @param success            Whether the request was successful
	 */
	void recordRequest(EndPoint endPoint, long responseTimeMillis, boolean success);

	/**
	 * Records the response time for a backend endpoint.
	 * 
	 * @param endPoint           The backend endpoint
	 * @param responseTimeMillis The response time in milliseconds
	 */
	void recordResponseTime(EndPoint endPoint, long responseTimeMillis);
}
