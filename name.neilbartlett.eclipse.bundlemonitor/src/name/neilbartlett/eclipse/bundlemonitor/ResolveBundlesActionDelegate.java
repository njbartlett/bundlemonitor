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
package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

public class ResolveBundlesActionDelegate implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		BundleContext context = Activator.getDefault().getBundleContext();
		ServiceReference svcRef = context
				.getServiceReference(PackageAdmin.class.getName());

		if (svcRef == null) {
			MessageDialog.openError(view.getSite().getShell(), "Error",
					"Package Admin service is not available");
		}

		PackageAdmin pkgAdmin = (PackageAdmin) context.getService(svcRef);
		if (pkgAdmin == null) {
			MessageDialog.openError(view.getSite().getShell(), "Error",
					"Package Admin service is not available");
		}

		try {
			pkgAdmin.resolveBundles(null);
		} finally {
			context.ungetService(svcRef);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
