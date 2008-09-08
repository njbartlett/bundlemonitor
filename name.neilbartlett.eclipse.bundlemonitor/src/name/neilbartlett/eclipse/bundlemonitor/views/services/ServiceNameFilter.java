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
package name.neilbartlett.eclipse.bundlemonitor.views.services;


import name.neilbartlett.eclipse.bundlemonitor.views.shared.PropertyEntry;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class ServiceNameFilter extends ViewerFilter {
	
	private String serviceName;

	public ServiceNameFilter() {
		this("");
	}
	
	public ServiceNameFilter(String serviceName) {
		this.serviceName = serviceName;
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean result;
		
		if(element instanceof PropertyEntry) {
			result = true;
		} else if(element instanceof ServiceReference) {
			String[] interfaces = (String[]) ((ServiceReference) element).getProperty(Constants.OBJECTCLASS);
			result = false;
			for (int i = 0; i < interfaces.length; i++) {
				if(interfaces[i].toLowerCase().indexOf(serviceName.toLowerCase()) > -1) {
					result = true;
					break;
				}
			} 
		} else {
			result = false;
		}
		
		return result;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
