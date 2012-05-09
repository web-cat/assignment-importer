package org.webcat.eclipse.importer;

import org.webcat.eclipse.importer.model.ImportTarget;
import org.webcat.eclipse.importer.model.RootTarget;
import org.webcat.eclipse.importer.model.ProjectTarget;
import java.util.Scanner;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 *  Class with utility for parsing the XML import file. Specifically
 *  dealing with the dpendency list.
 *
 *  @author bellen08
 *  @version Apr 16, 2012
 */
public class XMLUtil
{

    // ----------------------------------------------------------
    /**
     * Parses the dependency String in the form "id, id, id" into an
     * array list of Strings each representing the unique id of the
     * given project
     * @param depends return null if empty
     * @return list of dependent project ids
     */
    public ArrayList<String> parseDependsString(String depends)
    {
        Scanner sc = new Scanner(depends);
        ArrayList<String> dList = new ArrayList<String>();
        sc.useDelimiter(", *");
        while(sc.hasNext())
        {
            dList.add(sc.next());
        }
        if(dList.size() != 0)
        {
            return dList;
        }
        else
        {
            return null;
        }
    }

    /**
     * Method that will return the arraylist of projectTargets representing the
     * dependency folders
     * @param root the root target
     * @param depends the dependency string attribute of the project target
     * @return ArrayList of projectTargets
     */
    public ArrayList<ProjectTarget> getProjectDependencies(RootTarget root, String depends)
    {
        ArrayList<ProjectTarget> list = new ArrayList<ProjectTarget>();
        ArrayList<String> ids = parseDependsString(depends);
        ImportTarget [] children = root.getChildren();
        ProjectTarget projTarg;
        for(String id: ids)
        {
            for (int i = 0; i < children.length; i++)
            {
                if(!children[i].isContainer())
                {
                    projTarg = (ProjectTarget)children[i];
                    if(projTarg.getID().equals(id))
                    {
                        list.add(projTarg);
                    }
                }
            }
        }
        return list;
    }
}
