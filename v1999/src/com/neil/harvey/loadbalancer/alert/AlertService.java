package com.neil.harvey.loadbalancer.alert;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * Service interface for alerting on endpoint failures and recoveries.
 */
public interface AlertService {
	/**
	 * Alerts that the specified endpoint has failed.
	 */
	void alertFailure(final EndPoint endPoint);

	/**
	 * Alerts that the specified endpoint has recovered.
	 */
	void alertRecovery(final EndPoint endPoint);
}
