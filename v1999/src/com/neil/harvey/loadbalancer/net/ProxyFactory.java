package com.neil.harvey.loadbalancer.net;

import java.net.Socket;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPointRegistry;
import com.neil.harvey.loadbalancer.metrics.MetricsCollector;

public class ProxyFactory {
	private final EndPointRegistry endPointService;
	private final MetricsCollector metricsCollector;
	private final Algorithm algorithm;

	public ProxyFactory(//
			final EndPointRegistry newEndPointService, //
			final MetricsCollector newMetricsCollector, //
			final Algorithm newAlgorithm) {
		if (newEndPointService == null) {
			throw new IllegalArgumentException("endPointService cannot be null");
		}

		endPointService = newEndPointService;

		if (newMetricsCollector == null) {
			throw new IllegalArgumentException("metricsCollector cannot be null");
		}

		metricsCollector = newMetricsCollector;

		if (newAlgorithm == null) {
			throw new IllegalArgumentException("algorithm cannot be null");
		}

		algorithm = newAlgorithm;
	}

	public Proxy createProxy(final Socket client) {
		return new Proxy(client, endPointService, metricsCollector, algorithm);
	}
}
