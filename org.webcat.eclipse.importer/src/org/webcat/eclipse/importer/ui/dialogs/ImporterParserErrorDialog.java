package org.webcat.eclipse.importer.ui.dialogs;

import org.webcat.eclipse.importer.ImporterTargetException;
import org.webcat.eclipse.importer.TargetParseError;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.webcat.eclipse.importer.ui.messages.Messages;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.webcat.eclipse.importer.TargetParseException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Text;


// -------------------------------------------------------------------------
/**
 *  Displays to the user any errors that occurred during the parsing of the
 *  importer definitions files.
 *
 *  @author Ellen Boyd bellen08
 *  @version Feb 5, 2012
 */
public class ImporterParserErrorDialog extends Dialog
{
    /* The label that displays the description of the error. */
    private Label summaryLabel;

    /* The text field that displays the error list or stack trace*/
    private Text errorField;

    /* The string containing the description of the error */
    private String summaryString;

    /* The String containing the error list or stack trace. */
    private String errorString;

    // ----------------------------------------------------------
    /**
     * Create a new instance of the error dialog with the specific parent
     * shell and getting its information from the given exception
     *
     * @param shell the shell that will parent this dialog
     * @param exception the exception described by the dialog.
     */
    public ImporterParserErrorDialog(Shell shell, Throwable exception)
    {
        super(shell);
        setShellStyle(getShellStyle() |SWT.RESIZE);

        if(exception instanceof TargetParseException)
        {
            setFromParseErrors(((TargetParseException)exception).getErrors());
        }
        else
        {
            setFromException(exception);
        }
    }

    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite)super.createDialogArea(parent);

        GridLayout gl = new GridLayout();
        gl.numColumns = 1;
        composite.setLayout(gl);

        Composite headerComp = new Composite(composite, SWT.NONE);
        gl = new GridLayout();
        gl.numColumns = 2;
        headerComp.setLayout(gl);

        Label imageLabel = new Label(headerComp, SWT.NONE);
        imageLabel.setSize(32,32);
        imageLabel.setImage(Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
        GridData gd = new GridData();
        gd.widthHint = 32;
        gd.heightHint = 32;
        gd.verticalAlignment = GridData.BEGINNING;
        imageLabel.setLayoutData(gd);

        summaryLabel = new Label(headerComp, SWT.WRAP);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = 368;
        summaryLabel.setLayoutData(gd);

        errorField = new Text(composite, SWT.BORDER | SWT.READ_ONLY|SWT.MULTI |
            SWT.H_SCROLL | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 400;
        gd.heightHint = 150;
        errorField.setLayoutData(gd);

        if(errorString != null)
        {
            errorField.setText(errorString);
        }

        if(summaryString != null)
        {
            summaryLabel.setText(summaryString);
        }

        getShell().setText(Messages.PARSERERROR_DIALOG_TITLE);
        return composite;

    }

    /**
     * Creates the main buttons for the dialog.
     */
    protected void createButtonsForButtonBar(Composite parent)
    {
        //create only an ok button
        createButton(parent, IDialogConstants.OK_ID,
            IDialogConstants.OK_LABEL,true);
    }

    /**
     * Initializes the dialog's text area with the specified array of parser
     * error objects.
     */
    private void setFromParseErrors(TargetParseError[] errors)
    {
        StringBuffer buffer = new StringBuffer();
        for(int i= 0; i < errors.length; i ++)
        {
            buffer.append(errors[i].toString());
            buffer.append('\n');

            errorString = buffer.toString();
            summaryString = Messages.PARSERERROR_ERROR_MESSAGE_MULTIPLE;
        }
    }

    private void setFromException(Throwable e)
    {
        Throwable exception = e;
        //change to ImportTargetException
        if (e instanceof ImporterTargetException)
        {
            exception = ((ImporterTargetException)e).getCause();
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(exception.toString());
        buffer.append("\n\n");
        StackTraceElement [] trace = exception.getStackTrace();
        for(int i = 0; i < trace.length; i ++)
        {
            buffer.append(trace[i].toString());
            buffer.append('\n');

        }
        errorString = buffer.toString();
        summaryString = Messages.PARSERERROR_ERROR_MESSAGE_SINGLE;

    }
}

