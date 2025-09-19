package com.neil.harvey.loadbalancer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.neil.harvey.loadbalancer.alert.TestSystemOutAlertServiceImpl;
import com.neil.harvey.loadbalancer.algorithm.TestAlgorithmFactory;
import com.neil.harvey.loadbalancer.algorithm.impl.TestIPHashAlgorithm;
import com.neil.harvey.loadbalancer.algorithm.impl.TestLeastUsedAlgorithm;
import com.neil.harvey.loadbalancer.algorithm.impl.TestRoundRobinAlgorithm;
import com.neil.harvey.loadbalancer.endpoint.TestEndPoint;
import com.neil.harvey.loadbalancer.endpoint.TestEndPointRegistryImpl;
import com.neil.harvey.loadbalancer.healthcheck.TestConnectHealthCheckImpl;
import com.neil.harvey.loadbalancer.io.TestPipe;
import com.neil.harvey.loadbalancer.metrics.TestSystemOutMetricsCollectorImpl;
import com.neil.harvey.loadbalancer.proxy.TestProxyFactory;

@RunWith(Suite.class)
@SuiteClasses({ //
		TestSystemOutAlertServiceImpl.class, //
		TestAlgorithmFactory.class, //
		TestIPHashAlgorithm.class, //
		TestLeastUsedAlgorithm.class, //
		TestRoundRobinAlgorithm.class, //
		TestEndPoint.class, //
		TestEndPointRegistryImpl.class, //
		TestConnectHealthCheckImpl.class, //
		TestPipe.class, //
		TestSystemOutMetricsCollectorImpl.class, //
		TestProxyFactory.class, //
		TestLauncher.class, //
})
public class AllTests {

}
