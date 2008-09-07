package name.neilbartlett.eclipse.bundlemonitor;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class CreateConfigWizard extends Wizard {

	private final IWorkbenchPage workbenchPage;
	private CreateConfigWizardPage configPage;
	private final ServiceTracker cmTracker;
	
	public CreateConfigWizard(IWorkbenchPage workbenchPage, ServiceTracker cmTracker) {
		this.workbenchPage = workbenchPage;
		this.cmTracker = cmTracker;
	}

	public boolean performFinish() {
		BundleContext context = Activator.getDefault().getBundleContext();
		ConfigurationAdmin cm = (ConfigurationAdmin) cmTracker.getService();
		
		if(cm != null) {
			try {
				String pid = configPage.getPid();
				
				Configuration config;
				if(configPage.isFactory()) {
					config = cm.createFactoryConfiguration(pid, null);
				} else {
					config = cm.getConfiguration(pid);
				}

				ConfigurationPidEditorInput configInput = new ConfigurationPidEditorInput(config.getPid());
				workbenchPage.openEditor(configInput, ConfigurationEditor.EDITOR_ID);
				
				return true;
			} catch (IOException e) {
				ErrorDialog.openError(getShell(), "Error", null, new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Error loading or creating configuration", e));
				return false;
			} catch (PartInitException e) {
				ErrorDialog.openError(getShell(), "Error", null, e.getStatus());
			}
		}
		
		MessageDialog.openError(getShell(), "Error", "Configuration admin is not available");
		return false;
	}
	
	public void addPages() {
		configPage = new CreateConfigWizardPage();
		
		addPage(configPage);
	}
	

}
