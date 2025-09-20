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
import com.neil.harvey.loadbalancer.endpoint.TestInMemoryEndPointRegistryImpl;
import com.neil.harvey.loadbalancer.healthcheck.TestConnectHealthCheckServiceImpl;
import com.neil.harvey.loadbalancer.io.TestPipe;
import com.neil.harvey.loadbalancer.metrics.TestSystemOutMetricsServiceImpl;
import com.neil.harvey.loadbalancer.proxy.TestProxyFactory;

@RunWith(Suite.class)
@SuiteClasses({ //
		TestSystemOutAlertServiceImpl.class, //
		TestAlgorithmFactory.class, //
		TestIPHashAlgorithm.class, //
		TestLeastUsedAlgorithm.class, //
		TestRoundRobinAlgorithm.class, //
		TestEndPoint.class, //
		TestInMemoryEndPointRegistryImpl.class, //
		TestConnectHealthCheckServiceImpl.class, //
		TestPipe.class, //
		TestSystemOutMetricsServiceImpl.class, //
		TestProxyFactory.class, //
		TestLauncher.class, //
})
public class AllTests {

}
