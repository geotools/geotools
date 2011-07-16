package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.tests.unit.EFeatureTestData.newIsEqual;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;
import org.geotools.data.efeature.EFeatureDataStore;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureIterator;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.query.EFeatureFilter;
import org.geotools.data.efeature.query.EFeatureQuery;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureHintsTest extends AbstractResourceTest {
    
    private static boolean binary = false;
    
    private int eFeatureCount = 12;
    private EFeatureTestData eData;
    
    private ParameterInfoTestData eParams;
    private EFeatureDataStoreFactory eStoreFactory;
    private EFeatureTestsContextHelper eContextHelper;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

//    @org.junit.Test
//    public void testSingletonHint() {
//        testSingletonHint(false);
//        testSingletonHint(true);
//    }
//    
//    public void testSingletonHint(boolean eDetached) {
//        EFeatureDataStore eStore;
//        Map<String,Serializable> params;
//        try {
//            //
//            // Measure test duration
//            //
//            dTime();
//            //
//            // Prepare store
//            //
//            params = eParams.createParams(eResourceURI.toString(), null);
//            eStore = eStoreFactory.createDataStore(params);
//            Hints hints = new Hints();
//            hints.put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, false);
//            hints.put(EFeatureHints.EFEATURE_VALUES_DETACHED, false);
//            //
//            // Loop over types
//            //
//            for(String eType : new String[]{"efeature.EFeatureData","efeature.EFeatureCompatibleData"}) {
//                //
//                // Prepare loop objects
//                //
//                Query query = new Query(eType,Filter.INCLUDE);
//                query.setHints(hints);
//                EFeatureReader eReader = eStore.getFeatureReader(query);
//                EFeatureHints eHints = eReader.eHints();
//                eHints.eSetValuesDetached(eDetached);
//                //
//                // -------------------------------------------------------
//                //  A.1) Test instance ESimpleFeature mode (attached)
//                // -------------------------------------------------------
//                //
//                query.getHints().put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, false);
//                ESimpleFeature f1 = eReader.next();
//                ESimpleFeature f2 = eReader.next();
//                assertNotSame("Features are same instances ["+eType+",detached:="+eDetached+",singleton:=false]", f1,f2);
//                assertNotSame("EObjects are same instances ["+eType+",detached:="+eDetached+",singleton:=false]", f1.eObject(),f2.eObject());
//                assertNotSame("EFeatures are same instances ["+eType+",detached:="+eDetached+",singleton:=false]", f1.eFeature(),f2.eFeature());
//                //
//                // -------------------------------------------------------
//                //  A.2) Test singleton ESimpleFeature mode
//                // -------------------------------------------------------
//                //
//                eHints.eSetSingletonFeatures(true);
//                f1 = eReader.next();
//                f2 = eReader.next();
//                assertSame("Features are not same instances ["+eType+",detached:="+eDetached+",singleton:=true]", f1,f2);
//                assertSame("EObjects are not same instances ["+eType+",detached:="+eDetached+",singleton:=true]", f1.eObject(),f2.eObject());
//                assertSame("EFeatures are not same instances ["+eType+",detached:="+eDetached+",singleton:=true]", f1.eFeature(),f2.eFeature());
//                //
//                // -----------------------------------------------------------------------------
//                //  A.3) Test instance ESimpleFeature mode again, this time ensuring hint resets
//                // -----------------------------------------------------------------------------
//                //
//                eHints.eSetSingletonFeatures(false);
//                f1 = eReader.next();
//                f2 = eReader.next();
//                assertNotSame("Features are same instances [transitive,"+eType+",detached:="+eDetached+",singleton:=false]", f1,f2);
//                assertNotSame("EObjects are same instances [transitive,"+eType+",detached:="+eDetached+",singleton:=false]", f1.eObject(),f2.eObject());
//                assertNotSame("EFeatures are same instances [transitive,"+eType+",detached:="+eDetached+",singleton:=false]", f1.eFeature(),f2.eFeature());
//                //
//                // Dispose data store
//                //
//                eStore.dispose();
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, e.getMessage(), e);
//            fail(e);
//        }
//    }    
//    
//    @org.junit.Test
//    public void testDetachedHint() {
//        testDetachedHint(true);
//        testDetachedHint(false);
//    }
//    
//    public void testDetachedHint(boolean eSingleton) {
//        EFeatureDataStore eStore;
//        Map<String,Serializable> params;
//        try {
//            //
//            // Prepare common objects
//            //
//            params = eParams.createParams(eResourceURI.toString(), null);
//            eStore = eStoreFactory.createDataStore(params);
//            Hints hints = new Hints();
//            hints.put(EFeatureHints.EFEATURE_SINGLETON_FEATURES, false);
//            hints.put(EFeatureHints.EFEATURE_VALUES_DETACHED, false);
//            //
//            // Loop over types
//            //
//            for(String eType : new String[]{"efeature.EFeatureData","efeature.EFeatureCompatibleData"}) {
//                //
//                // Prepare loop objects
//                //
//                Query query = new Query(eType,Filter.INCLUDE);
//                query.setHints(hints);
//                EFeatureReader eReader = eStore.getFeatureReader(query);
//                EFeatureHints eHints = eReader.eHints();
//                eHints.eSetSingletonFeatures(eSingleton);
//                EAttribute eAttr = ("efeature.EFeatureData".equals(eType) ? 
//                        EFeatureTestsPackage.eINSTANCE.getEFeatureData_Attribute() :
//                            EFeatureTestsPackage.eINSTANCE.getEFeatureCompatibleData_Attribute());
//                //
//                // --------------------------------------------------------------------
//                //  1) Test attached ESimpleFeature values mode (establishes base line)
//                // --------------------------------------------------------------------
//                //
//                ESimpleFeature feature = eReader.next();
//                EFeature eFeature = feature.eFeature();
//                EFeatureInfo eStructure = eFeature.getStructure();
//                List<Object> eValues = feature.getAttributes();
//                assertTrue("Feature and EObject values not same [1,"+eType+",singleton:="+eSingleton+",detached:=false]", 
//                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
//                //
//                // --------------------------------------------------------------------
//                //  2) Test that feature and EObject both change values
//                // --------------------------------------------------------------------
//                //
//                Long v = 1L*(int)(Math.random()*10000);
//                eFeature.eSet(eAttr, v);
//                eValues = feature.getAttributes();
//                assertTrue("Feature and EObject values not same [2,"+eType+",singleton:="+eSingleton+",detached:=false]", 
//                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
//                //
//                // ---------------------------------------------------------------------
//                //  3) Test detached ESimpleFeature values mode (establishes base line)
//                // ---------------------------------------------------------------------
//                //
//                eHints.eSetValuesDetached(true);
//                feature = eReader.next();
//                System.out.println(feature);
//                eFeature = feature.eFeature();
//                System.out.println(eFeature);
//                String eID = eFeature.getID();
//                System.out.println(eID);
//                eStructure = eFeature.getStructure();
//                eValues = feature.getAttributes();
//                assertTrue("Feature and EObject values not same [3,"+eType+",singleton:="+eSingleton+",detached:=true]", 
//                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
//                //
//                // --------------------------------------------------------------------
//                //  4) Test that ESimpleValues values does not change
//                // --------------------------------------------------------------------
//                //
//                v = 1L*(int)(Math.random()*10000);
//                eFeature.eSet(eAttr, v);
//                eValues = feature.getAttributes();
//                assertFalse("Feature and EObject values same [4,"+eType+",singleton:="+eSingleton+",detached:=true]", 
//                        eValues.equals(EFeatureUtils.eGetFeatureValues(eStructure, eFeature, Transaction.AUTO_COMMIT)));
//                //
//                // Dispose data store
//                //
//                eStore.dispose();
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, e.getMessage(), e);
//            fail(e);
//        }
//    }    
    
    @org.junit.Test
    public void testChangeRecorder() {
        try {
            //
            // Prepare common objects
            //
            Map<String,Serializable> params = eParams.createParams(eResourceURI.toString(), null);
            EFeatureDataStore eStore = eStoreFactory.createDataStore(params);

            EAttribute eAttribute = EFeaturePackage.eINSTANCE.getEFeature_ID();
            EObjectCondition eCondition = newIsEqual(eAttribute, "F1");
            WHERE where = new WHERE(eCondition);
            eAttribute = EFeatureTestsPackage.eINSTANCE.getEFeatureData_Attribute();
            
            TreeIterator<EObject> eObjects = eResource.getAllContents();                
            EFeatureFilter eFilter = new EFeatureFilter(where);
            EFeatureQuery eQuery = new EFeatureQuery(eObjects, eFilter);
            EFeatureIterator it = eQuery.iterator();
            EObject eFeature = it.next();        
            Object v = 1200L;
            System.out.println("V: " + v);
            ChangeRecorder r = new ChangeRecorder(eFeature);
            Object o1 = eFeature.eGet(eAttribute);
            System.out.println("2: " + o1);
            Object o2 = eFeature.eGet(eAttribute);
            System.out.println("2: " + o2);
            eFeature.eSet(eAttribute, v);
            ChangeDescription c = r.endRecording();
            c.applyAndReverse();
            c.apply();
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
