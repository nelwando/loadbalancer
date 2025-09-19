package com.neil.harvey.loadbalancer.healthcheck;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public interface HealthCheck {
	boolean checkHealth(final EndPoint endPoint);
}
