package org.webcat.eclipse.importer.ui.wizards;

import org.webcat.eclipse.importer.Importer;
import org.eclipse.swt.SWT;
import org.webcat.eclipse.importer.ui.messages.Messages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.wizard.WizardPage;

public class ImporterSummaryPage
    extends WizardPage
{
    public static final int RESULT_OK = 0;
    public static final int RESULT_CANCELED = 1;
    public static final int RESULT_INCOMPLETE = 2;
    public static final int RESULT_ERROR = 3;

    public ImporterSummaryPage(Importer engine)
    {
        super("Import successful");
        setTitle("Summary page title");
        setDescription("SUMMARYPAGE_DESCRIPTION");
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite comp = new Composite(parent, SWT.NONE);
    }


    public boolean canFlipToNextPage()
    {
        return true;
    }



}
