package name.neilbartlett.eclipse.bundlemonitor;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class DeleteConfigViewAction implements IViewActionDelegate {

	private IViewPart view;
	private ConfigWrapper selected;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		try {
			selected.getConfiguration(((ConfigView) view).tracker).delete();
		} catch (IOException e) {
			MessageDialog.openError(view.getSite().getShell(), "Error", e.getLocalizedMessage());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		selected = null;
		if(selection != null && !selection.isEmpty()) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if(element instanceof ConfigWrapper) {
				selected = (ConfigWrapper) element;
			} else if(element instanceof PropertyEntry) {
				selected = (ConfigWrapper) ((PropertyEntry) element).getOwner();
			}
		}
		
		action.setEnabled(selected != null);	}

}
