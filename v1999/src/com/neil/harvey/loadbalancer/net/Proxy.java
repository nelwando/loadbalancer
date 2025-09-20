package com.neil.harvey.loadbalancer.net;

import java.io.IOException;
import java.net.Socket;

import com.neil.harvey.loadbalancer.algorithm.Algorithm;
import com.neil.harvey.loadbalancer.endpoint.EndPoint;
import com.neil.harvey.loadbalancer.endpoint.EndPointRegistry;
import com.neil.harvey.loadbalancer.io.Pipe;
import com.neil.harvey.loadbalancer.metrics.MetricsService;

/**
 * Handles a client connection by selecting a backend endpoint and piping data
 * between the client and backend.
 */
public class Proxy implements Runnable {
	private final Socket client;
	private final EndPointRegistry endPointRegistry;
	private final MetricsService metricsService;
	private final Algorithm algorithm;

	/**
	 * Constructs a Proxy instance.
	 * 
	 * @param newClient          The client socket connection
	 * @param newEndPointRegistry The registry managing backend endpoints
	 * @param newAlgorithm       The load balancing algorithm to use
	 */
	public Proxy(final Socket newClient, //
			final EndPointRegistry newEndPointRegistry, //
			final MetricsService newMetricsService, //
			final Algorithm newAlgorithm) {
		if (newClient == null) {
			throw new IllegalArgumentException("client cannot be null");
		}

		client = newClient;

		if (newEndPointRegistry == null) {
			throw new IllegalArgumentException("endPointService cannot be null");
		}

		endPointRegistry = newEndPointRegistry;

		if (newMetricsService == null) {
			throw new IllegalArgumentException("metricsService cannot be null");
		}

		metricsService = newMetricsService;

		if (newAlgorithm == null) {
			throw new IllegalArgumentException("algorithm cannot be null");
		}

		algorithm = newAlgorithm;
	}

	/**
	 * Runs the client handler, selecting a backend endpoint and piping data between
	 * the client and backend.
	 */
	public void run() {
		final long startTime = System.currentTimeMillis();

		final EndPoint endPoint = getEndPoint();

		if (endPoint == null) {
			// Exit as soon as possible as there are no available endpoints to improve
			// performance
			System.out.println("No healthy endpoints available to handle request from " + client.getInetAddress());

			try {
				client.close();
			} catch (IOException e) {
				System.out.println("Error closing client socket: " + e.getMessage());
			}

			metricsService.recordRequest(endPoint, System.currentTimeMillis() - startTime, false);
			return;
		}

		try {
			Socket backendSocket = new Socket(endPoint.getHost(), endPoint.getPort());
			endPoint.incrementActiveConnections();
			// note: calling client.getInputStream() multiple times may result in an
			// IOException so this is an area that could be improved in terms of retrying
			// the request
			Thread t1 = new Thread(new Pipe(client.getInputStream(), backendSocket.getOutputStream()));
			Thread t2 = new Thread(new Pipe(backendSocket.getInputStream(), client.getOutputStream()));
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			backendSocket.close();
			endPoint.decrementActiveConnections();
			client.close();

			metricsService.recordResponseTime(endPoint, System.currentTimeMillis() - startTime);
			metricsService.recordRequest(endPoint, System.currentTimeMillis() - startTime, true);
		} catch (Exception e) {
			metricsService.recordRequest(endPoint, System.currentTimeMillis() - startTime, false);
			endPointRegistry.notifyFailure(endPoint);
		} finally {
			try {
				client.close();
			} catch (IOException ex) {
				System.out.println("Error closing client socket: " + ex.getMessage());
			}
		}
	}

	private EndPoint getEndPoint() {
		if (endPointRegistry.getHealthyEndPoints().isEmpty()) {
			return null;
		}

		final EndPoint endPoint = algorithm.getEndpoint(endPointRegistry.getHealthyEndPoints(), //
				client.getInetAddress().getHostAddress(), //
				client.getPort()); //

		return endPoint;
	}
}
