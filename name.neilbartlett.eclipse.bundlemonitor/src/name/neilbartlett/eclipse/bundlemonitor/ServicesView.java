package name.neilbartlett.eclipse.bundlemonitor;

import java.util.HashSet;
import java.util.Set;

import name.neilbartlett.eclipse.bundlemonitor.util.SWTConcurrencyUtils;
import name.neilbartlett.eclipse.bundlemonitor.util.ViewerUpdater;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.osgi.framework.AllServiceListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

public class ServicesView extends FilteredViewPart implements AllServiceListener {

	private TreeViewer viewer;
	
	private Set services = new HashSet();
	private ServiceNameFilter nameFilter;

	private BundleContext context;

	public void createMainControl(Composite parent) {
			/**** Create controls ****/
			context = Activator.getDefault().getBundleContext();
			
			Tree tree = new Tree(parent, SWT.FULL_SELECTION);
			tree.setLinesVisible(true);
			tree.setHeaderVisible(false);
			
			TreeColumn col;
			col = new TreeColumn(tree, SWT.NONE);
			col.setWidth(400);
			
			col = new TreeColumn(tree, SWT.NONE);
			col.setWidth(200);
			
			tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			
			viewer = new TreeViewer(tree);
			viewer.setContentProvider(new ServicesContentProvider());
			viewer.setLabelProvider(new ServiceLabelProvider());
			
			nameFilter = new ServiceNameFilter();
			viewer.addFilter(nameFilter);
			
			/**** Load content ****/
			context.addServiceListener(this);
			ServiceReference[] refs = null;
			try {
				refs = context.getAllServiceReferences(null, null);
			} catch (InvalidSyntaxException e) {
				// Shouldn't happen
				throw new RuntimeException("Invalid filter expression", e);
			}
			// Result is be null if no services exist... thanks OSGi!
			if(refs != null) {
				for (int i = 0; i < refs.length; i++) {
					services.add(refs[i]);
				}
			}
			viewer.setInput(services);
			
			/**** Layout ****/
			GridLayout layout = new GridLayout(1,false);
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			parent.setLayout(layout);
	}
	
	protected void updatedFilter(String filterString) {
		nameFilter.setServiceName(filterString);
		viewer.refresh();
	}
	
	public void dispose() {
		context.removeServiceListener(this);
		super.dispose();
	}

	public void serviceChanged(ServiceEvent event) {
		final ServiceReference reference = event.getServiceReference();
		ViewerUpdater updater = null;
		
		if(ServiceEvent.REGISTERED == event.getType()) {
			updater = new ViewerUpdater() {
				public void updateViewer(Viewer viewer) {
					boolean added = services.add(reference);
					if(added) ((TreeViewer) viewer).add(services, reference);
				}
			};
		} else if(ServiceEvent.UNREGISTERING == event.getType()) {
			updater = new ViewerUpdater() {
				public void updateViewer(Viewer viewer) {
					boolean removed = services.remove(reference);
					if(removed) ((TreeViewer) viewer).remove(reference);
				}
			};
		} else if(ServiceEvent.MODIFIED == event.getType()) {
			updater = new ViewerUpdater() {
				public void updateViewer(Viewer viewer) {
					((TreeViewer) viewer).refresh(reference);
				}
			};
		}
		
		if(updater != null) SWTConcurrencyUtils.safeAsyncUpdate(viewer, updater);
	}

}
