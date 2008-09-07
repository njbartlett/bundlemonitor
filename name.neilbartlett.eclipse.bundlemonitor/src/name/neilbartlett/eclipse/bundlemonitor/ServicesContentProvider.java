package name.neilbartlett.eclipse.bundlemonitor;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.osgi.framework.ServiceReference;

public class ServicesContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		Object[] result;
		
		if(parentElement instanceof ServiceReference) {
			ServiceReference ref = (ServiceReference) parentElement;
			
			String[] keys = ref.getPropertyKeys();
			result = new Object[keys.length];
			
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				result[i] = new PropertyEntry(ref, key, ref.getProperty(key));
			}
		} else {
			result = new Object[0];
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

	public boolean hasChildren(Object element) {
		return element instanceof ServiceReference;
	}

	public Object[] getElements(Object inputElement) {
		Object[] result;
		if(inputElement instanceof Collection) {
			Collection collection = (Collection) inputElement;
			result = (Object[]) collection.toArray(new Object[collection.size()]);
		} else if(inputElement instanceof Object[]) {
			result = (Object[]) inputElement;
		} else {
			result = new Object[0];
		}
		return result;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
