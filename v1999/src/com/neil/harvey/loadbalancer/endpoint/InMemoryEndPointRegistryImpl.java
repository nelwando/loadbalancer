package com.neil.harvey.loadbalancer.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.neil.harvey.loadbalancer.alert.AlertService;
import com.neil.harvey.loadbalancer.healthcheck.HealthCheckService;

/**
 * Implementation of the EndPointRegistry interface for managing backend
 * endpoints, including health checks and failure notifications.
 */
public class InMemoryEndPointRegistryImpl implements EndPointRegistry {
	final AlertService alertService;
	final HealthCheckService healthCheckService;
	private final Vector<EndPoint> allEndPoints = new Vector<>(5);
	final int timeToLive; // in milliseconds
	private volatile boolean running = false;

	/**
	 * Constructs an EndPointRegistryImpl instance.
	 * 
	 * @param newAlertService The alert service for notifying about endpoint status
	 *                        changes
	 * @param newHealthCheck  The health check implementation to use
	 * @param newTimeToLive   The interval in milliseconds between health checks
	 *                        (must be greater than 0)
	 */
	public InMemoryEndPointRegistryImpl(final AlertService newAlertService, //
			final HealthCheckService newHealthCheckService, //
			final int newTimeToLive) { //
		if (newAlertService == null) {
			throw new IllegalArgumentException("alertService cannot be null");
		}

		alertService = newAlertService;

		if (newHealthCheckService == null) {
			throw new IllegalArgumentException("healthCheckService cannot be null");
		}

		healthCheckService = newHealthCheckService;

		if (newTimeToLive <= 0) {
			throw new IllegalArgumentException("timeToLive must be greater than zero");
		}

		timeToLive = newTimeToLive;
	}

	@Override
	public boolean start() {
		if (!running) {
			running = true;

			// Health check thread
			new Thread(new Runnable() {
				public void run() {
					while (running) {
						try {
							synchronized (allEndPoints) {
								for (int i = 0; i < allEndPoints.size(); i++) {
									final EndPoint endPoint = (EndPoint) allEndPoints.get(i);
									final boolean wasHealthy = endPoint.isHealthy();
									final boolean isHealthy = healthCheckService.checkHealth(endPoint);

									endPoint.setHealthy(isHealthy);

									if (wasHealthy != isHealthy) {
										if (isHealthy) {
											alertService.alertRecovery(endPoint);
										} else {
											alertService.alertFailure(endPoint);
										}
									}
								}
							}

							// Sleep for the specified timeToLive before next check
							Thread.sleep(timeToLive);
						} catch (InterruptedException e) {
							// ignore
						}
					}
				}
			}, "EndPointService - HealthChecker").start();
		}

		return running;
	}

	@Override
	public boolean register(final EndPoint endPoint) {
		if (endPoint == null) {
			throw new IllegalArgumentException("endPoint cannot be null");
		}

		synchronized (allEndPoints) {
			if (!allEndPoints.contains(endPoint)) {
				return allEndPoints.add(endPoint);
			}
		}

		return false;
	}

	@Override
	public boolean deregister(final EndPoint endPoint) {
		if (endPoint == null) {
			throw new IllegalArgumentException("endPoint cannot be null");
		}

		synchronized (allEndPoints) {
			return allEndPoints.remove(endPoint);
		}
	}

	@Override
	public boolean stop() {
		running = false;
		return true;
	}

	@Override
	public List<EndPoint> getHealthyEndPoints() {
		final List<EndPoint> healthyEndPoints = new ArrayList<>();

		synchronized (allEndPoints) {
			for (int i = 0; i < allEndPoints.size(); i++) {
				final EndPoint endPoint = (EndPoint) allEndPoints.get(i);

				if (endPoint.isHealthy()) {
					healthyEndPoints.add(endPoint);
				}
			}
		}

		return healthyEndPoints;
	}

	@Override
	public boolean notifyFailure(final EndPoint endPoint) {
		synchronized (allEndPoints) {
			if (allEndPoints.contains(endPoint)) {
				endPoint.setHealthy(false);
				alertService.alertFailure(endPoint);
				return true;
			}
		}

		return false;
	}
}
