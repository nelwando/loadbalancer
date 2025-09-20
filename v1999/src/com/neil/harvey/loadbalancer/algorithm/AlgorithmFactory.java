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
	
	/**
	 * Registers a new algorithm with the factory.
	 * 
	 * @param name      The name of the algorithm (case-insensitive)
	 * @param algorithm The Algorithm instance to register
	 */
	public static void registerAlgorithm(String name, Algorithm algorithm) {
		if (name == null || algorithm == null) {
			throw new IllegalArgumentException("Name and algorithm must not be null");
		}
		
		algorithms.put(name.toLowerCase(), algorithm);
	}
	
	/**
	 * Retrieves an Algorithm instance based on the specified algorithm name.
	 * 
	 * @param algorithmName The name of the desired algorithm (case-insensitive)
	 * @return The corresponding Algorithm instance
	 * @throws IllegalArgumentException if the algorithm name is unknown
	 */
	public static Algorithm getAlgorithm(String algorithmName) {
		final Algorithm algorithm = algorithms.get(algorithmName == null ? "" : algorithmName.toLowerCase());
		
		if(algorithm != null) {
			return algorithm;
		}
		
		throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
	}
}
