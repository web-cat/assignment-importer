package org.webcat.eclipse.importer.ui.wizards;

import java.util.ArrayList;
import org.webcat.eclipse.importer.XMLUtil;
import org.webcat.eclipse.importer.model.RootTarget;
import org.eclipse.core.runtime.CoreException;
import java.io.IOException;
import org.webcat.eclipse.importer.model.ImporterManifest;
import org.webcat.eclipse.importer.model.ProjectTarget;
import org.webcat.eclipse.importer.ui.ImporterUIPlugin;
import org.webcat.eclipse.importer.ui.messages.*;
import org.webcat.eclipse.importer.Importer;
import org.eclipse.jface.wizard.Wizard;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author bellen08
 *  @version Feb 2, 2012
 */

public class ImporterWizard
    extends Wizard
{

    /**
     * The main page of the wizard that contains the assignment list
     * and other entry fields.
     */
    private ImporterStartPage startPage;

    private ImporterSummaryPage summaryPage;

    private ProjectTarget project;
    /**
     * A reference to the importer engine that should be used by the wizard.
     */
    private Importer importer;

    // ----------------------------------------------------------
    /**
     * Setter method for the project field. (Called from ImporterSummaryPage)
     * @param target the project to download
     */
    public void setProject(ProjectTarget target)
    {
        project = target;
    }
    // ----------------------------------------------------------
    @Override
    public boolean performFinish()
    {
        //read from the ImportStartPage reference
        ProjectTarget target = startPage.getSelectedProject();
        ImporterManifest manifest = new ImporterManifest();
        manifest.setProjectTarget(target);
        //get a reference to the root
        RootTarget root = importer.getRoot();
        XMLUtil util = new XMLUtil();
        String depends = target.getDepends();
        ArrayList<ProjectTarget> dList = util.getProjectDependencies(root, depends);
        //add all the dependent projects so that they are stored in the manifest.
        for(ProjectTarget pt: dList)
        {
            manifest.addDependentProject(pt);
        }
        //call the import action
        try
        {
            importer.importProject(manifest);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Initializes the wizard;
     * @param anImporter
     */
    public void init(Importer anImporter)
    {
        //Initiate the wizard.
        this.importer = anImporter;
        this.setWindowTitle(Messages.WIZARD_TITLE);
        this.setDefaultPageImageDescriptor(ImporterUIPlugin.getImageDescriptor("application-import.png"));
        this.setNeedsProgressMonitor(true);
    }


 // ----------------------------------------------------------
    public void addPages()
    {

        startPage = new ImporterStartPage(importer);

        addPage(startPage);
       // addPage(finalPage);


        //submitter.setLongRunningTaskManager(
    //            new RunnableContextLongRunningTaskManager(getContainer()));
    }


    @Override
    public boolean canFinish()
    {
        return true;
    }


}
