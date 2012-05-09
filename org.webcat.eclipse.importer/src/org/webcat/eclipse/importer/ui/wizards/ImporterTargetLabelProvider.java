package org.webcat.eclipse.importer.ui.wizards;
import org.webcat.eclipse.importer.model.ImportTarget;
import org.webcat.eclipse.importer.ui.ImporterUIPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.LabelProvider;

// -------------------------------------------------------------------------
/**
 *  The label provider for the import target tree in the wizard.
 *
 *  @author  Ellen Boyd (bellen08)
 *  @version Feb 4, 2012
 */
public class ImporterTargetLabelProvider
    extends LabelProvider
{
    /**
     * The image used for project groups
     */
    private Image folderImage;
    /**
     * The image used for project targets.
     */
    private Image fileImage;

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the label provider.
     */
    public ImporterTargetLabelProvider()
    {
        folderImage = ImporterUIPlugin.
            getImageDescriptor("folder.gif").createImage();
        fileImage = ImporterUIPlugin.getImageDescriptor
            ("file.gif").createImage();

    }

    /**
     * From help.eclipse.org:
     * Disposes of this label provider.
     */
    public void dispose()
    {
        folderImage.dispose();
        fileImage.dispose();

        super.dispose();
    }

    public Image getImage(Object element)
    {
        ImportTarget object = (ImportTarget)element;

        if(object.isContainer())
        {
            return folderImage;
        }
        else
        {
            return fileImage;
        }
    }

    /**
     * From org.eclipse.jface.viewers.ilabelprovider
     * Returns the text for the label of the given element.
     * In this case, should return the name of the assignment
     * or assignment group.
     * @param element the ImportTarget element
     */
    public String getText(Object element)
    {
        ImportTarget object = (ImportTarget)element;

        if (object.getName() != null)
        {
            return object.getName();
        }
        else
        {
            //if the object is a group with no name
            if(object.isContainer())
            {
                return "";
            }
            else
            {
                return super.getText(element);
            }
        }
    }
}
