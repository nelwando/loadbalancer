package com.neil.harvey.loadbalancer.io;

import static org.junit.Assert.*;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestPipe {

    @Test
    public void testPipeCopiesData() throws Exception {
        byte[] inputData = "Hello, Pipe!".getBytes("UTF-8");
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        t.join();
        assertArrayEquals(inputData, out.toByteArray());
    }

    @Test
    public void testPipeClosesStreams() throws Exception {
        final boolean[] closed = {false, false};
        InputStream in = new ByteArrayInputStream(new byte[0]) {
            @Override public void close() throws IOException { closed[0] = true; super.close(); }
        };
        OutputStream out = new ByteArrayOutputStream() {
            @Override public void close() throws IOException { closed[1] = true; super.close(); }
        };
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        t.join();
        assertTrue(closed[0]);
        assertTrue(closed[1]);
    }

    @Test
    public void testPipeHandlesEmptyStream() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        t.join();
        assertEquals(0, out.size());
    }

    @Test
    public void testPipeHandlesOutputException() throws Exception {
        final CountDownLatch closedLatch = new CountDownLatch(1);
        InputStream in = new ByteArrayInputStream("fail".getBytes("UTF-8")) {
            @Override public void close() throws IOException { closedLatch.countDown(); super.close(); }
        };
        OutputStream out = new OutputStream() {
            @Override public void write(int b) throws IOException { throw new IOException("fail"); }
            @Override public void close() throws IOException { /* do nothing */ }
        };
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        t.join();
        // InputStream should be closed even if output fails
        assertTrue(closedLatch.await(1, TimeUnit.SECONDS));
    }
}