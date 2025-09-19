package com.neil.harvey.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple test server that listens on a specified port and echoes received lines
 * prefixed with its identifier.
 */
public class TestServer {
	private final int port;
	private final String id;

	public TestServer(int port, String id) {
		this.port = port;
		this.id = id;
	}

	/**
	 * Start the server.
	 * @throws Exception If an error occurs.
	 */
	public void start() throws Exception {
		ServerSocket server = null; 
		
		try {
			server = new ServerSocket(port);
			
			System.out.println("Backend " + id + " listening on " + port);

			while (true) {
				final Socket client = server.accept();
				
				Thread t = new Thread(new Runnable() {
					public void run() {
						handle(client);
					}
				});
				
				t.start();
			}
		}
		finally {
			if(server != null) {
				server.close();
			}
		}		
	}

	/**
	 * Handle a client connection.
	 * @param client The client socket.
	 */
	private void handle(Socket client) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
			String line;
			
			while ((line = reader.readLine()) != null) {
				writer.println("[" + id + "] " + line);
			}
			
			client.close();
		} catch (Exception e) {
			System.out.println("Error handling client: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Main method to start the test server.
	 * @param args Command line arguments: <port> <id>
	 * @throws Exception If an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: TestServer <port> <id>");
			System.exit(1);
		}
		
		int port = Integer.parseInt(args[0]);
		String id = args[1];
		new TestServer(port, id).start();
	}
}
