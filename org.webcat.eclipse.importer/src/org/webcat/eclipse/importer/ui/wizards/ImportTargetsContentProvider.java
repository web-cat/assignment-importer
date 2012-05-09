package org.webcat.eclipse.importer.ui.wizards;

import org.webcat.eclipse.importer.model.ImportTarget;
import java.util.ArrayList;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ITreeContentProvider;

// -------------------------------------------------------------------------
/**
 *  The content provider for the tree that displays the import
 *  targets in the wizard.
 *
 *  @author bellen08
 *  @version Feb 2, 2012
 */
public class ImportTargetsContentProvider
    implements ITreeContentProvider
{

    /* The root of the import target tree. */
    private ImportTarget root;

    /**
     * (from org.eclipse.jface.viewers.ITreeContentProvider:
     * Disposes of this content provider. This is called by the viewer when it
     * is disposed.
     * The viewer should not be updated during this call as it is in the
     * process of being disposed.
     */
    @Override
    public void dispose()
    {
        //do nothing?
    }


    /**
     * (from org.eclipse.jface.viewers.IContentProvider):
     * Notifies this content provider that the given viewer's input has been
     * switched to a different element.
     *
     * A typical use for this method is registering the content provider as a
     * listener to changes on the new input (using -model specific means), and
     * deregistering the viewer from the older input. In response to these
     * change notifications, the content provider should update the viewer (see
     * the add, remove, update and refresh methods on the viewers.)
     */
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        root = (ImportTarget)newInput;
    }


    /**
     * (from org.eclipse.jface.viewers.ITreeContentProvider)
     * Returns the elements to display in the viewer when its input
     * is set to the given element. These elements can be presented
     * as rows in the table, items in a list, etc. The result is not modified
     * by the viewer.
     */
    @Override
    public Object[] getElements(Object inputElement)
    {
        return getChildren(root);
    }


    /**
     * (from org.eclipse.jface.viewers.ITreeContentProvider):
     * Returns the child elements of the given parent element.
     *
     * The difference between this method and the IstructuredContentProvider.getElements
     * is that getElements is called to obtain the tree viewer's root elements,
     * whereas get children is used to obtain the children of a given parent element
     * in the tree (including a root)
     */
    @Override
    public Object[] getChildren(Object parentElement)
    {
        ImportTarget obj = (ImportTarget)parentElement;
        ArrayList<ImportTarget> children = new ArrayList<ImportTarget>();

        computeChildren(obj, children);
        return children.toArray();
    }

    /**
     * Computes the visible children of the specified node, displaying
     * a message to the user if an error occurs.
     */
    private void computeChildren(ImportTarget obj, ArrayList<ImportTarget> list)
    {
        try
        {
            ImportTarget[] children = obj.getLogicalChildren();
            for (int i = 0; i < children.length; i ++)
            {
                ImportTarget child = children[i];
                if(!child.isHidden())
                {
                    if(child.isContainer() && !child.isNested())
                    {
                        computeChildren(child, list);
                    }
                    else
                    {
                        list.add(child);
                    }
                }
            }
        }
        catch(Throwable e)
        {
            //SubmissionParserErrorDialog dlg = new SubmissionParserErrorDialog(null, e);
        }
    }


    /**
     * (from org.eclipse.jface.viewers.ITreeContentProvider):
     * Returns the parent for the given element. or null indicating that the
     * parent can't be computed. In this case the tree-structured
     * viewer can't expand a given node correctly if requested.
     */
    @Override
    public Object getParent(Object element)
    {
        return ((ImportTarget)element).parent();
    }

    /**
     * (from org.eclipse.jface.viewers.ITreeContentProvider):
     *
     * Returns whether the given element has children.
     *
     * Intended as an optimazation for when the viewer does not need the
     * actual children. Clients may be able to implement this more efficiently
     * than getChildren
     */
    @Override
    public boolean hasChildren(Object element)
    {
        ArrayList<ImportTarget> children = new ArrayList<ImportTarget>();
        computeChildren((ImportTarget)element, children);
        return children.size() > 0;
    }

}
