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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.osgi.framework.Bundle;

public class BundleSymbolicNameFilter extends ViewerFilter {

	private String filterString = "";

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Bundle bundle = (Bundle) element;
		
		return bundle.getSymbolicName().toLowerCase().indexOf(filterString) > -1;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString.toLowerCase();
	}
}
