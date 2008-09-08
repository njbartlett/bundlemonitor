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
import org.eclipse.jface.viewers.ViewerSorter;
import org.osgi.framework.Bundle;

public abstract class BundlesViewerSorter extends ViewerSorter {
	public int compare(Viewer viewer, Object e1, Object e2) {
		Bundle b1 = (Bundle) e1;
		Bundle b2 = (Bundle) e2;

		return compareBundles(b1, b2);
	}

	protected abstract int compareBundles(Bundle b1, Bundle b2);
}
