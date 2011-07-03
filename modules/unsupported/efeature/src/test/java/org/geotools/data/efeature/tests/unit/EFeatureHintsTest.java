package org.geotools.data.efeature.tests.unit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureDataStore;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureReader;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.opengis.filter.Filter;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureHintsTest extends AbstractResourceTest {
    
    private static boolean binary = false;
    
    private int eFeatureCount = 6;
    private EFeatureTestData eData;
    
    private ParameterInfoTestData eParams;
    private EFeatureDataStoreFactory eStoreFactory;
    private EFeatureTestsContextHelper eContextHelper;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testSingletonHint() {
        EFeatureDataStore eStore;
        Map<String,Serializable> params;
        try {
            //
            // Measure test duration
            //
            dTime();
            //
            // Prepare store
            //
            params = eParams.createParams(eResourceURI.toString(), null);
            eStore = eStoreFactory.createDataStore(params);
            //
            // Loop over types
            //
            for(String eType : new String[]{"efeature.EFeatureData","efeature.EFeatureCompatibleData"}) {
                //
                // Prepare EFeatureReader
                //
                Query query = new Query(eType,Filter.INCLUDE);
                EFeatureReader eReader = eStore.getFeatureReader(query);
                //
                // Test unique ESimpleFeature mode
                //
                query.getHints().put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, false);
                ESimpleFeature f1 = eReader.next();
                ESimpleFeature f2 = eReader.next();
                assertNotSame("Features are same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1,f2);
                assertNotSame("EObjects are same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1.eObject(),f2.eObject());
                assertNotSame("EFeatures are same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1.eFeature(),f2.eFeature());
                //
                // Test unique ESimpleFeature mode
                //
                query.getHints().put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, true);
                f1 = eReader.next();
                f2 = eReader.next();
                assertSame("Features are not same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==true]", f1,f2);
                assertSame("EObjects are not same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==true]", f1.eObject(),f2.eObject());
                assertSame("EFeatures are not same instances ["+eType+",EFEATURE_SINGLETON_FEATURES==true]", f1.eFeature(),f2.eFeature());
                //
                // Test unique ESimpleFeature mode again, this time ensuring hint is honored 
                //
                query.getHints().put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, false);
                f1 = eReader.next();
                f2 = eReader.next();
                assertNotSame("Features are same instances [transitive,"+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1,f2);
                assertNotSame("EObjects are same instances [transitive,"+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1.eObject(),f2.eObject());
                assertNotSame("EFeatures are same instances [transitive,"+eType+",EFEATURE_SINGLETON_FEATURES==false]", f1.eFeature(),f2.eFeature());
                //
                // Dispose data store
                //
                eStore.dispose();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }
    
    @org.junit.Test
    public void testDetachedHint() {
        EFeatureDataStore eStore;
        Map<String,Serializable> params;
        try {
            //
            // Measure test duration
            //
            dTime();
            //
            // Prepare store
            //
            params = eParams.createParams(eResourceURI.toString(), null);
            eStore = eStoreFactory.createDataStore(params);
            //
            // Loop over types
            //
            for(String eType : new String[]{"efeature.EFeatureData","efeature.EFeatureCompatibleData"}) {
                //
                // Prepare EFeatureReader
                //
                Query query = new Query(eType,Filter.INCLUDE);
                EFeatureReader eReader = eStore.getFeatureReader(query);
                //
                // Test ESimpleFeature values attached mode
                //
                query.getHints().put(EFeatureHints.EFEATURE_VALUES_DETACHED, false);
                //
                // Collect test information. 
                //
                ESimpleFeature f1 = eReader.next();
                EFeature eFeature = f1.eFeature();
                EFeatureInfo eStructure = eFeature.getStructure();
                List<Object> eValues = f1.getAttributes();
                //
                // Assert that ESimpleFeature and EObject contains the same Feature values
                //
                assertTrue("Features values are not same ["+eType+",EFEATURE_VALUES_DETACHED==false]", 
                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
                //
                // Change value and assert again, this time expecting a difference
                //
                EAttribute eAttr = ("efeature.EFeatureData".equals(eType) ? 
                        EFeatureTestsPackage.eINSTANCE.getEFeatureData_Attribute() :
                            EFeatureTestsPackage.eINSTANCE.getEFeatureCompatibleData_Attribute());
                eFeature.eSet(eAttr, 10L);
                if("efeature.EFeatureCompatibleData".equals(eType)) {
                    Object eData = eFeature.eGet(EFeaturePackage.eINSTANCE.getEFeature_Data());
                }
                eValues = f1.getAttributes();
                assertTrue("Features values are same ["+eType+",EFEATURE_VALUES_DETACHED==false]", 
                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
                //
                // Dispose data store
                //
                eStore.dispose();
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
    public EFeatureHintsTest(String name) {
        super(name, binary ? "bin" : "xmi", true, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureHintsTest.class);
    }
        
    @Override
    protected void doSetUp() throws Exception {
        //
        // Initialize commonly used objects
        //
        eParams = new ParameterInfoTestData();
        eContextHelper = new EFeatureTestsContextHelper(true, binary);
        eStoreFactory = EFeatureFactoryFinder.getDataStoreFactory();
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
        eData.random(0,eFeatureCount,eFeatureCount);
        eData.save();
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
