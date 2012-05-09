package org.webcat.eclipse.importer.ui.actions;

import org.webcat.eclipse.importer.ui.ImporterUIPlugin;
import org.webcat.eclipse.importer.ui.dialogs.AmbiguousProjectToImportDialog;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

// -------------------------------------------------------------------------
/**
 *  The workbench action delegate that invokes the submission wizard.This action
 *  is used by the Import Project option in the Project in menu, as well as the
 *  import button in the main toolbar. (The import option in the project's context
 *  provided by the similiar class in the .popup.actions package
 *
 *  @author Ellen Boyd bellen08
 *  @version Feb 5, 2012
 */
public class ProjectImportAction implements IWorkbenchWindowActionDelegate
{

    /* The workbench window to which this action belongs. */
    private IWorkbenchWindow window;

    /**
     * Called when the workbench action is invoked.
     */
    public void run(IAction action)
    {
        //only need this
        ImporterUIPlugin.getDefault().spawnImporterUI(window.getShell(),
            null);
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(IWorkbenchWindow window)
    {
        this.window = window;
    }

}
