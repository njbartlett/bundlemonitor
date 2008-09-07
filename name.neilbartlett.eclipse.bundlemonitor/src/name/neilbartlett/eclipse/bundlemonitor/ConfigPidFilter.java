package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.osgi.service.cm.Configuration;

public class ConfigPidFilter extends ViewerFilter {
	
	private String pid;

	public ConfigPidFilter() {
		this("");
	}
	
	public ConfigPidFilter(String pid) {
		this.pid = pid.toLowerCase();
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean result;
		
		if(element instanceof PropertyEntry) {
			result = true;
		} else if(element instanceof Configuration) {
			String pid = ((Configuration) element).getPid();
			result = pid.toLowerCase().indexOf(this.pid) > -1;
		} else if(element instanceof ConfigWrapper) {
			String pid = ((ConfigWrapper) element).getPid();
			result = pid.toLowerCase().indexOf(this.pid) > -1;
		} else {
			result = false;
		}
		
		return result;
	}
	
	public void setServiceName(String pid) {
		this.pid = pid.toLowerCase();
	}

}
