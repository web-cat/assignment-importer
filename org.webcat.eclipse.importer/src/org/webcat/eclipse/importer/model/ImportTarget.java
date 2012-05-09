package org.webcat.eclipse.importer.model;

import org.webcat.eclipse.importer.internal.Xml;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

import java.util.LinkedHashMap;

// -------------------------------------------------------------------------
/**
 *  An abstract base class from which all objects in the import target tree
 *  are derived.
 *
 *  @author bellen08
 *  @version Jan 22, 2012
 */
public abstract class ImportTarget
{


    // ----------------------------------------------------------
    /**
     * Create a new ImportTarget object.
     * @param parent represents this node's parent in the tree
     */
    protected ImportTarget(ImportTarget parent)
    {
        this.parent = parent;
        name = null;
        hidden = false;


        otherAttributes = new LinkedHashMap<String, String>();
    }

    // ----------------------------------------------------------
    /**
     * Gets the parent node to this node in the tree.
     * @return the ImportTarget that is the parent of this tree
     */
    public ImportTarget parent()
    {
        return parent;
    }


    // ----------------------------------------------------------
    /**
     * Gets the root of the tree that this object is contained in.
     * @return the root of the tree, or null if it could not be
     *  found or if the top level target was not a RootTarget
     */
    public RootTarget getRoot()
    {
        ImportTarget par = this;
        while(par.parent() != null)
        {
            par = par.parent();
        }
        if(par instanceof RootTarget)
        {
            return (RootTarget) par;
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node may
     * contain children.
     *
     * @return true if the node may contain children; otherwise, false.
     */
    public abstract boolean isContainer();

    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node should be
     * displayed at the same level as its parent, or if it should be nested
     * at a lower-level. Typically a target that is a container will not be
     * nested if it does not have a name, but subclasses can provide their own
     * behavior.
     *
     * @return true if the node should be nested at a lower level in the tree;
     *      false if it should be displayed at same level as parent.
     */
    public abstract boolean isNested();

    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether an action can be taken
     * on this node. In a wizard for example, this would enable the "next"
     * or "finish" button so the user can download/import an assignment.
     *
     * @return true if the node is actionable; otherwise, false
     */
    public abstract boolean isActionable();

    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node has been
     * loaded into local memory. This is always true for most nodes.
     * @return true if the node is local or if it has been delay-loaded;
     * false if
     */
    public abstract boolean isLoaded();


    // ----------------------------------------------------------
    /**
     * Gets the name of the target.
     *
     * @return the name of the target
     */
    public String getName()
    {
        return name;
    }

    // ----------------------------------------------------------
    /**
     * Sets the name of the target
     *
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

//    // ----------------------------------------------------------
//    /**
//     * Gets the list of the dependencies for the given ImportTarget.
//     *
//     * @return an array of the id numbers of projects this importTarget
//     *      is dependent on.
//     */
//    public int[] getDependencies()
//    {
//        return dependencies;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * @return the uri
//     */
//    public String getUri()
//    {
//        return uri;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * @param uri the uri to set
//     */
//    public void setUri(String uri)
//    {
//        this.uri = uri;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * @return the idNum
//     */
//    public int getIdNum()
//    {
//        return idNum;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * @param idNum the idNum to set
//     */
//    public void setIdNum(int idNum)
//    {
//        this.idNum = idNum;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * @param actionable the actionable to set
//     */
//    public void setActionable(boolean actionable)
//    {
//        this.actionable = actionable;
//    }
//
//    // ----------------------------------------------------------
//    /**
//     * Sets the value of dependencies.
//     *
//     * @param dependencies the new dependency list.
//     */
//    public void setDependencies(int[] dependencies)
//    {
//        this.dependencies = dependencies;
//    }

    // ----------------------------------------------------------
    /**
     * Gets the value indicating whether the target should be hidden
     * in the user interface.
     *
     * @return true if the target should be hidden; false otherwise
     */
    public boolean isHidden()
    {
        return hidden;
    }

    // ----------------------------------------------------------
    /**
     * Sets the value indicating whether the target should be hidden in
     * the user interface.
     *
     * @param value true if the target should be hidden; otherwise, false.
     */
    public void setHidden(boolean value)
    {
        hidden = value;
    }




    // ----------------------------------------------------------
    /**
     * Gets the value of an attribute for import targets at this level
     * in the tree. This function walks up the tree to find an inherited
     * attribute if necessary
     *
     * @param attribute the name of the attribute
     * @return a String containing the value of the attribute, or null
     *      if not present
     */
    public String getAttribute(String attribute)
    {
        String localAttribute = getLocalAttribute(attribute);
        if(localAttribute != null)
        {
            return localAttribute;
        }
        else if(parent != null)
        {
            return parent.getAttribute(attribute);
        }
        else
        {
            return null;
        }
    }

    // ----------------------------------------------------------
    /**
     * Gets the value of an attribute for import targets at this level in
     * the tree. This function does not walk up the tree to find an inherited
     * attribute-- it returns the attribute specified for this node only.
     *
     * @param attribute the name of the attribute
     * @return a String containing the value of the attribute or null
     *      if not present.
     */
    public String getLocalAttribute(String attribute)
    {
        return otherAttributes.get(attribute);
    }

    // ----------------------------------------------------------
    /**
     * Sets an attribute for this node.
     *
     * @param attribute the name of the attribute
     * @param value the value for the attribute
     */
    public void setAttribute(String attribute, String value)
    {
        if (value != null)
        {
            otherAttributes.put(attribute, value);
        }
        else
        {
            otherAttributes.remove(attribute);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the children of this node. This function only considers the
     * link structure of the target tree, not the nested state of any
     * of the nodes
     *
     * @return an array of ImportTargets representing the children of the
     *      node
     */
    public ImportTarget[] getChildren()
    {
        if (children != null)
        {
            return children;
        }
        else
        {
            return new ImportTarget[0];
        }
    }

    // ----------------------------------------------------------
    /**
     * A helper function to ease initializing children from a list
     *
     * @param list the list to set the children from.
     */
    protected void setChildren(List<ImportTarget> list)
    {
        children = new ImportTarget[list.size()];
        list.toArray(children);
    }

    // ----------------------------------------------------------
    /**
     * Gets the "logical" children of this node, respecting the nested state of
     * any children (so that children of a non-nested child are "pushed up"
     * into the parent). This method is appropriate for determining the
     * children of a node as they should be displayed in a user interface.
     *
     * @return an array of ImportTargets that represent the logical
     *     children of the node
     */
    public ImportTarget[] getLogicalChildren()
    {
       List<ImportTarget> childList = new ArrayList<ImportTarget>();

       computeLogicalChildren(this, childList);

       ImportTarget[] array = new ImportTarget[childList.size()];
       childList.toArray(array);
       return array;
    }


    // ----------------------------------------------------------
    /**
     * Recursively computes the children for the specified target, taking into
     * account the nested state of each target.
     *
     * @param target the target whose children should be computed
     * @param list a list that will hold the children upon returning
     */
    private static void computeLogicalChildren(ImportTarget target,
            List<ImportTarget> list)
    {
        ImportTarget[] children = target.getChildren();

        for (ImportTarget child : children)
        {
            if (!child.isHidden())
            {
                if (child.isContainer() && !child.isNested())
                {
                    computeLogicalChildren(child, list);
                }
                else
                {
                    list.add(child);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Parses the specified xml node and builds a subtree from the data.
     *
     * @param node the XML document Node to parese from
     */
    public abstract void parse(Node node);


    // ----------------------------------------------------------
    /**
     * Parses the common attributes for the specified XML node and adds them to
     * the target.
     *
     * @param node the XML document node to parse from.
     */
    protected void parseCommonAttributes(Node node)
    {
        Node nameNode = node.getAttributes().getNamedItem(Xml.Attributes.NAME);
        Node hiddenNode = node.getAttributes().getNamedItem(Xml.Attributes.HIDDEN);

        //Node uriNode = node.getAttributes().getNamedItem(Xml.Attributes.URI);
        //Node idNode = node.getAttributes().getNamedItem(Xml.Attributes.ID);
        //Node dependsNode = node.getAttributes().getNamedItem(Xml.Attributes.DEPENDS);
        //Node actionableNode = node.getAttributes().getNamedItem(Xml.Attributes.ACTIONABLE);

        String hiddenString = null;
       // String actionableString = null;
        //String idNumString = null;
        //String dependsString = null;
        if (nameNode != null)
        {
            setName(nameNode.getNodeValue());
        }

        if (hiddenNode != null)
        {
            hiddenString = hiddenNode.getNodeValue();
        }

        setHidden(Boolean.parseBoolean(hiddenString));


        for (int i = 0; i < node.getAttributes().getLength(); i ++)
        {
            Node attribute = node.getAttributes().item(i);
            String attributeName = attribute.getNodeName();

            String value = attribute.getNodeValue();
            otherAttributes.put(attributeName, value);
        }

    }





    /* The parent object to this object in the tree. */
    private ImportTarget parent;

    /* The name of the target */
    private String name;

    /* Indicates whether the target should be hidden */
    private boolean hidden;



    /* Indicates the id of the node */
 //   private int idNum;

    /* The list of child nodes to this node in the import target tree. */
    private ImportTarget[] children;

    /* Other attributes associated with an import target*/
    private LinkedHashMap<String, String> otherAttributes;

    /* The current node's dependencies stored in an array of ints representing
     * the id number of the project its dependent on.
     */
   // private int[] dependencies;

    //private boolean actionable;
}
