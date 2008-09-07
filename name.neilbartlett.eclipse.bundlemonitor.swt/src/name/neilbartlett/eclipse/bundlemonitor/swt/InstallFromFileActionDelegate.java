package name.neilbartlett.eclipse.bundlemonitor.swt;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class InstallFromFileActionDelegate implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		BundleContext context = Activator.getDefault().getBundleContext();
		MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 0,
				"Problem(s) installing bundle(s)", null);

		FileDialog dialog = new FileDialog(view.getSite().getShell(), SWT.OPEN
				| SWT.MULTI);
		dialog.setFilterExtensions(new String[] { "*.jar" });
		if (dialog.open() != null) {
			String[] names = dialog.getFileNames();
			String path = dialog.getFilterPath();

			for (int i = 0; i < names.length; i++) {
				try {
					FileInputStream stream = new FileInputStream(path
							+ System.getProperty("file.separator") + names[i]);
					context.installBundle(names[i], stream);
				} catch (FileNotFoundException e) {
					status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							0, "Failed to open " + names[i], e));
				} catch (BundleException e) {
					status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							0, "Error installing bundle " + names[i], e));
				}
			}
		}

		if (!status.isOK()) {
			ErrorDialog.openError(view.getSite().getShell(), "Install Bundles",
					null, status);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
