package org.webcat.eclipse.importer.internal;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author bellen08
 *  @version Feb 20, 2012
 */
public class ImporterPreferencePage
    extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    // ----------------------------------------------------------
    /**
     * Create a new ImporterPreferencePage object.
     */
    public ImporterPreferencePage()
    {
        super(FieldEditorPreferencePage.GRID);
        //setPreferenceStore()
        setDescription("Please enter the URL provided by your instructor "
                    + "that contains the project definitions to be used by the "
                    + " project import plug-in in the field below.\n");
    }

    @Override
    public void init(IWorkbench workbench)
    {
        // Does nothing; required by the IWorkbenchPreferencePage

    }


    @Override
    // ----------------------------------------------------------
    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    protected void createFieldEditors()
    {
        final int FIELD_WIDTH = 40;
        addField(new StringFieldEditor("org..webcat.eclipse.importer.definitions.URL",
                        "&Project Import definition URL:",FIELD_WIDTH,getFieldEditorParent()));

        addField(new StringFieldEditor("net.sf.webcat.eclipse.importer.identification.defaultUsername",
            "Default &username:",FIELD_WIDTH,getFieldEditorParent()));

    }

}
