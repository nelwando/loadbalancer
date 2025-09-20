package com.neil.harvey.loadbalancer.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Utility class for network operations.
 */
public class NetUtil {
	private NetUtil() {
		// Prevent instantiation
	}

	/**
	 * Closes the given socket thread safely if it is not null and not already
	 * closed.
	 * 
	 * @param socket The socket to close
	 */
	public static void close(final Socket socket) {
		if (socket != null) {
			synchronized (socket) {
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e) {
						System.out.println("Error closing socket: " + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Closes the given socket thread safely if it is not null and not already
	 * closed.
	 * 
	 * @param socket The socket to close
	 */
	public static void close(final ServerSocket socket) {
		if (socket != null) {
			synchronized (socket) {
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e) {
						System.out.println("Error closing server socket: " + e.getMessage());
					}
				}
			}
		}
	}
}
