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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.framework.Bundle;

public class BundlePropertiesDialog extends TitleAreaDialog {

	private final Bundle bundle;

	public BundlePropertiesDialog(Shell parentShell, Bundle bundle) {
		super(parentShell);
		this.bundle = bundle;
	}

	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		setTitle("Bundle Properties");

		Table table = new Table(dialogArea, SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn col;

		col = new TableColumn(table, SWT.NONE);
		col.setWidth(300);
		col.setText("Name");

		col = new TableColumn(table, SWT.NONE);
		col.setWidth(400);
		col.setText("Value");

		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new DictionaryContentProvider());
		viewer.setLabelProvider(new DictionaryEntryTableLabelProvider());
		viewer.setInput(bundle.getHeaders());

		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return dialogArea;
	}

}
