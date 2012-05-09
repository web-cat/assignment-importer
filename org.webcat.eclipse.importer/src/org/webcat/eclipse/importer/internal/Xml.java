package org.webcat.eclipse.importer.internal;


// -------------------------------------------------------------------------
/**
 *  Constants representing the elements and attributes in the xml file.
 *
 *  @author bellen08
 *  @author Tony Allevato
 *  @version Jan 18, 2012
 */
public class Xml
{

    // -------------------------------------------------------------------------
    /**
     *  Attributes of elements in the XML file.
     *
     *  @author bellen08
     *  @version Jan 18, 2012
     */
    public static class Attributes
    {
        public static final String HIDDEN = "hidden";
        public static final String DEPENDS = "depends";
        public static final String ID = "id";
        public static final String URI = "uri";
        public static final String NAME = "name";
        public static final String ACTIONABLE = "actionable";

    }


    // -------------------------------------------------------------------------
    /**
     *  Elements (nodes) in the XML definition file.
     *
     *  @author bellen08
     *  @version Jan 18, 2012
     */
    public static class Elements
    {
        public static final String PROJECT_GROUP = "project-group";
        public static final String PROJECT = "project";
        public static final String IMPORT_TARGETS = "import-targets";
    }

}
