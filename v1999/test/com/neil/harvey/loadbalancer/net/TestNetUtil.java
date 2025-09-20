package com.neil.harvey.loadbalancer.net;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

public class TestNetUtil {

	@Test
	public void testCloseSocket_NonNullOpenSocket() throws IOException {
		Socket socket = new Socket();
		assertFalse(socket.isClosed());
		NetUtil.close(socket);
		assertTrue(socket.isClosed());
	}

	@Test
	public void testCloseSocket_NullSocket() {
		try {
			NetUtil.close((Socket) null);
		} catch (Exception e) {
			fail("Should not throw when closing null Socket");
		}
	}

	@Test
	public void testCloseSocket_AlreadyClosedSocket() throws IOException {
		Socket socket = new Socket();
		socket.close();
		assertTrue(socket.isClosed());
		try {
			NetUtil.close(socket);
		} catch (Exception e) {
			fail("Should not throw when closing already closed Socket");
		}
	}

	@Test
	public void testCloseServerSocket_NonNullOpenServerSocket() throws IOException {
		ServerSocket serverSocket = new ServerSocket(0);
		assertFalse(serverSocket.isClosed());
		NetUtil.close(serverSocket);
		assertTrue(serverSocket.isClosed());
	}

	@Test
	public void testCloseServerSocket_NullServerSocket() {
		try {
			NetUtil.close((ServerSocket) null);
		} catch (Exception e) {
			fail("Should not throw when closing null ServerSocket");
		}
	}

	@Test
	public void testCloseServerSocket_AlreadyClosedServerSocket() throws IOException {
		ServerSocket serverSocket = new ServerSocket(0);
		serverSocket.close();
		assertTrue(serverSocket.isClosed());
		try {
			NetUtil.close(serverSocket);
		} catch (Exception e) {
			fail("Should not throw when closing already closed ServerSocket");
		}
	}
}