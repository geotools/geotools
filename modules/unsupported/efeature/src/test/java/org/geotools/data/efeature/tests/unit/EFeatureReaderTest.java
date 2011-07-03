package org.geotools.data.efeature.tests.unit;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureDataStore;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureReader;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.opengis.feature.simple.SimpleFeature;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureReaderTest extends AbstractResourceTest {
    
    private static boolean binary = false;
    
    private int eFeatureCount = 200;
    private EFeatureTestData eData;
    private Object[][] eTypeData = new Object[][]{
        {"efeature.EFeatureCompatibleData",EFeatureCompatibleData.class,0},
        {"efeature.EFeatureData",EFeatureData.class,0}};
    
    private ParameterInfoTestData eParams;
    private EFeatureDataStoreFactory eStoreFactory;
    private EFeatureTestsContextHelper eContextHelper;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testFeatureReaders() {
        EFeatureDataStore eStore;
        Map<String,Serializable> params;
        try {
            params = eParams.createParams(eResourceURI.toString(), null);
            eStore = eStoreFactory.createDataStore(params);
            dTime();
            for(Object[] it : eTypeData) {
                //
                // Assert structure
                //
                String eType = it[0].toString();                
                Class<?> cls = (Class<?>)it[1];
                int count = 0;
                int icount = Integer.valueOf(it[2].toString());
                EFeatureReader eReader = eStore.getFeatureReader(eType);
                while(eReader.hasNext()) {
                    SimpleFeature feature = eReader.next();
                    assertTrue("Feature[" + count + "]: does not implement ESimpleFeature",feature instanceof ESimpleFeature);
                    EObject eObject = ((ESimpleFeature)feature).eObject();
                    assertNotNull("Feature[" + count + "]: is not contained by an EObject",eObject);                    
                    assertTrue("EObject[" + count + "]: returned by ESimpleFeature is not an " + eType + " instance",cls.isInstance(eObject));
                    EFeature eFeature = ((ESimpleFeature)feature).eFeature();
                    assertNotNull("Feature[" + count + "]: is not contained by an EFeature",eFeature);
                    //assertTrue("EFeature data and Feature are not identical",feature==eFeature.getData());
                    Object n = feature.getAttribute("attribute");
                    assertNotNull("Attribute[" + count + "]: data is null",n);
                    Object g = feature.getAttribute("geometry");
                    assertNotNull("Geometry[" + count + "]: data is null",g);
                    count++;
                }
                assertEquals("Feature count mismatch",icount,count);
                trace("Count["+eType+"]: " + count,TIME_DELTA);
                //
                // Assert validation optimization
                //
                //eReader.reset();
                //while(eReader.hasNext());
                 
            }
            eStore.dispose();
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
    public EFeatureReaderTest(String name) {
        super(name, binary ? "bin" : "xmi", true, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureReaderTest.class);
    }
        
    @Override
    protected void doSetUp() throws Exception {
        //
        // Initialize commonly used objects
        //
        eParams = new ParameterInfoTestData();
        eContextHelper = new EFeatureTestsContextHelper(true, binary);
        eStoreFactory = EFeatureFactoryFinder.getDataStoreFactory();
        //
        // Update test data
        //
        int fcount = eFeatureCount/2;
        int gcount = eFeatureCount - fcount;
        eTypeData[0][2] = fcount;
        eTypeData[1][2] = gcount;        
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
    protected void createTestData(String name, Resource eResource) throws Exception {
        //
        // Create data used by all tests
        //
        eData = new EFeatureTestData(eResource);
        eData.random(10,(Integer)eTypeData[0][2],(Integer)eTypeData[1][2]);
        eData.save();
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
