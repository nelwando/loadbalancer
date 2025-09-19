package com.neil.harvey.loadbalancer.algorithm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating Algorithm instances based on the specified algorithm name.
 */
public class AlgorithmFactory {
	private static final Map<String, Algorithm> algorithms = new ConcurrentHashMap<>();
		
	private AlgorithmFactory() {
		// Prevent instantiation
	}
	
	public static void registerAlgorithm(String name, Algorithm algorithm) {
		if (name == null || algorithm == null) {
			throw new IllegalArgumentException("Name and algorithm must not be null");
		}
		
		algorithms.put(name.toLowerCase(), algorithm);
	}
	
	public static Algorithm getAlgorithm(String algorithmName) {
		final Algorithm algorithm = algorithms.get(algorithmName == null ? "" : algorithmName.toLowerCase());
		
		if(algorithm != null) {
			return algorithm;
		}
		
		// Add other algorithms here as needed
		throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
	}
}
