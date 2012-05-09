package org.webcat.eclipse.importer.model;

import org.webcat.eclipse.importer.internal.Xml;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;


// -------------------------------------------------------------------------
/**
 *  Represents the root of the import definition tree. The root
 *  contains settings that are inherited down throughout the entire
 *  tree.
 *
 *  @author bellen08
 *  @version Jan 22, 2012
 */
public class RootTarget
    extends ImportTarget
{

    // ----------------------------------------------------------
    /**
     * Create a new RootTarget object.
     */
    public RootTarget()
    {
        super(null);

    }

    // ----------------------------------------------------------
    /**
     * See ImportTarget {@link #isContainer()}
     */
    @Override
    public boolean isContainer()
    {
        return true;
    }

    // ----------------------------------------------------------
    /**
     * See ImportTarget {@link #isNested()}
     */
    @Override
    public boolean isNested()
    {
        return false;
    }

    // ----------------------------------------------------------
    /**
     * See ImportTarget {@link #isActionable()}
     */
    @Override
    public boolean isActionable()
    {
        return false;
    }

    // ----------------------------------------------------------
    /**
     * See ImportTarget {@link #isLoaded()}
     */
    @Override
    public boolean isLoaded()
    {
        return true;
    }

    // ----------------------------------------------------------
    /**
     * See ImportTarget {@link #parse(Node node)}
     */
    @Override
    public void parse(Node node)
    {
        String nodeName = node.getLocalName();
        if (Xml.Elements.IMPORT_TARGETS.equals(nodeName))
        {
            parseImportTargets(node);
        }
    }

    // ----------------------------------------------------------
    /**
     * Parses the children of a import-target root node.
     *
     * @param parentNode the node to parse.
     */
    private void parseImportTargets(Node parentNode)
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
     * @return the new project group
     */
    private ImportTarget parseProjectGroup(Node node)
    {
        ProjectGroupTarget group = new ProjectGroupTarget(this);
        group.parse(node);
        return group;
    }

    // ----------------------------------------------------------
    /**
     * Called if node represents a project group to create the new
     * group and parse it.
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
