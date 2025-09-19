package com.neil.harvey.loadbalancer.alert;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neil.harvey.loadbalancer.endpoint.EndPoint;

public class TestSystemOutAlertServiceImpl {

	private final PrintStream originalOut = System.out;
	private ByteArrayOutputStream outContent;

	@Before
	public void setUpStreams() {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
	}

	@Test
	public void testAlertRecovery() {
		SystemOutAlertServiceImpl alertService = new SystemOutAlertServiceImpl();
		EndPoint ep = new EndPoint("host1", 1234);
		alertService.alertRecovery(ep);
		String output = outContent.toString().trim();
		assertTrue(output.contains("ALERT: EndPoint"));
		assertTrue(output.contains("has recovered and is now healthy"));
		assertTrue(output.contains("host1:1234"));
	}

	@Test
	public void testAlertFailure() {
		SystemOutAlertServiceImpl alertService = new SystemOutAlertServiceImpl();
		EndPoint ep = new EndPoint("host2", 5678);
		alertService.alertFailure(ep);
		String output = outContent.toString().trim();
		assertTrue(output.contains("ALERT: EndPoint"));
		assertTrue(output.contains("has failed and is now unhealthy"));
		assertTrue(output.contains("host2:5678"));
	}
}