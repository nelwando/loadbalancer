package com.neil.harvey.loadbalancer.healthcheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * HealthChecker implementation that uses a simple TCP socket connection to
 * check if the endpoint is reachable.
 */
public class ConnectHealthCheckImpl implements HealthCheck {
	@Override
	public boolean checkHealth(EndPoint endPoint) {
		try {
			Socket s = new Socket();
			s.connect(new InetSocketAddress(endPoint.getHost(), endPoint.getPort()), 1000);
			s.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
