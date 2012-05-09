package org.webcat.eclipse.importer.tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import java.io.InputStream;
import org.junit.BeforeClass;
import org.webcat.eclipse.importer.Importer;
import org.webcat.eclipse.importer.XMLUtil;

public class XMLUtilTest
{

    private static XMLUtil xmlUtil;
    private static ArrayList<String> dList;

    // ----------------------------------------------------------
    /**
     * Initialization done once before all test cases in this class
     * run.
     * @throws Exception if exception occurs
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        xmlUtil = new XMLUtil();

        String dependsList = "id13232, id23423";
        dList = xmlUtil.parseDependsString(dependsList);

    }

    @Test
    public void testParse()
    {
        assertEquals(2, dList.size());
        String first = dList.get(0);
        String second = dList.get(1);
        assertEquals(first, "id13232");
        assertEquals(second, "id23423");
    }

}
