package name.neilbartlett.eclipse.bundlemonitor;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class ConfigsContentProvider implements ITreeContentProvider {
	
	private static final Object[] EMPTY = new Object[0];
	private final ServiceTracker tracker;
	
	public ConfigsContentProvider(ServiceTracker tracker) {
		this.tracker = tracker;
	}
	
	public Object[] getElements(Object inputElement) {
		Object[] result;
		if(inputElement == null) {
			result = null;
		} else if(inputElement instanceof Object[]) {
			result = (Object[]) inputElement;
		} else if(inputElement instanceof Collection) {
			Collection c = (Collection) inputElement;
			result = c.toArray(new Object[c.size()]);
		} else {
			result = new Object[0];
		}
		return result;
	}

	public boolean hasChildren(Object element) {
		return element instanceof ConfigWrapper;
	}
	
	public Object[] getChildren(Object parentElement) {
		Object[] result;
		
		if(!(parentElement instanceof ConfigWrapper)) {
			result = EMPTY;
		} else {
			ConfigWrapper wrapper = (ConfigWrapper) parentElement;
			Configuration config = wrapper.getConfiguration(tracker);
			if(config == null) {
				result = EMPTY; 
			} else {
				Dictionary props = config.getProperties();
				
				result = new Object[props.size()];
				int i=0;
				for(Enumeration iter = props.keys(); iter.hasMoreElements(); i++) {
					String key = (String) iter.nextElement();
					result[i] = new PropertyEntry(wrapper, key, props.get(key));
				}
			}
		}
		return result;
	}

	public Object getParent(Object element) {
		Object result = null;
		if(element instanceof PropertyEntry) {
			result = ((PropertyEntry) element).getOwner();
		}
		return result;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
}
