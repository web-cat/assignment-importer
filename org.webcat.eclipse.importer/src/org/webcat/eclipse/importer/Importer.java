package org.webcat.eclipse.importer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.core.resources.*;
import org.webcat.eclipse.importer.model.ImportTarget;
import java.util.ArrayList;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import java.net.URL;
import java.io.File;
import org.webcat.eclipse.importer.model.ProjectTarget;
import org.webcat.eclipse.importer.model.ImporterManifest;
import org.webcat.eclipse.importer.model.RootTarget;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.webcat.eclipse.importer.internal.SubmissionParserErrorHandler;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.io.InputStream;
import org.eclipse.jdt.core.JavaCore.*;
import org.eclipse.jdt.core.IJavaProject.*;
import org.eclipse.jdt.core.IClasspathContainer;

// -------------------------------------------------------------------------
/**
 * The primary class providing functionality for the electronic importer. Usage
 * example: java.io.File folder = new
 * java.io.File("path to what you want to import"); ImportableFile itemToImport
 * = new ImportableFile(folder) Importer importer = new Importer(); URL
 * targetsURL = new URL("http://yoursite.com/targets.xml");
 * importer.readImportTargets(targetsURL); RootTarget root = importer.getRoot()
 * //Traverse the import targets from root and get the assignment that you
 * //with to import to .. perhaps via a user interface ProjectTarget project =
 * ...;
 *
 * @author Ellen Boyd (bellen08)
 * @version Feb 5, 2012
 */
public class Importer
{
    private RootTarget root;


    // ----------------------------------------------------------
    /**
     * Gets the root object of the import target tree.
     *
     * @return an ITargetRoot represented the root of the import target
     */
    public RootTarget getRoot()
    {
        return root;
    }


