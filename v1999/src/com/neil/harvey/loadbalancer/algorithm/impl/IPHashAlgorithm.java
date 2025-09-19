package com.neil.harvey.loadbalancer.algorithm.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Implements IP Hash load balancing algorithm.
 * Maps client IP addresses to specific backend endpoints.
 * Falls back to Round Robin if no mapping exists or if the mapped endpoint is unhealthy.
 */
public class IPHashAlgorithm extends RoundRobinAlgorithm implements Algorithm {
	private Map<String, EndPoint> ipToEndPointMap = new ConcurrentHashMap<>();

	@Override
	public EndPoint getEndpoint(List<EndPoint> endPoints, String host, int port) {
		if (endPoints.isEmpty()) {
			return null;
		}

			if (ipToEndPointMap.containsKey(host)) {
			EndPoint cachedEndPoint = ipToEndPointMap.get(host);

			if (cachedEndPoint.isHealthy()) {
				return cachedEndPoint;
			} else {
				ipToEndPointMap.remove(host);
			}
		}

		// Fallback to Round Robin if no cached endpoint
		final EndPoint endPointFromRR = super.getEndpoint(endPoints, host, port);

		if (endPointFromRR != null) {
			ipToEndPointMap.put(host, endPointFromRR);
			return endPointFromRR;
		}

		return null;
	}

}
