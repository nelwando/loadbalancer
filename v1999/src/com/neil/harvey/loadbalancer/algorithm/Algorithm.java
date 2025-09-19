package com.neil.harvey.loadbalancer.algorithm;

import java.util.List;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Interface for load balancing algorithms.
 * 
 * Implementations should provide a method to select an appropriate backend
 * endpoint from a set of available endpoints based on the algorithm's logic.
 */
public interface Algorithm {
	/**
	 * Selects an appropriate backend endpoint from the provided set of endpoints.
	 * 
	 * @param endPoints The list of available backend endpoints
	 * @param host      The host of the incoming request (for algorithms that use
	 *                  client info)
	 * @param port      The port of the incoming request (for algorithms that use
	 *                  client info)
	 * @return The selected EndPoint to which the request should be forwarded
	 */
	EndPoint getEndpoint(final List<EndPoint> endPoints, final String host, final int port);
}
