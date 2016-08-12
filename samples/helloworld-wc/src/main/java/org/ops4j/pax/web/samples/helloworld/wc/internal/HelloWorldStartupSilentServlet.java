/* Copyright 2013 Guillaume Yziquel.
 *
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
package org.ops4j.pax.web.samples.helloworld.wc.internal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Hello World Servlet.
 *
 * @author Guillaume Yziquel
 * @since 4.0.0, September 25, 2013
 */
public class HelloWorldStartupSilentServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//CHECKSTYLE:OFF
	public static boolean isActive = false;
	//CHECKSTYLE:ON

	@Override
	public void init() throws ServletException {
		isActive = true;
	}

	@Override
	public void destroy() {
		isActive = false;
		super.destroy();
	}
}
