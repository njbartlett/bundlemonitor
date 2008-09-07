package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ConfigurationPidEditorInput implements IEditorInput {
	
	private final String pid;
	
	public ConfigurationPidEditorInput(String pid) {
		this.pid = pid;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return pid;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return "Configuration";
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getPid() {
		return pid;
	}
	
	public boolean equals(Object obj) {
		if(obj.getClass() == this.getClass()) {
			return this.pid.equals(((ConfigurationPidEditorInput) obj).pid);
		}
		return false;
	}
	
	public int hashCode() {
		return pid.hashCode();
	}

}
