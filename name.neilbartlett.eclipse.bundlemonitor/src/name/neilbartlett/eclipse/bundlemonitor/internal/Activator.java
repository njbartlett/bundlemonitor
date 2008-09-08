/*
 * Copyright (c) 2008 Neil Bartlett
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Neil Bartlett - initial implementation
 */
package name.neilbartlett.eclipse.bundlemonitor.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Standard Eclipse-generated plug-in activator
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "name.neilbartlett.eclipse.bundlemonitor";
	
	private static Activator plugin;
	private BundleContext context;

	public void start(BundleContext context) throws Exception {
		this.context = context;
		super.start(context);
		plugin = this;
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static Activator getDefault() {
		return plugin;
	}
	
	public BundleContext getBundleContext() {
		return context;
	}
}
