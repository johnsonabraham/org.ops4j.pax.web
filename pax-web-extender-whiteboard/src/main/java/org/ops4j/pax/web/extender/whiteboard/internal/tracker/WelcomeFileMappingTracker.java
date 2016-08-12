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
package org.ops4j.pax.web.extender.whiteboard.internal.tracker;

import org.ops4j.pax.web.extender.whiteboard.WelcomeFileMapping;
import org.ops4j.pax.web.extender.whiteboard.internal.ExtenderContext;
import org.ops4j.pax.web.extender.whiteboard.internal.element.WelcomeFileWebElement;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Tracks {@link org.ops4j.pax.web.extender.whiteboard.WelcomeFileMapping}.
 *
 * @author Dmitry Sklyut
 * @since 0.7.0
 */
public class WelcomeFileMappingTracker extends
		AbstractTracker<WelcomeFileMapping, WelcomeFileWebElement> {

	/**
	 * Constructor.
	 *
	 * @param extenderContext extender context; cannot be null
	 * @param bundleContext   extender bundle context; cannot be null
	 */
	public WelcomeFileMappingTracker(final ExtenderContext extenderContext,
									 final BundleContext bundleContext) {
		super(extenderContext, bundleContext);
	}

	public static ServiceTracker<WelcomeFileMapping, WelcomeFileWebElement> createTracker(
			final ExtenderContext extenderContext,
			final BundleContext bundleContext) {
		return new WelcomeFileMappingTracker(extenderContext, bundleContext)
				.create(WelcomeFileMapping.class);
	}

	/**
	 * @see AbstractTracker#createWebElement(org.osgi.framework.ServiceReference
	 *, Object)
	 */
	@Override
	WelcomeFileWebElement createWebElement(
			final ServiceReference<WelcomeFileMapping> serviceReference,
			final WelcomeFileMapping published) {
		return new WelcomeFileWebElement(published);
	}
}