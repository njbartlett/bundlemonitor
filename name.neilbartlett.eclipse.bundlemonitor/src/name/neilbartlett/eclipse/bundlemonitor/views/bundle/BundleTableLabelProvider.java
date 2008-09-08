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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class BundleTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Bundle bundle = (Bundle) element;
		String label;

		switch (columnIndex) {
		case 0:
			label = Long.toString(bundle.getBundleId());
			break;
		case 1:
			label = stateName(bundle.getState());
			break;
		case 2:
			label = bundle.getSymbolicName();
			break;
		case 3:
			label = bundle.getLocation();
			break;
		default:
			label = "";
		}

		return label;
	}

	protected String stateName(int i) {
		String name;

		switch (i) {
		case Bundle.ACTIVE:
			name = "ACTIVE";
			break;
		case Bundle.INSTALLED:
			name = "INSTALLED";
			break;
		case Bundle.RESOLVED:
			name = "RESOLVED";
			break;
		case Bundle.STARTING:
			name = "STARTING";
			break;
		case Bundle.STOPPING:
			name = "STOPPING";
			break;
		default:
			name = "<<unknown>>";
			break;
		}

		return name;
	}

}
