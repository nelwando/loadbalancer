package com.neil.harvey.loadbalancer.endpoint;

/**
 * Represents a backend endpoint with host, port, and health status.
 */
public class EndPoint {
	private final String host;
	private final int port;
	private volatile boolean healthy = true;
	private volatile int activeConnections = 0;

	public EndPoint(final String newHost, final int newPort) {
		host = newHost;
		port = newPort;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public synchronized void incrementActiveConnections() {
		activeConnections++;
	}
	
	public synchronized void decrementActiveConnections() {
		if (activeConnections > 0) {
			activeConnections--;
		}
	}
	
	public synchronized int getActiveConnections() {
		return activeConnections;
	}

	public synchronized boolean isHealthy() {
		return healthy;
	}

	/**
	 * Sets the health status of the endpoint.
	 * 
	 * Deliberately package-private to prevent external modification.
	 * 
	 * @param newHealthy true if the endpoint is healthy, false otherwise
	 */
	synchronized void setHealthy(final boolean newHealthy) {
		healthy = newHealthy;
	}

	public String toString() {
		return host + ":" + port + " => active connections: " + activeConnections + ", healthy = " + healthy;
	}
}
