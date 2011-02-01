package org.geotools.data.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

import junit.framework.TestCase;

/**
 * @author Russell Petty, GSV
 *
 * @source $URL$
 */
public class AttributeCreateOrderListTest extends TestCase {
  
    private static final String ROOT_LABEL = "root";
    
    private AttributeCreateOrderList at;
    private List<TreeAttributeMapping> unProcessedList;
 
    public void testEmptyTree() throws IOException {
        at = new AttributeCreateOrderList(ROOT_LABEL);   
        unProcessedList = new ArrayList<TreeAttributeMapping>();      
       
        processTestData();
    }
    
    public void testSimpleTree() throws IOException {
        at = new AttributeCreateOrderList(ROOT_LABEL);   
        unProcessedList = new ArrayList<TreeAttributeMapping>();
        
        createAttribute(ROOT_LABEL, "child1");
        createAttribute(ROOT_LABEL, "child2");
        createAttribute("child1", "grandChild1");
        createAttribute("child1", "grandChild2");
        createAttribute("child2", "grandChild3");

        processTestData();
    }

    public void testComplexTree() throws IOException {
        at = new AttributeCreateOrderList(ROOT_LABEL);   
        unProcessedList = new ArrayList<TreeAttributeMapping>();
        
        createAttribute(ROOT_LABEL, "child1");
        createAttribute(ROOT_LABEL, "child2");
        createAttribute(ROOT_LABEL, "child3");
        createAttribute("child1", "grandChild1");
        createAttribute("child1", "grandChild2");
        createAttribute("child2", "grandChild3");
        createAttribute("child2", "grandChild4");
        createAttribute("child2", "grandChild5");
        createAttribute("grandChild5", "greatGrandChild6");
        createAttribute("grandChild5", "greatGrandChild7");
        createAttribute("greatGrandChild7", "greatGreatGrandChild8");
        
        processTestData();
    }

    public void testInvalidTree() throws IOException {
        at = new AttributeCreateOrderList(ROOT_LABEL);   
        unProcessedList = new ArrayList<TreeAttributeMapping>();
        
        createAttribute(ROOT_LABEL, "child1");
        createAttribute(ROOT_LABEL, "child2");        
        createAttribute("child1", "grandChild1");
        createAttribute("child1", "grandChild2");
        // next element refers to non existent parent
        createAttribute("xxxx", "grandChild3");

        try {
            processTestData();
            assertTrue(false);
        } catch (IllegalStateException e) {
            // expected result
        }
    }

    public void testInvalidRootInTree() throws IOException {
        at = new AttributeCreateOrderList(ROOT_LABEL);   
        unProcessedList = new ArrayList<TreeAttributeMapping>();
        
        try {
            createAttribute("child1", "child1");  
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected result
        }
    }
    
    private void processTestData() {
        // Main test class. Takes a list and makes sure that:
        // 1) parents are always processed before children
        // 2) The number of elements processed is the same as the number of elements put in.
        // 3) All elements are processed.
        final int size = unProcessedList.size();        
        Iterator<TreeAttributeMapping> it = at.iterator();
        Set<String> retrievedElements = new HashSet<String>();
        retrievedElements.add(ROOT_LABEL);

        int count = 0;
        while(it.hasNext()) {
            count++;
            TreeAttributeMapping tam = it.next();       
            retrievedElements.add(tam.getLabel());
            
            // make sure parents are retrieved before children.
            if(!tam.getLabel().equals(ROOT_LABEL)) {
                assertTrue(retrievedElements.contains(tam.getParentLabel()));
                unProcessedList.remove(tam);
            }
        }
        // make sure all elements are returned
        assertEquals(count, size);
        assertTrue(unProcessedList.isEmpty());
    }
    
    
    private void createAttribute(String parentlabel, String childLabel) {
        TreeAttributeMapping tam =  new TreeAttributeMapping(null, null, null,
                null, false, null, childLabel, parentlabel, null, null);
        at.put(tam);
        unProcessedList.add(tam);
    }
}
