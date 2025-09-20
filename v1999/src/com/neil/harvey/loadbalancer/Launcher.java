package com.neil.harvey.loadbalancer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neil.harvey.loadbalancer.alert.AlertService;
import com.neil.harvey.loadbalancer.alert.SystemOutAlertServiceImpl;
import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.algorithm.AlgorithmFactory;
import com.neil.harvey.loadbalancer.algorithm.impl.IPHashAlgorithm;
import com.neil.harvey.loadbalancer.algorithm.impl.LeastUsedAlgorithm;
import com.neil.harvey.loadbalancer.algorithm.impl.RoundRobinAlgorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPoint;
import com.neil.harvey.loadbalancer.endpoint.EndPointRegistry;
import com.neil.harvey.loadbalancer.endpoint.InMemoryEndPointRegistryImpl;
import com.neil.harvey.loadbalancer.healthcheck.HealthCheckService;
import com.neil.harvey.loadbalancer.healthcheck.ConnectHealthCheckServiceImpl;
import com.neil.harvey.loadbalancer.metrics.MetricsService;
import com.neil.harvey.loadbalancer.metrics.SystemOutMetricsServiceImpl;
import com.neil.harvey.loadbalancer.net.ProxyFactory;

/**
 * Launcher class to start the load balancer.
 */
public class Launcher {
	private static final String ROUND_ROBIN = "RoundRobin";
	private static final String IP_HASH = "IPHash";
	private static final String LEAST_USED = "LeastUsed";

	static {
		// Enhancement: drive the registration of algorithms via configuration and
		// reflection
		AlgorithmFactory.registerAlgorithm(ROUND_ROBIN, new RoundRobinAlgorithm());
		AlgorithmFactory.registerAlgorithm(IP_HASH, new IPHashAlgorithm());
		AlgorithmFactory.registerAlgorithm(LEAST_USED, new LeastUsedAlgorithm());
	}

	/**
	 * Main method to start the load balancer.
	 * 
	 * @param args Command line arguments: <listenPort> <endpoints> <algorithm>
	 *             <timeToLive>
	 * @throws IOException If an I/O error occurs
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			System.out.println(
					"Usage: java -cp com.neil.harvey.loadbalancer.Launch <listenPort> <endpoints> <algorithm> <timeToLive>");
			System.out.println("  listenPort: The port the load balancer should listen on (1-65535)");
			System.out.println("  endpoints: Comma separated list of backend endpoints in the form host:port");
			System.out.println("  algorithm: Load balancing algorithm to use (RoundRobin, IPHash)");
			System.out.println("  timeToLive: Health check interval in milliseconds (greater than 0)");
			return;
		}

		final int listenPort = Integer.parseInt(args[0]);
		final String endpoints = args[1];
		final String algorithmName = args[2];
		final int timeToLive = Integer.parseInt(args[3]);

		// Enhancement: Initialization should be driven via configuration
		final AlertService alertService = new SystemOutAlertServiceImpl();
		final HealthCheckService healthChecker = new ConnectHealthCheckServiceImpl();
		final MetricsService metricsCollector = new SystemOutMetricsServiceImpl();
		final EndPointRegistry endPointRegistry = new InMemoryEndPointRegistryImpl(alertService, healthChecker, timeToLive);
		final List<EndPoint> endPointList = parseEndPoints(endpoints);
		for (final EndPoint endPoint : endPointList) {
			endPointRegistry.register(endPoint);
		}

		final Algorithm algorithm = AlgorithmFactory.getAlgorithm(algorithmName);
		final ProxyFactory clientHandlerFactory = new ProxyFactory(endPointRegistry, metricsCollector, algorithm);

		final LoadBalancer loadBalancer = new LoadBalancer(listenPort, clientHandlerFactory);

		try {
			endPointRegistry.start();
			loadBalancer.start();
		} finally {
			endPointRegistry.stop();
			loadBalancer.stop();
		}
	}

	/**
	 * Parses a comma-separated list of endpoints in the form host:port
	 * 
	 * @param endpoints Comma-separated list of endpoints
	 * @return List of EndPoint objects
	 */
	private static List<EndPoint> parseEndPoints(String endpoints) {
		if (endpoints == null || endpoints.trim().isEmpty()) {
			throw new IllegalArgumentException("endpoints cannot be null or empty");
		}

		final String[] parts = endpoints.split(",");
		final List<EndPoint> endPointList = new ArrayList<>();

		for (String part : parts) {
			final String[] hostPort = part.split(":");

			if (hostPort.length != 2) {
				throw new IllegalArgumentException("Invalid endpoint format: " + part);
			}

			final String host = hostPort[0].trim();
			final int port;

			try {
				port = Integer.parseInt(hostPort[1].trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid port number in endpoint: " + part);
			}

			if (port <= 0 || port > 65535) {
				throw new IllegalArgumentException("Port number must be in the range 1-65535 in endpoint: " + part);
			}

			endPointList.add(new EndPoint(host, port));
		}

		return endPointList;
	}
}
