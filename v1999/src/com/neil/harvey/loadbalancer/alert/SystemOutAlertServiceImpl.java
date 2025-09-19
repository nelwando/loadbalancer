package com.neil.harvey.loadbalancer.alert;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

/**
 * A simple implementation of AlertService that prints alerts to the console.
 * 
 * For simplicity, the alert is printed out to the console.
 * In a real-world scenario, this could be an email, SMS, or integration with an alerting system.
 */
public class SystemOutAlertServiceImpl implements AlertService {
	@Override
	public void alertRecovery(final EndPoint endPoint) {
		System.out.println("ALERT: EndPoint " + endPoint + " has recovered and is now healthy.");
	}

	@Override
	public void alertFailure(EndPoint endPoint) {
		System.out.println("ALERT: EndPoint " + endPoint + " has failed and is now unhealthy.");
	}
}
