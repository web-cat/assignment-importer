package org.webcat.eclipse.importer.tests;

import org.webcat.eclipse.importer.model.RootTarget;
import org.webcat.eclipse.importer.model.ProjectTarget;
import org.webcat.eclipse.importer.model.ProjectGroupTarget;
import org.webcat.eclipse.importer.model.ImportTarget;

import java.io.InputStream;
import org.webcat.eclipse.importer.Importer;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;



// -------------------------------------------------------------------------
/**
 *  Tests the xml-parsing files..
 *
 *  @author Ellen Boyd
 *  @version Feb 7, 2012
 */
public class TargetParsingTest
{

    private static Importer importer;


    // ----------------------------------------------------------
    /**
     * Initialization done once before all test cases in this class
     * run.
     * @throws Exception if exception occurs
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        importer = new Importer();

        //Create a new instance of an importer and read the test
        //target definitions; this will be shared by all tests
        InputStream stream = TargetParsingTest.class.getResourceAsStream(
            "test-targets.xml");
        importer.readImportTargets(stream);
        stream.close();
    }

    // ----------------------------------------------------------
    /**
     * Tests that the root of the test XML file is not null after
     * the importer reads in the stream.
     *
     * @throws Exception
     */
    @Test
    public void targetRoot() throws Exception
    {
        RootTarget root = importer.getRoot();
        assertNotNull("root should not be null", root);
    }

    // ----------------------------------------------------------
    /**
     * Tests the parsing the root.
     * @throws Exception
     */
    @Test
    public void targetProjectGroups() throws Exception
    {
        RootTarget root = importer.getRoot();
        ImportTarget [] children = root.getChildren();
        //the root should have 3 children ("Labs" "Projects" "Demos")
        assertEquals (3, children.length);
        //project-group "labs"
        ImportTarget leftChild = children[0];
        assertTrue(leftChild instanceof ProjectGroupTarget);
        ProjectGroupTarget labGroup = (ProjectGroupTarget)leftChild;
        assertEquals(labGroup.getName(), "Labs");
        assertFalse(labGroup.isHidden());


        ImportTarget middleChild = children[1];
        assertTrue(middleChild instanceof ProjectGroupTarget);
        ProjectGroupTarget projectGroup = (ProjectGroupTarget)middleChild;
        assertEquals(projectGroup.getName(), "Projects");
        assertFalse(projectGroup.isHidden());

        ImportTarget rightChild = children[2];
        assertTrue(rightChild instanceof ProjectGroupTarget);
        ProjectGroupTarget demoGroup = (ProjectGroupTarget)rightChild;
        assertEquals(demoGroup.getName(), "Demos");
        assertFalse(demoGroup.isHidden());


    }

    // ----------------------------------------------------------
    /**
     * Tests the parsing the root.
     * @throws Exception
     */
    @Test
    public void targetProjectGroupsinGroups() throws Exception
    {
        RootTarget root = importer.getRoot();
        ImportTarget [] children = root.getChildren();
        //the root should have 3 children ("Labs" "Projects" "Demos")
        assertEquals (3, children.length);
        //project-group "labs"
        ImportTarget leftChild = children[0];
        assertTrue(leftChild instanceof ProjectGroupTarget);
        ProjectGroupTarget labGroup = (ProjectGroupTarget)leftChild;
        assertEquals(labGroup.getName(), "Labs");
        assertFalse(labGroup.isHidden());

        ImportTarget[] labChildren = leftChild.getChildren();
        assertEquals(2, labChildren.length);

        ProjectTarget lab01 = (ProjectTarget)labChildren[0];
        assertEquals("lab01", lab01.getName());
        assertFalse(lab01.isHidden());

        ProjectTarget lab02 = (ProjectTarget)labChildren[1];
        assertEquals("lab02", lab02.getName());
        assertTrue(lab02.isHidden());



    }

    // ----------------------------------------------------------
    /**
     * Tests the Demos group.
     * @throws Exception
     */
    @Test
    public void targetProject() throws Exception
    {
        RootTarget root = importer.getRoot();

        ProjectGroupTarget demoGroup = (ProjectGroupTarget)root.getChildren()[2];
        assertEquals("Demos", demoGroup.getName());
        ImportTarget[] demoChildren = demoGroup.getChildren();
        assertEquals(2,demoChildren.length);

        //Left child is a group with 2 project children (android)
        ImportTarget leftDemoChild = demoChildren[0];
        assertTrue(leftDemoChild instanceof ProjectGroupTarget);

        //Right child is a project (Linked list)
        ImportTarget demoProject = demoChildren[1];
        assertTrue(demoProject instanceof ProjectTarget);

        ProjectTarget project = (ProjectTarget)demoProject;

        assertEquals("Linked List", project.getName());

        assertFalse(project.isHidden());

        assertEquals("linked list uri", project.getURI());

        assertEquals("linked list depends", project.getDepends());

        assertEquals("linked list id", project.getID());
    }

}
