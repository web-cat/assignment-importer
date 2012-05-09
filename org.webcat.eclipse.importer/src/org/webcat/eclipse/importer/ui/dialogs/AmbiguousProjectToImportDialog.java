package org.webcat.eclipse.importer.ui.dialogs;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import java.text.MessageFormat;
import org.webcat.eclipse.importer.ui.messages.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.dialogs.MessageDialog;

public class AmbiguousProjectToImportDialog extends MessageDialog
{
    /* The first project that is selected (the one in the Navigator). */
    private IProject project1;

    /* The second project that is selected (the one owning the active
     * editor). */
    private IProject project2;

    /* The project that the user has selected in the dialog. */
    private IProject selectedProject;

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the dialog. This method is protected because
     * pre-processing must occur before the superclass constructor can be called.
     * Use the static createWithProjects method to create a new dialog instead
     *
     * @param parentShell the shell that owns the dialog
     * @param dialogMessage the message to display in the dialog
     * @param project1 a project
     * @param project2 a project
     */
    protected AmbiguousProjectToImportDialog(Shell parentShell,String dialogMessage,
        IProject project1, IProject project2)
    {
        super(parentShell, Messages.AMBIGUOUSSELECTION_DIALOG_TITLE,
            null, dialogMessage, MessageDialog.INFORMATION, new String[] {Messages.AMBIGUOUSSELECTION_OK,
            Messages.AMBIGUOUSSELECTION_CANCEL},0);
        this.project1 = project1;
        this.project2 = project2;

    }

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the AmbiguousProjectToImportDialog class
     * .
     * @param parentShell the Shell that will own the dialog box
     * @param project1 the first project that is selected
     * @param project2 the second project that is selected
     * @return an instance of AmbiguousProjectToImportDialog
     */
    public static AmbiguousProjectToImportDialog createWithProjects(
        Shell parentShell, IProject project1, IProject project2)
    {
        String message = MessageFormat.format(
            Messages.AMBIGUOUSSELECTION_DIALOG_MESSAGE, project1.getName(),
            project2.getName());

        return new AmbiguousProjectToImportDialog(parentShell, message,
            project1, project2);

    }

    protected Control createCustomArea(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);

        Button radio1 = new Button(container, SWT.RADIO);
        radio1.setText(MessageFormat.format(
            Messages.AMBIGUOUSSELECTION_OPTION_1, project1.getName()));
        radio1.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                selectedProject = project1;
            }
        });

        Button radio2 = new Button(container, SWT.RADIO);
        radio2.setText(MessageFormat.format(
            Messages.AMBIGUOUSSELECTION_OPTION_2, project2.getName()));
    radio2.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e)
        {
            selectedProject = project2;
        }
    });

    return container;

    }

    // ----------------------------------------------------------
    /**
     * Gets the project that was selected by the user after the dialog is
     * dismissed.
     *
     * @return the project that was selected by the user
     */
    public IProject getSelectedProject()
    {
        return selectedProject;
    }



}




