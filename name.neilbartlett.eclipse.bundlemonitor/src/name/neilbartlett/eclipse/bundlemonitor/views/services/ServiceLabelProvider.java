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

import java.lang.reflect.Method;

import name.neilbartlett.eclipse.bundlemonitor.internal.Activator;
import name.neilbartlett.eclipse.bundlemonitor.views.shared.PropertyEntry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class ServiceLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final String LABEL_NULL = "<null>";
	private static final String LABEL_ERROR = "<error>";
	
	private Image imgObject;
	
	public ServiceLabelProvider() {
		ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/generic_element.gif");
		imgObject = desc.createImage();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		Image image = null;
		if(element instanceof ServiceReference && columnIndex == 0) {
			image = imgObject;
		}
		return image;
	}

	public String getColumnText(Object element, int columnIndex) {
		String label;
		
		if(element instanceof ServiceReference) {
			ServiceReference ref = (ServiceReference) element;
			if(columnIndex == 0) {
				String[] interfaces = (String[]) ref.getProperty(Constants.OBJECTCLASS);
				label = arrayToString(interfaces);
			} else {
				label = ref.getBundle().getSymbolicName();
			}
		} else if(element instanceof PropertyEntry) {
			PropertyEntry prop = (PropertyEntry) element;
			
			if(columnIndex == 0) {
				label = prop.getKey();
			} else if(columnIndex == 1) {
				Object value = prop.getValue();
				if(value == null) {
					label = LABEL_NULL;
				} else if(value instanceof Object[]) {
					label = arrayToString((Object[]) value);
				} else {
					label = value.toString();
				}
			} else {
				label = null;
			}
		} else {
			label = LABEL_ERROR;
		}
		
		return label;
	}
	
	protected static String arrayToString(Object[] array) {
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<array.length; i++) {
			if(i > 0) buffer.append(',');
			buffer.append(array[i] == null ? LABEL_NULL : array[i].toString());
		}
		
		return buffer.toString();
	}
	

	public void dispose() {
		super.dispose();
		try {
			Method disposeMethod = imgObject.getClass().getMethod("dispose", new Class[0]);
			disposeMethod.invoke(imgObject, new Object[0]);
		} catch (Exception e) {
			// Ignore
		}
	}

}
