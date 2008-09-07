package name.neilbartlett.eclipse.bundlemonitor;

import java.util.HashSet;
import java.util.Set;

import name.neilbartlett.eclipse.bundlemonitor.util.SWTConcurrencyUtils;
import name.neilbartlett.eclipse.bundlemonitor.util.ViewerUpdater;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.AllServiceListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

public class ServicesView extends ViewPart implements AllServiceListener {

	private TreeViewer viewer;
	
	private Set services = new HashSet();
	private ServiceNameFilter nameFilter;

	private BundleContext context;

	public void createPartControl(Composite parent) {
		try {
			context = Activator.getDefault().getBundleContext();
			Filter filter = FrameworkUtil.createFilter("(objectClass=*)");
			
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			
			new Label(composite, SWT.NONE).setText("Filter:");
			final Text txtFilter = new Text(composite, SWT.BORDER);
			txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			
			Tree tree = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
			tree.setLinesVisible(true);
			tree.setHeaderVisible(true);
			
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
			
			txtFilter.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					nameFilter.setServiceName(txtFilter.getText());
					viewer.refresh();
				}
			});
			
			// Initialize content
			context.addServiceListener(this);
			ServiceReference[] refs = context.getAllServiceReferences(null, null);
			for (int i = 0; i < refs.length; i++) {
				services.add(refs[i]);
			}
			viewer.setInput(services);
			
		} catch (InvalidSyntaxException e) {
			// Shouldn't happen
			new Label(parent, SWT.NONE).setText("Invalid filter");
		}
	}
	
	public void dispose() {
		context.removeServiceListener(this);
		super.dispose();
	}

	public void setFocus() {
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
