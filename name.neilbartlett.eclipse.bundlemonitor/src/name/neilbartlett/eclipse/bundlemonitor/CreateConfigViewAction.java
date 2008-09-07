package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class CreateConfigViewAction implements IViewActionDelegate {

	private IViewPart view;
	
	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		ServiceTracker cmTracker = new ServiceTracker(Activator.getDefault().getBundleContext(), ConfigurationAdmin.class.getName(), null);
		cmTracker.open();
		
		if(cmTracker.getService() == null) {
			MessageDialog.openError(view.getSite().getShell(), "Create Configuration", "Configuration Admin service is not available");
		} else {
			CreateConfigWizard wizard = new CreateConfigWizard(view.getSite().getPage(), cmTracker);
			
			WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
			dialog.open();
		}
		cmTracker.close();
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
