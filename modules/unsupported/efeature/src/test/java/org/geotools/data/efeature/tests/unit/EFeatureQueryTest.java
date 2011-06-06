package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.tests.unit.EFeatureTestData.newCondition;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureIterator;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.query.EFeatureFilter;
import org.geotools.data.efeature.query.EFeatureQuery;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureQueryTest extends AbstractStandaloneTest {
    
    /** 
     * Static logger for all {@link EFeatureQueryTest} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureQueryTest.class); 

    private int eFeatureCount = 2000;
    private EFeatureTestData eData;
    private Object[][] eTypeData = new Object[][]{
        {"efeature.EFeatureData",EFeatureData.class,null,0},
        {"efeature.EFeatureCompatibleData",EFeatureCompatibleData.class,null,0}};
    
    private ParameterInfoTestData eParams;
    private EFeatureInfo eFeatureDataInfo;
    private EFeatureInfo eFeatureCompatibleDataInfo;
    private EFeatureDataStoreFactory eStoreFactory;
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
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eResource.getAllContents(), eFilter);
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
    public void testFeatureQueryNoMatch() {
        try {
            
            EObjectCondition eCondition = newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), Integer.toString(Integer.MAX_VALUE));
            WHERE where = new WHERE(eCondition);
            
            dTime();
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                EFeatureInfo eInfo = (EFeatureInfo)type[2];
                int count = 0;
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eResource.getAllContents(), eFilter);
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
    public void testFeatureQueryMatch() {
        try {
            
            EObjectCondition eCondition = newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F1");
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F52"));
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F173"));
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "F200"));
            WHERE where = new WHERE(eCondition);
            
            dTime();
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                EFeatureInfo eInfo = (EFeatureInfo)type[2];
                int count = 0;
                EFeatureFilter eFilter = new EFeatureFilter(eInfo, where);
                EFeatureQuery eQuery = new EFeatureQuery(eResource.getAllContents(), eFilter);
                EFeatureIterator it = eQuery.iterator();
                while(it.hasNext()) {
                    EObject eObject = it.next();
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
        eParams = new ParameterInfoTestData();
        eContextHelper = new EFeatureTestsContextHelper(true, false);
        eStoreFactory = EFeatureFactoryFinder.getDataStoreFactory();
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
    protected void createTestData(Resource eResource) throws Exception {
        //
        // Optimize test speed by selectively creating data
        //
        if("testFeatureQueryMatch".equals(getName())) 
        {
            eData = new EFeatureTestData(eResource);
            eData.init(10,(Integer)eTypeData[0][3],(Integer)eTypeData[1][3]);
            eData.save();
        } 
        if("testFeatureQueryNoMatch".equals(getName())) {
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
