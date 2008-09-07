package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.viewers.IElementComparer;
import org.osgi.service.cm.Configuration;

/**
 * Required because the .equals() method of ConfigurationImpl in Equinox's Config
 * Admin implementation is broken.
 * @author Neil
 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=221473">Bug 221474</a>
 */
public class ConfigurationComparer implements IElementComparer {
	
	public boolean equals(Object a, Object b) {
		boolean result;
		
		if(a instanceof Configuration) {
			result = (b instanceof Configuration) && ((Configuration) a).getPid().equals(((Configuration) b).getPid());
		} else {
			result = a.equals(b);
		}
		
		return result;
	}

	public int hashCode(Object element) {
		return element.hashCode();
	}

}
