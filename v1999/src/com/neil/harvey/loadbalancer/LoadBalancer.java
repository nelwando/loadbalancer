package com.neil.harvey.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.neil.harvey.loadbalancer.net.Proxy;
import com.neil.harvey.loadbalancer.net.ProxyFactory;
import com.neil.harvey.loadbalancer.net.NetUtil;

/**
 * A simple load balancer that listens for incoming connections and forwards
 * them to backend endpoints using a specified load balancing algorithm.
 * 
 */
public class LoadBalancer {
	final ProxyFactory proxyFactory;

	private final int listenPort;

	private volatile boolean running = false;

	private ServerSocket serverSocket;

	/**
	 * Constructs a LoadBalancer instance.
	 * 
	 * @param newListenPort   The port the load balancer should listen on (1-65535)
	 * @param newProxyFactory The factory to create Proxy instances for handling
	 *                        requests to endpoints
	 */
	public LoadBalancer(final int newListenPort, final ProxyFactory newProxyFactory) {
		if (newListenPort <= 0 || newListenPort > 65535) {
			throw new IllegalArgumentException("listenPort must be in the range 1-65535");
		}

		listenPort = newListenPort;

		if (newProxyFactory == null) {
			throw new IllegalArgumentException("proxyFactory cannot be null");
		}

		proxyFactory = newProxyFactory;
	}

	/**
	 * Starts the load balancer, listening for incoming connections and forwarding
	 * them to backend endpoints.
	 */
	public boolean start() {
		if (!running) {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					throw new IllegalStateException("LoadBalancer is already running");
				}

				serverSocket = new ServerSocket(listenPort);

				System.out.println("LoadBalancer listening on port " + listenPort);

				running = true;

				while (running) {
					// Wait for a client connection
					final Socket client = serverSocket.accept();
					final Proxy handler = proxyFactory.createProxy(client);
					/*
					 * This currently creates a new thread per connection. In a production system, a
					 * thread pool would be more appropriate.
					 */
					new Thread(handler).start();
				}
			} catch (IOException e) {
				System.out.println("IO Error: " + e.getMessage());
			} finally {
				NetUtil.close(serverSocket);
				running = false;
			}
		}

		return running;
	}

	/**
	 * Stops the load balancer
	 */
	public boolean stop() {
		running = false;

		synchronized (serverSocket) {
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("Error closing server socket: " + e.getMessage());
				}
			}
		}

		return true;
	}
}
