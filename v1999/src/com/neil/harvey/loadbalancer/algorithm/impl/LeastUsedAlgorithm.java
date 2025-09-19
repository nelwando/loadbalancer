package com.neil.harvey.loadbalancer.algorithm.impl;

import java.util.List;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Implementation of a least-used load balancing algorithm that selects the
 * backend endpoint with the fewest active connections.
 */
public class LeastUsedAlgorithm implements Algorithm {

	@Override
	public EndPoint getEndpoint(List<EndPoint> endPoints, String host, int port) {
		if (endPoints.isEmpty()) {
			return null;
		}

		EndPoint best = (EndPoint) endPoints.get(0);

		for (int i = 1; i < endPoints.size(); i++) {
			EndPoint b = (EndPoint) endPoints.get(i);

			if (b.getActiveConnections() < best.getActiveConnections()) {
				best = b;
			}
		}
		
		return best;
	}
}
