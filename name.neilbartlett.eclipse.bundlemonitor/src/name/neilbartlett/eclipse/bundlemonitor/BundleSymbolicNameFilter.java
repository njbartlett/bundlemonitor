package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.osgi.framework.Bundle;

public class BundleSymbolicNameFilter extends ViewerFilter {

	private String filterString = "";

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Bundle bundle = (Bundle) element;
		
		return bundle.getSymbolicName().toLowerCase().indexOf(filterString) > -1;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString.toLowerCase();
	}
	
	

}
