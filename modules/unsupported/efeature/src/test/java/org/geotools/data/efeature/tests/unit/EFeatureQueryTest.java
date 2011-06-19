package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.tests.unit.EFeatureTestData.newIsEqual;

import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureIterator;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.query.EFeatureFilter;
import org.geotools.data.efeature.query.EFeatureQuery;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureQueryTest extends AbstractResourceTest {
    
    private int eFeatureCount = 200;
    private EFeatureTestData eData;
    private Object[][] eTypeData = new Object[][]{
        {"efeature.EFeatureData",EFeatureData.class,null,0},
        {"efeature.EFeatureCompatibleData",EFeatureCompatibleData.class,null,0}};
    
    private EFeatureInfo eFeatureDataInfo;
    private EFeatureInfo eFeatureCompatibleDataInfo;
    private EFeatureTestsContextHelper eContextHelper;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testFeatureQueryEmpty() {
        try {
            
            WHERE where = new WHERE(EObjectCondition.E_TRUE);
            
            dTime();
            
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                EFeatureInfo eInfo = (EFeatureInfo)type[2];
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eObjects, eFilter);
                EFeatureIterator it = eQuery.iterator();
                while(it.hasNext()) {
                    EObject eObject = it.next();
                    String eID = EcoreUtil.getID(eObject);
                    trace("eID: " + eID);
                    count++;
                }
                
                assertEquals("Feature count mismatch",0,count);
                trace("Count["+eType+"]: " + count,TIME_DELTA);
                
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }
    
    @org.junit.Test
    public void testFeatureLiteralQueryNoMatch() {
        try {
            
            EObjectCondition eCondition = newIsEqual(
                    EFeaturePackage.eINSTANCE.getEFeature_ID(), 
                    Integer.toString(Integer.MAX_VALUE));
            WHERE where = new WHERE(eCondition);
            
            dTime();
            
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                EFeatureInfo eInfo = (EFeatureInfo)type[2];
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eObjects, eFilter);
                EFeatureIterator it = eQuery.iterator();
                while(it.hasNext()) {
                    it.next();
                    count++;
                }
                
                assertEquals("Feature count mismatch",0,count);                
                trace("Count["+eType+"]: " + count,TIME_DELTA);
                
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }    
    
    @org.junit.Test
    public void testFeatureLiteralMatch() {
        try {
            // 
            // ---------------------------------------------------
            //  This test assumes that 100 EFeatureData 
            //  instances are created first, consuming the IDs
            //  1 through 100. The test uses this information
            //  to check the boundary IDs, two randomly 
            //  selected IDs within this boundary and one
            //  outside the boundary which should be an 
            //  EFeatureCompatibleData instance. 
            // ---------------------------------------------------
            //
            EObjectCondition eCondition = newIsEqual(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F1");
            eCondition = eCondition.OR(newIsEqual(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F22"));
            eCondition = eCondition.OR(newIsEqual(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F73"));
            eCondition = eCondition.OR(newIsEqual(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F100"));
            eCondition = eCondition.OR(newIsEqual(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F101", eFeatureCompatibleDataInfo));
            WHERE where = new WHERE(eCondition);
            
            dTime();
            
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                EFeatureInfo eInfo = (EFeatureInfo)type[2];
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eObjects, eFilter);
                EFeatureIterator it = eQuery.iterator();
                while(it.hasNext()) {
                    EObject eObject = it.next();
                    count++;
                    if(count==5) {
                        assertTrue("Object " + eObject + " is not an " +
                        	"instance of EFeatureCompatibleData",
                        	eObject instanceof EFeatureCompatibleData);
                    }
                }
                
                assertEquals("Feature count mismatch",5,count);
                trace("Count["+eType+"]: " + count,TIME_DELTA);
                
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }    
    
    @org.junit.Test
    public void testFeatureCQL() {
        try {
            
            // -----------------------------------------------
            //  Test OpenGIS Filter -> EMF Where conversion
            // -----------------------------------------------
            
            Filter filter = CQL.toFilter("default LIKE 'geom' AND " +
            		"(ID = 'F1' OR ID = 'F22' OR ID = 'F73' OR ID = 'F100')");
            
            dTime();
            
            for(Object[] type : eTypeData) {
                int count = 0;
                String eType = type[0].toString();                
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureQuery eQuery = EFeatureUtils.toEFeatureQuery(eObjects, filter);
                EFeatureIterator it = eQuery.iterator();
                while(it.hasNext()) {
                    it.next();
                    count++;
                }
                assertEquals("Feature count mismatch",4,count);
                trace("Count["+eType+"]: " + count,TIME_DELTA);
                
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }            
    }    
    
    // ----------------------------------------------------- 
    //  TestCase setup
    // -----------------------------------------------------

    /**
     * Main for test runner.
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Constructor with test name.
     */
    public EFeatureQueryTest(String name) {
        super(name, "xmi", true, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureQueryTest.class);
    }
        
    @Override
    protected void doSetUp() throws Exception {
        //
        // Initialize commonly used objects
        //
        eContextHelper = new EFeatureTestsContextHelper(true, false);
        eFeatureDataInfo = eContextHelper.eGetFeatureInfo("efeature.EFeatureData");
        eFeatureCompatibleDataInfo = eContextHelper.eGetFeatureInfo("efeature.EFeatureCompatibleData");
        int fcount = eFeatureCount/2;
        int gcount = eFeatureCount - fcount;
        eTypeData[0][2] = eFeatureDataInfo;
        eTypeData[0][3] = fcount;
        eTypeData[1][2] = eFeatureCompatibleDataInfo;
        eTypeData[1][3] = gcount;
        
    }
    
    // ----------------------------------------------------- 
    //  AbstractStandaloneTest implementation
    // -----------------------------------------------------
            
    @Override
    protected ResourceSet createResourceSet() {        
        return eContextHelper.getResourceSet();
    }
    
    @Override
    protected EditingDomain createEditingDomain(ResourceSet rset) {
        return eContextHelper.getEditingDomain();
    }
            
    @Override
    protected String createFileName(String name) {
        //
        // Is test expecting an empty resource?
        //
        if("testFeatureQueryEmpty".equals(name)) {
            return EMPTY_RESOURCE_TEST;
        }
        // 
        // Use default name
        //
        return super.createFileName(name);
    }

    @Override
    protected void createTestData(String name, Resource eResource) throws Exception {
        //
        // ------------------------------------------------
        //  Create data used by all tests expecting data
        // ------------------------------------------------
        //
        //  Is test expecting an empty resource?
        //
        if(!"testFeatureQueryEmpty".equals(name)) {
            eData = new EFeatureTestData(eResource);
            eData.init(10,(Integer)eTypeData[0][3],(Integer)eTypeData[1][3]);
            eData.save();
        }
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
