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

import org.osgi.framework.Bundle;

public class BundleIdSorter extends BundlesViewerSorter {

	protected int compareBundles(Bundle b1, Bundle b2) {
		return new Long(b1.getBundleId()).compareTo(new Long(b2.getBundleId()));
	}

}
