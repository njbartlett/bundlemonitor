package name.neilbartlett.eclipse.bundlemonitor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public abstract class FilteredViewPart extends ViewPart {

	private Text txtFilter;
	private Action filterAction;
	
	private Composite stackPanel;
	private StackLayout stack;
	private Composite topPanel;
	private Composite mainPanel;


	public final void createPartControl(Composite parent) {
		// Create controls
		stackPanel = new Composite(parent, SWT.NONE);
		stack = new StackLayout();
		stackPanel.setLayout(stack);
		
		topPanel = new Composite(stackPanel, SWT.NONE);
		
		// Filter panel
		Composite filterPanel = new Composite(topPanel, SWT.NONE);
		new Label(filterPanel, SWT.NONE).setText("Filter:");
		txtFilter = new Text(filterPanel, SWT.SEARCH);

		// Main panel
		mainPanel = new Composite(stackPanel, SWT.NONE);
		createMainControl(mainPanel);

		// Add listeners
		txtFilter.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updatedFilter(txtFilter.getText());
			}
		});
		
		// Layout
		stack.topControl = mainPanel;
		
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		topPanel.setLayout(layout);

		filterPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		filterPanel.setLayout(new GridLayout(2, false));
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		mainPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		// Toolbar
		createActions();
		fillToolBar(getViewSite().getActionBars().getToolBarManager());
	}

	private void createActions() {
		filterAction = new FilterAction();
	}

	/**
	 * Fill the view toolbar. Subclasses may override but must call <code>super.fillToolBar</code>
	 * @param toolBar
	 */
	protected void fillToolBar(IToolBarManager toolBar) {
		toolBar.add(filterAction);
	}
	
	protected abstract void createMainControl(Composite container);
	
	protected abstract void updatedFilter(String filterString);
	
	private class FilterAction extends Action {
		public FilterAction() {
			super("Filter", IAction.AS_CHECK_BOX);
			setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/filter.gif"));
		}
		
		public void run() {
			if(filterAction.isChecked()) {
				stack.topControl = topPanel;
				mainPanel.setParent(topPanel);
				updatedFilter(txtFilter.getText());
			} else {
				stack.topControl = mainPanel;
				mainPanel.setParent(stackPanel);
				updatedFilter("");
			}
			stackPanel.layout(true, true);
			setFocus();
		}
	}
	
	public void setFocus() {
		if(filterAction.isChecked()) {
			txtFilter.setFocus();
		} else {
			doSetFocus();
		}
	}

	protected void doSetFocus() {
	}
	
}
