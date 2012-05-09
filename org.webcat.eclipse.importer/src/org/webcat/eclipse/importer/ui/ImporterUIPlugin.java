package org.webcat.eclipse.importer.ui;

import java.io.InputStream;
import org.webcat.eclipse.importer.tests.TargetParsingTest;
import org.eclipse.jface.wizard.WizardDialog;
import org.webcat.eclipse.importer.ui.wizards.ImporterWizard;
import org.webcat.eclipse.importer.ui.dialogs.ImporterParserErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.webcat.eclipse.importer.Importer;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;



// -------------------------------------------------------------------------
/**
 *  The main plug-in class for the importer user interface plug-in.
 *
 *  @author bellen08
 *  @version Feb 4, 2012
 */
public class ImporterUIPlugin extends AbstractUIPlugin
{

    private ResourceBundle resourceBundle;
    private static ImporterUIPlugin plugin;

    /**
     * The unique identifier of the plug-in.
     */
    public static final String PLUGIN_ID = "org.webcat.eclipse.importer";

    private String lastSelectedAssignmentPath;

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the ImporterUIPlugin.
     */
    public ImporterUIPlugin()
    {
        super();
        plugin = this;
        try
        {
            resourceBundle = ResourceBundle.getBundle(PLUGIN_ID + ".ImporterUIPluginResources");
        }
        catch(MissingResourceException x)
        {
            resourceBundle = null;
        }
    }

    // ----------------------------------------------------------
    /**
     * Returns the shared instance of the plug-in.
     *
     * @return the shared instance of the of the plug-in.
     */
    public static ImporterUIPlugin getDefault()
    {
        return plugin;
    }

    // ----------------------------------------------------------
    /**
     * Returns the string from the plugin's resource bundle, or the key itself
     * if not found.
     *
     * @param key the key of the string to return
     * @return the string with the specified key, or the key itself if the
     *     string was not found
     */
    public static String getResourceString(String key)
    {
        ResourceBundle bundle = ImporterUIPlugin.getDefault()
                .getResourceBundle();
        try
        {
            return (bundle != null) ? bundle.getString(key) : key;
        }
        catch(MissingResourceException e)
        {
            return key;
        }
    }

    // ----------------------------------------------------------
    /**
     * Gets the plug-in's resource bundle.
     *
     * @return the plug-in's resource bundle
     */
    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }

    // ----------------------------------------------------------
    /**
     * Gets an image descriptor for the specified image in the plug-in's
     * "icons" directory.
     *
     * @param path the path to the icon that should be loaded, relative to
     *     the "icons" folder in the plug-in
     * @return an ImageDescriptor for the image
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        try
        {
            URL base = Platform.getBundle(PLUGIN_ID).getEntry(
                    "/icons/"); //$NON-NLS-1$
            URL url = new URL(base, path);

            return ImageDescriptor.createFromURL(url);
        }
        catch(MalformedURLException e)
        {
            // Do nothing.
        }

        return null;
    }

    // ----------------------------------------------------------
    /**
     * Gets the path to the most recently selected assignment in the
     * submission wizard.
     *
     * @return the path to the most recently selected assignment
     */
    public String getLastSelectedAssignmentPath()
    {
        return lastSelectedAssignmentPath;
    }

    // ----------------------------------------------------------
    /**
     * Spawns the user interface for the importer. The window that displays
     * the possible import-able projects from the project-target file.
     * @param shell
     * @param project
     */
    public void spawnImporterUI(Shell shell, IProject project)
    {
        URL url;
        //version of the importer engine
        Importer engine = new Importer();
        try
        {
            //url = new URL(SubmitterCore.getDefault().getOption(SubmitterCore.DEFINITIONS_URL));

            ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);

            //RunnableContextLongRunningTaskManager taskManager =
              //  new RunnableContextLongRunningTaskManager(dlg);
           // engine.setLongRunningTaskManager(taskManager);
            InputStream stream = TargetParsingTest.class.getResourceAsStream(
                "test-targets.xml");

            engine.readImportTargets(stream);

           // engine.setLongRunningTaskManager(null);
        }
        catch (Throwable e)
        {
            ImporterParserErrorDialog dlg = new ImporterParserErrorDialog(shell, e);
            dlg.open();
            return;
        }

        ImporterWizard wizard = new ImporterWizard();
        wizard.init(engine);

        //Instantiates the wizard container with the wizard and opens it
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.open();

    }

}
