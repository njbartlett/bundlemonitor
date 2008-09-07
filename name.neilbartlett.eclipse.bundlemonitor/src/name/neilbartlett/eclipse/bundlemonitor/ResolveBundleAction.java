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

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

public class ResolveBundleAction implements IObjectActionDelegate {

	private IWorkbenchPart targetPart;
	private ISelection selection;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	public void run(IAction action) {
		BundleContext context = Activator.getDefault().getBundleContext();
		ServiceReference pkgAdminRef = context
				.getServiceReference(PackageAdmin.class.getName());

		if (pkgAdminRef == null) {
			// TODO
			return;
		}

		PackageAdmin pkgAdmin = (PackageAdmin) context.getService(pkgAdminRef);
		if (pkgAdmin == null) {
			// TODO
			return;
		}

		IStructuredSelection structSel = (IStructuredSelection) selection;
		Bundle[] bundles = new Bundle[structSel.size()];
		int i = 0;
		for (Iterator iterator = structSel.iterator(); iterator.hasNext(); i++) {
			bundles[i] = (Bundle) iterator.next();
		}

		boolean success = pkgAdmin.resolveBundles(bundles);

		if (!success) {
			MessageDialog.openWarning(targetPart.getSite().getShell(),
					"Warning", "One or more bundles failed to resolve");
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
