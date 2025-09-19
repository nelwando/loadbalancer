package com.neil.harvey.loadbalancer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Pipes data from an InputStream to an OutputStream in a separate thread.
 */
public class Pipe implements Runnable {
	private final InputStream in;
	private final OutputStream out;

	public Pipe(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	public void run() {
		byte[] buf = new byte[1024];

		try {
			int read;

			while ((read = in.read(buf)) != -1) {
				out.write(buf, 0, read);
				out.flush();
			}
		} 
		catch (IOException e) 
		{
			// Silently ignore, happens when one side closes the connection
		} 
		finally {
			try {
				in.close();
			} 
			catch (IOException e) {
				System.out.println("Pipe IOException: " + e.getMessage());
			}
			
			try {
				out.close();
			} 
			catch (IOException e) {
				System.out.println("Pipe IOException: " + e.getMessage());
			}
		}
	}
}