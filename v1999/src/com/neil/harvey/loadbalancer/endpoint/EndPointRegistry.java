package com.neil.harvey.loadbalancer.endpoint;

import java.util.List;

/**
 * Service interface for managing backend endpoints, including health checks and failure notifications.
 */
public interface EndPointRegistry {
	/**
	 * Registers a new endpoint with the service.
	 *
	 * @param endPoint The endpoint to register
	 * @return true if the registration was successful, false otherwise
	 */
	boolean register(EndPoint endPoint);
	
	/**
	 * Deregisters an existing endpoint from the service.
	 * @param endPoint
	 * @return
	 */
	boolean deregister(EndPoint endPoint);
	
	/**
	 * Starts the endpoint service, including health checks.
	 * 
	 * @return true if the service started successfully, false otherwise
	 */
	boolean start();
	
	/**
	 * Stops the endpoint service.
	 * 
	 * @return true if the service stopped successfully, false otherwise
	 */
	boolean stop();
	
	/**
	 * Gets the current set of backend endpoints.
	 * 
	 * @return List of EndPoint objects
	 */
	List<EndPoint> getHealthyEndPoints();
	
	/**
	 * Notifies that the specified endpoint has failed.
	 * 
	 * @param endPoint The endpoint that has failed
	 * @return true if the notification was sent successfully, false otherwise
	 */
	boolean notifyFailure(final EndPoint endPoint);
}
