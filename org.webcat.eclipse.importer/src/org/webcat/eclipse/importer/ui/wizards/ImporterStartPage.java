package org.webcat.eclipse.importer.ui.wizards;

import org.webcat.eclipse.importer.model.ProjectTarget;
import org.webcat.eclipse.importer.model.ImportTarget;
import org.webcat.eclipse.importer.ui.ImporterUIPlugin;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.webcat.eclipse.importer.ui.messages.Messages;
import org.eclipse.jface.viewers.TreeViewer;
import org.webcat.eclipse.importer.Importer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.wizard.WizardPage;

// -------------------------------------------------------------------------
/**
 *  The main page of the import wizard that contains the assignment tree and
 *  other user-input fields.
 *
 *  Class WizardPage:
 *       extends DialogPage (a) (org.eclipse.jface.dialogs.DialogPage )
 *
 *
 *       implements: IWizardPage;
 *  @author bellen08
 *  @version Feb 2, 2012
 */
public class ImporterStartPage
    extends WizardPage
{
    private Importer importer;
    private TreeViewer assignmentTree;

    // ----------------------------------------------------------
    /**
     * Creates an instance of the main wizard page.
     *
     * @param importer the Importer to use to submit
     */
    public ImporterStartPage(Importer importer)
    {
        super(Messages.STARTPAGE_PAGE_NAME);
        setTitle(Messages.STARTPAGE_PAGE_TITLE);
        setDescription(Messages.STARTPAGE_PAGE_DESCRIPTION);

        this.importer = importer;
    }

    // ----------------------------------------------------------
    /**
     * Gets the project currently selected in the tree.
     *
     * @return the IDefinitionObject representing the currently selected
     * project.
     */
    public ProjectTarget getSelectedProject()
    {
        IStructuredSelection sel = (IStructuredSelection)assignmentTree.getSelection();
        if (sel.getFirstElement() instanceof ProjectTarget)
        {
            return (ProjectTarget)sel.getFirstElement();
        }
        else
        {
            return null;
        }

    }

    @Override
    public void createControl(Composite parent)
    {               IRunnableContext context = getContainer();

    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    composite.setLayout(gl);

    Label submitLabel = new Label(composite, SWT.NONE);
    submitLabel.setText(Messages.STARTPAGE_SUBMIT_AS);
    GridData gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
    submitLabel.setLayoutData(gd);

    assignmentTree = new TreeViewer(composite,
            SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    assignmentTree.setContentProvider(
            new ImportTargetsContentProvider());
    assignmentTree.setLabelProvider(new ImporterTargetLabelProvider());
    assignmentTree.setInput(importer.getRoot());

    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.heightHint = 150;
    assignmentTree.getControl().setLayoutData(gd);
    assignmentTree
            .addSelectionChangedListener(new ISelectionChangedListener() {
                public void selectionChanged(SelectionChangedEvent e)
                {
                    assignmentTreeSelectionChanged();
                }
            });

    setControl(composite);





    expandAllLocalGroups(importer.getRoot(), context);
    selectLastSelectedAssignmentInTree();
    initializationComplete = true;

    updatePageComplete();
    }





    private void updatePageComplete()
    {
            setPageComplete(true);
    }

 // ----------------------------------------------------------
    private void assignmentTreeSelectionChanged()
    {
        updatePageComplete();
    }

    // ----------------------------------------------------------
    private void expandAllLocalGroups(ImportTarget obj,
                                      IRunnableContext context)
    {
        ImportTarget[] children = obj.getLogicalChildren();

        for(int i = 0; i < children.length; i++)
        {
            ImportTarget child = children[i];

            if(child.isLoaded())
            {
                if(assignmentTree.isExpandable(child))
                {
                    assignmentTree.setExpandedState(child, true);
                    expandAllLocalGroups(child, context);
                }
            }
        }
    }

    /* Set to false while control initialization occurs so that an error
       message will not be displayed in the wizard until actual user input
       occurs, as per Eclipse user interface guidelines. */
    private boolean initializationComplete = false;


 // ----------------------------------------------------------
    private void selectLastSelectedAssignmentInTree()
    {
        String path =
            ImporterUIPlugin.getDefault().getLastSelectedAssignmentPath();

        Tree tree = assignmentTree.getTree();
        TreeItem item = null;

        if (path != null)
        {
            String[] components = path.split("/\\$#\\$/"); //$NON-NLS-1$
            TreeItem[] children = tree.getItems();

            for (String component : components)
            {
                item = findTreeItemWithText(component, children);

                if (item == null)
                {
                    return;
                }
                else
                {
                    item.setExpanded(true);
                    children = item.getItems();
                }
            }

            if (item != null)
            {
                tree.select(item);
            }
        }
    }

    // ----------------------------------------------------------
    private TreeItem findTreeItemWithText(String text, TreeItem[] items)
    {
        for (TreeItem item : items)
        {
            if (item.getText().equals(text))
            {
                return item;
            }
        }

        return null;
    }

    // ----------------------------------------------------------
    private void setErrorMessageIfInitialized(String msg)
    {
        if(initializationComplete)
            setErrorMessage(msg);
    }



}

