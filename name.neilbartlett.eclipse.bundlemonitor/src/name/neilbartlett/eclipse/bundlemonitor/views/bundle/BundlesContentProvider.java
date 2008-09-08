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
package name.neilbartlett.eclipse.bundlemonitor.views.bundle;

import java.util.HashMap;
import java.util.Map;

import name.neilbartlett.eclipse.bundlemonitor.internal.SWTConcurrencyUtils;
import name.neilbartlett.eclipse.bundlemonitor.internal.ViewerUpdater;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class BundlesContentProvider implements IStructuredContentProvider,
		BundleListener {

	private final StructuredViewer viewer;
	private final Map bundlesMap = new HashMap();
	private final BundleContext context;

	public BundlesContentProvider(StructuredViewer viewer, BundleContext context) {
		this.viewer = viewer;
		this.context = context;
	}

	public void start() {
		// Start listening
		context.addBundleListener(this);

		// Scan existing bundles
		Bundle[] bundles = context.getBundles();
		synchronized (bundlesMap) {
			for (int i = 0; i < bundles.length; i++) {
				bundlesMap.put(new Long(bundles[i].getBundleId()), bundles[i]);
			}
		}

		// Refresh
		viewer.refresh();
	}

	public void stop() {
		// Stop listening
		context.removeBundleListener(this);

		// Clear bundle list
		synchronized (bundlesMap) {
			bundlesMap.clear();
		}

		// Refresh
		viewer.refresh();
	}

	public Object[] getElements(Object inputElement) {
		synchronized (bundlesMap) {
			return bundlesMap.values().toArray(new Object[bundlesMap.size()]);
		}
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void bundleChanged(BundleEvent event) {
		switch (event.getType()) {
		case BundleEvent.INSTALLED:
			addBundle(event.getBundle());
			break;
		case BundleEvent.UNINSTALLED:
			removeBundle(event.getBundle());
			break;
		default:
			SWTConcurrencyUtils.safeAsyncUpdate(viewer, new ViewerUpdater() {
				public void updateViewer(Viewer viewer) {
					viewer.refresh();
				}
			});
		}
	}

	protected void addBundle(Bundle bundle) {
		Bundle previous;
		synchronized (bundlesMap) {
			previous = (Bundle) bundlesMap.put(new Long(bundle.getBundleId()),
					bundle);
		}
		if (previous == null)
			SWTConcurrencyUtils.safeRefresh(viewer);
	}

	protected void removeBundle(Bundle bundle) {
		Bundle previous;
		synchronized (bundlesMap) {
			previous = (Bundle) bundlesMap
					.remove(new Long(bundle.getBundleId()));
		}

		if (previous != null)
			SWTConcurrencyUtils.safeRefresh(viewer);
	}

}
