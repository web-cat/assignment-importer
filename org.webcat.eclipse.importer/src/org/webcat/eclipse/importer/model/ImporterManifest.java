package org.webcat.eclipse.importer.model;

import org.webcat.eclipse.importer.XMLUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// -------------------------------------------------------------------------
/**
 *  This class stores references to several objects that are required in
 *  various places during the import process, so they can easily be passed
 *  between functions.
 *
 *  @author Ellen Boyd
 *  @version Mar 23, 2012
 */
public class ImporterManifest
{
    /*The project in which the user is importing*/
    private ProjectTarget project;

    /* The ID of the user*/
    private String username;

    /*The password used to log into the submission target system, if
     * required
     */
    private String password;

    /* Other parameters to include in the importer manifest.
     */
    private Map<String, String> parameters;

    /*
     * List of dependent projects for this project.
     */
    private ArrayList<ProjectTarget> dependencies;

    // ----------------------------------------------------------
    /**
     * Create a new ImporterManifest object.
     */
    public ImporterManifest()
    {
        parameters = new HashMap<String, String>();
        dependencies = new ArrayList<ProjectTarget>();
    }



    // ----------------------------------------------------------
    /**
     * Gets the project referred to by this object.
     *
     * @return a ProjectTarget representing the project to submit.
     */
    public ProjectTarget getProject()
    {
        return project;
    }

    // ----------------------------------------------------------
    /**
     * Sets the projet value.
     * @param val the new project
     */
    public void setProjectTarget(ProjectTarget val)
    {
        project = val;
    }

    // ----------------------------------------------------------
    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    // ----------------------------------------------------------
    /**
     * @param username the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    // ----------------------------------------------------------
    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    // ----------------------------------------------------------
    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    // ----------------------------------------------------------
    /**
     * @param project the project to set
     */
    public void setProject(ProjectTarget project)
    {
        this.project = project;
    }

    public ArrayList<ProjectTarget> getDependencies()
    {
        return dependencies;
    }
    // ----------------------------------------------------------
    /**
     * Returns the number of project this current import project is dependent on.
     * @return
     */
    public int numDependencies()
    {
        return dependencies.size();
    }

    /**
     * Adds a project to the list of dependencies.
     * @param proj the new dependency.
     */
    public void addDependentProject(ProjectTarget proj)
    {
        dependencies.add(proj);
    }

//    private void setDependencies(String depends)
//    {
//        XMLUtil util = new XMLUtil();
//        ArrayList<String> projIds = util.parseDependsString(depends);
//
//    }
}
