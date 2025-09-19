package com.neil.harvey.loadbalancer.algorithm.impl;

import java.util.List;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class RoundRobinAlgorithm implements Algorithm {
	private int rrIndex = 0;

	@Override
	public EndPoint getEndpoint(final List<EndPoint> endPoints, final String host, final int port) {
		if (endPoints.isEmpty()) {
			return null;
		}

		int size = endPoints.size();

		for (int i = 0; i < size; i++) {
			rrIndex = (rrIndex + 1) % size;

			EndPoint endPoint = (EndPoint) endPoints.toArray()[rrIndex];

			if (endPoint.isHealthy()) {
				return endPoint;
			}
		}

		return null;
	}
}
