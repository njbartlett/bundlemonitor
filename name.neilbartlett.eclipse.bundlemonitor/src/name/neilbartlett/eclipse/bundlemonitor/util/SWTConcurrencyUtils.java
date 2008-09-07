package name.neilbartlett.eclipse.bundlemonitor.util;

import org.eclipse.jface.viewers.Viewer;

public class SWTConcurrencyUtils {
	public static void safeAsyncUpdate(final Viewer viewer,
			final ViewerUpdater updater) {
		if (!viewer.getControl().isDisposed()) {
			viewer.getControl().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!viewer.getControl().isDisposed()) {
						updater.updateViewer(viewer);
					}
				}
			});
		}
	}

	public static void safeRefresh(Viewer viewer) {
		safeAsyncUpdate(viewer, new ViewerUpdater() {
			public void updateViewer(Viewer viewer) {
				viewer.refresh();
			}
		});
	}
}
