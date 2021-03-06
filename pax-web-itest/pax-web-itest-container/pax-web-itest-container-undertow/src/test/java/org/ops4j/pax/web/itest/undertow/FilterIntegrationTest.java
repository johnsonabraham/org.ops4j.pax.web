/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.itest.undertow;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.web.itest.base.VersionUtil;
import org.ops4j.pax.web.itest.base.client.HttpTestClientFactory;
import org.ops4j.pax.web.service.WebContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.http.HttpContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * @author Achim Nierbeck
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class FilterIntegrationTest extends ITestBase {

	private static final Logger LOG = LoggerFactory.getLogger(FilterIntegrationTest.class);

	@Configuration
	public static Option[] configure() {
		return configureUndertow();
	}

	@Before
	public void setUp() throws BundleException, InterruptedException {
	}

	@After
	public void tearDown() throws BundleException {
	}


	@Test
	public void testSimpleFilter() throws Exception {
		ServiceTracker<WebContainer, WebContainer> tracker = new ServiceTracker<>(bundleContext, WebContainer.class, null);
		tracker.open();
		WebContainer service = tracker.waitForService(TimeUnit.SECONDS.toMillis(20));

		final String fullContent = "This content is Filtered by a javax.servlet.Filter";
		Filter filter = new Filter() {

			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
			}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
				PrintWriter writer = response.getWriter();
				writer.write(fullContent);
				writer.flush();
			}

			@Override
			public void destroy() {
			}
		};

		Dictionary<String, String> initParams = new Hashtable<>();

		HttpContext defaultHttpContext = service.createDefaultHttpContext();
		service.begin(defaultHttpContext);
		service.registerResources("/", "default", defaultHttpContext);

		service.registerFilter(filter, new String[]{"/testFilter/*",}, new String[]{"default",}, initParams, defaultHttpContext);

		service.end(defaultHttpContext);

		Thread.sleep(200);

		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain 'This content is Filtered by a javax.servlet.Filter'",
						resp -> resp.contains("This content is Filtered by a javax.servlet.Filter"))
				.doGETandExecuteTest("http://127.0.0.1:8181/testFilter/filter.me");

		service.unregisterFilter(filter);
	}

	@Test
	@Ignore
	public void testFilterWar() throws Exception {
		String bundlePath = WEB_BUNDLE
				+ "mvn:org.ops4j.pax.web.samples/simple-filter/"
				+ VersionUtil.getProjectVersion()
				+ "/war?"
				+ WEB_CONTEXT_PATH
				+ "=/web-filter";
		Bundle installWarBundle = installAndStartBundle(bundlePath);

		HttpTestClientFactory.createDefaultTestClient()
				.withResponseAssertion("Response must contain 'Filtered'",
						resp -> resp.contains("Filtered"))
				.doGETandExecuteTest("http://127.0.0.1:8181/web-filter/me.filter");

		installWarBundle.uninstall();

	}
}
