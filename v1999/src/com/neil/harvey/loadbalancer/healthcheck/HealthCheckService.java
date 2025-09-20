package com.neil.harvey.loadbalancer.healthcheck;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Service interface for performing health checks on endpoints.
 */
public interface HealthCheckService {
	/**
	 * Checks the health of the specified endpoint.
	 * 
	 * @param endPoint The endpoint to check
	 * @return true if the endpoint is healthy, false otherwise
	 */
	boolean checkHealth(final EndPoint endPoint);
}
