/**
 * 
 */
package name.neilbartlett.eclipse.bundlemonitor;


class PropertyEntry {
	private final Object owner;
	private final String key;
	private final Object value;

	public PropertyEntry(Object owner, String key, Object value) {
		this.owner = owner;
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public Object getOwner() {
		return owner;
	}
}