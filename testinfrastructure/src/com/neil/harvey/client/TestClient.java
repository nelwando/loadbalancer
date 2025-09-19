package com.neil.harvey.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple test client that connects to a server, sends messages, and prints the responses.
 */
public class TestClient {
	/**
	 * Main method to run the test client.
	 * @param args Command line arguments (not used).
	 * @throws Exception If an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 9000;

		Socket s = new Socket(host, port);
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

		out.println("hello");
		System.out.println("Got: " + in.readLine());

		out.println("bye");
		System.out.println("Got: " + in.readLine());

		s.close();
	}
}
