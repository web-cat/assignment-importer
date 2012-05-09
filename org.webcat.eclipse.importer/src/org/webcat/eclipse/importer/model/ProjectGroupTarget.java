package org.webcat.eclipse.importer.model;

import org.webcat.eclipse.importer.internal.Xml;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

// -------------------------------------------------------------------------
/**
 *  Represents a project group in the import definition tree. A
 *  project group is a container for other groups and projects, and
 *  contains common settings that can be inherited by its children.
 *
 *
 *  @author bellen08
 *  @version Jan 22, 2012
 */
public class ProjectGroupTarget
    extends ImportTarget
{

    // ----------------------------------------------------------
    /**
     * Creates a new project group node with the specified parent.
     *
     * @param parent the node assigned parent of new node
     */
    public ProjectGroupTarget(ImportTarget parent)
    {
        super(parent);
    }

    @Override
    public boolean isContainer()
    {
        return true;
    }

    @Override
    public boolean isNested()
    {
        return (getName() != null);
    }

    @Override
    public boolean isActionable()
    {
       return false;
    }

    @Override
    public boolean isLoaded()
    {
        return true;
    }

    @Override
    public void parse(Node parentNode)
    {
        parseCommonAttributes(parentNode);
        Node node = parentNode.getFirstChild();

        List<ImportTarget> children = new ArrayList<ImportTarget>();

        while (node != null)
        {
            String nodeName = node.getLocalName();

            if (Xml.Elements.PROJECT_GROUP.equals(nodeName))
            {
                children.add(parseProjectGroup(node));
            }

            else if (Xml.Elements.PROJECT.equals(nodeName))
            {
                children.add(parseProject(node));
            }
            node = node.getNextSibling();
        }
        setChildren(children);
    }

    // ----------------------------------------------------------
    /**
     * Called if node represents a project group to create the new
     * group and parse it.
     *
     * @param node the project-group node to parse
     * @return the new project-group
     */
    private ImportTarget parseProjectGroup(Node node)
    {
        ProjectGroupTarget group = new ProjectGroupTarget(this);
        group.parse(node);
        return group;
    }

    // ----------------------------------------------------------
    /**
     * Called if node reprsents a project to create the new assignment and
     * parse it.
     *
     * @param node the project node to parse
     * @return the new project
     */
    private ImportTarget parseProject(Node node)
    {
        ProjectTarget project = new ProjectTarget(this);
        project.parse(node);
        return project;
    }


}
