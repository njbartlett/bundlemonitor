package name.neilbartlett.eclipse.bundlemonitor;

import java.net.MalformedURLException;
import java.net.URL;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class InstallFromURLActionDelegate implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		BundleContext context = Activator.getDefault().getBundleContext();
		MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 0,
				"Problem(s) installing bundle(s)", null);

		InputDialog dialog = new InputDialog(view.getSite().getShell(),
				"Install Bundle", "Enter URL of bundle to install:", "",
				new IInputValidator() {
					public String isValid(String newText) {
						try {
							new URL(newText);
							return null;
						} catch (MalformedURLException e) {
							return "Invalid URL";
						}
					}
				});
		if (dialog.open() == Window.OK) {
			String url = dialog.getValue();
			try {
				context.installBundle(url);
			} catch (BundleException e) {
				status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
						"Error installing bundle " + url, e));
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
