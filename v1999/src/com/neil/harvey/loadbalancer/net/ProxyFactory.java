package com.neil.harvey.loadbalancer.net;

import java.net.Socket;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPointRegistry;
import com.neil.harvey.loadbalancer.metrics.MetricsService;

public class ProxyFactory {
	private final EndPointRegistry endPointRegistry;
	private final MetricsService metricsService;
	private final Algorithm algorithm;

	public ProxyFactory(//
			final EndPointRegistry newEndPointService, //
			final MetricsService newMetricsService, //
			final Algorithm newAlgorithm) {
		if (newEndPointService == null) {
			throw new IllegalArgumentException("endPointService cannot be null");
		}

		endPointRegistry = newEndPointService;

		if (newMetricsService == null) {
			throw new IllegalArgumentException("metricsService cannot be null");
		}

		metricsService = newMetricsService;

		if (newAlgorithm == null) {
			throw new IllegalArgumentException("algorithm cannot be null");
		}

		algorithm = newAlgorithm;
	}

	public Proxy createProxy(final Socket client) {
		return new Proxy(client, endPointRegistry, metricsService, algorithm);
	}
}