    // ----------------------------------------------------------
    /**
     * Reads the import target definitions from the specified URL.
     *
     * @param stream
     *            the InputStream
     * @throws IOException
     *             if an I/O exception occurred.
     */
    public void readImportTargets(final InputStream stream)
        throws IOException
    {
        try
        {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);
            factory.setCoalescing(false);
            factory.setNamespaceAware(true);
            factory.setValidating(false);

            SubmissionParserErrorHandler errorHandler =
                new SubmissionParserErrorHandler();

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(errorHandler);

            Document document = builder.parse(stream);
            TargetParseError[] errors = errorHandler.getErrors();

            if (errors != null)
            {
                // throw new TargetParseException(errors);
            }
            else
            {
                root = new RootTarget();
                root.parse(document.getDocumentElement());
            }

        }
        catch (ParserConfigurationException e)
        {
            throw new ImporterTargetException(e);
        }
        catch (SAXException e)
        {
            throw new ImporterTargetException(e);
        }
    }


    // also import the dependency here
    public void importProject(final ImporterManifest manifest)
        throws IOException,
        CoreException
    {
        ProjectTarget project = manifest.getProject();
        ArrayList<ProjectTarget> dependProjs = new ArrayList<ProjectTarget>();
        // create file destination path
        // ArrayList<String> depends = project.getDepends();
        String depends = project.getDepends();
        // if it does have dependencies, link the dependencies first
        if (depends != null)
        {
           dependProjs = linkDependencies(depends, project);//Helper method to download the dependent files
        }
       // dependProjs = manifest.getDependencies();
        //linkDepends(dependProjs);
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        IProject workspaceProject =
            workspace.getRoot().getProject(project.getName());
        if (workspaceProject.exists())
        {
            return;
        }
        IPath workspacePath = workspace.getRoot().getLocation();
        IPath path = workspacePath; // .append(workspaceProject.getName());
        File sendTo = path.toFile();

        URL url = new URL(project.getURI());
        InputStream stream = url.openStream();
        ZipUtils unzipper = new ZipUtils();
        unzipper.unpack(sendTo, stream);

        IProjectDescription description =
            workspace.loadProjectDescription(path.append(workspaceProject
                .getName() + "/.project"));
        workspaceProject.create(description, null);
        workspaceProject.open(null);
        for(int i = 0; i < dependProjs.size(); i ++)
       {
            dependencyHelper(project, dependProjs.get(i));
      }
    }

    /**
     * Potential helper method to link the dependencies. (Using the manifest's
     * already-parsed values.
     * @param depends the ArrayList of the project's the project being imported
     *  is dependent on.
     */
    private void linkDepends(ArrayList<ProjectTarget> depends)
    {
        for(ProjectTarget pt: depends)
        {
            if(ResourcesPlugin.getWorkspace().getRoot().findMember(pt.getName()) != null)
            {
                //then the project exists in the workspace, no need to import
            }
            //else:
            else
            {
                ImporterManifest manifest = new ImporterManifest();
                XMLUtil util = new XMLUtil();
                String ds = pt.getDepends();
                ArrayList<ProjectTarget> dList = util.getProjectDependencies(root, ds);       for(ProjectTarget p: dList)
                {
                    manifest.addDependentProject(pt);
                }
                manifest.setProjectTarget(pt);

                try
                {
                    importProject(manifest);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                catch(CoreException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * Helper method to link the dependent folders of the given project being
     * imported.
     *
     * @param depends
     *            the String of the dependencies attribute form XML
     * @throws CoreException
     */
    private ArrayList<ProjectTarget> linkDependencies(String depends, ProjectTarget project) throws CoreException
    {
        ArrayList<ProjectTarget> dependProjects = new ArrayList<ProjectTarget>();
        XMLUtil util = new XMLUtil();
        ArrayList<String> dependIds = util.parseDependsString(depends);
        if (dependIds != null)
        {
            for (int i = 0; i < dependIds.size(); i++)
            {
                String id = dependIds.get(i);                 // find the project based on this.
                ImportTarget[] children = root.getChildren();
                for (int j = 0; j < children.length; j++)
                {
                    ImportTarget it = children[j];
                    ProjectTarget proj;
                    // if it isn't a container then it is a project
                    if (!it.isContainer())
                    {
                        proj = (ProjectTarget)it; // downcast to a ProjectTarget
                        //check to see if the ProjectTarget dependency is already
                        //in the users workspace
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        IWorkspaceRoot root = workspace.getRoot();

                        // if the project's id matches the given id
                        // then we've found the dependent project
                        if (proj.getID().equals(id))
                        {
                            dependProjects.add(proj);
                            //If it is is already in the user's workspace, then don't do anything
                            if(root.findMember(proj.getName()) != null)
                            {
                                System.out.println("Exists");
                            }
                            else
                            {
                                //variable proj is the dependency folder
                                ImporterManifest manifest = new ImporterManifest();//create a new manifest (prepare for download)
                                manifest.setProjectTarget(proj); //set the project target
                                try
                                {
                                    importProject(manifest);// import dependency
// project
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (CoreException e)
                                {
                                    e.printStackTrace();
                                }

                                //link the dependency between project to proj
                            }


                        }
                    }
                }
            }
        }
        return dependProjects;
    }


    /**
     * Helper method to add dependent project to the build path during the import
     * process.
     * @param project the ProjectTarget being imported
     * @param depProj the ProjectTarget that project is dependent on
     */
    private void dependencyHelper(ProjectTarget project, ProjectTarget depProject) throws CoreException
    {
        //IProject for the project being imported
        IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());

        //open the dependent project
        IProject iDepend = ResourcesPlugin.getWorkspace().getRoot().getProject(depProject.getName());
        iDepend.open(new NullProgressMonitor());

        //create a java project
        IJavaProject javaProject = JavaCore.create(iProject);
        IClasspathEntry [] entries = javaProject.getRawClasspath();
        //new class path element
        IClasspathEntry prjEntry = JavaCore.newProjectEntry(new Path("/" + depProject.getName()), true); // exported
        //current class path entries
        IClasspathEntry [] newEntries = new IClasspathEntry[entries.length + 1];
        for(int i = 0; i < entries.length; i ++)
        {
            newEntries[i] = entries[i];
        }
        newEntries[entries.length] = prjEntry;
        //reset the class path of the project
        javaProject.setRawClasspath(newEntries, new NullProgressMonitor());
    }




}

