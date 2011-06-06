package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.tests.unit.EFeatureTestData.newCondition;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.statements.WHERE;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.query.EFeatureFilter;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureFilterTest extends AbstractStandaloneTest {
    
    /** 
     * Static logger for all {@link EFeatureFilterTest} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureFilterTest.class); 

    private int eFeatureCount = 200;
    
    private EFeatureTestData eData;    
    private EFeatureInfo eFeatureDataInfo;
    private EFeatureInfo eFeatureCompatibleDataInfo;
    private EFeatureTestsContextHelper eContextHelper;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testFeatureFilters() {
        try {
            EObjectCondition eCondition = newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "1");
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "52"));
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "173"));
            eCondition = eCondition.OR(newCondition(EFeaturePackage.eINSTANCE.getEFeature_ID(), "200"));
            WHERE where = new WHERE(eCondition);
            EFeatureFilter eFilter = new EFeatureFilter(eFeatureDataInfo, where);
            //EFeatureQuery eQuery = new EFeatureQuery(null, where);
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
    public EFeatureFilterTest(String name) {
        super(name, "xmi", true, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureFilterTest.class);
    }
        
    @Override
    protected void doSetUp() throws Exception {
        //
        // Initialize commonly used objects
        //
        eContextHelper = new EFeatureTestsContextHelper(true, false);
        eFeatureDataInfo = eContextHelper.eGetFeatureInfo("efeature.EFeatureData");
        eFeatureCompatibleDataInfo = eContextHelper.eGetFeatureInfo("efeature.EFeatureCompatibleData");
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
        if("testFeatureFilters".equals(getName())) 
        {
            eData = new EFeatureTestData(eResource);
            int fcount = eFeatureCount/2;
            int gcount = eFeatureCount - fcount;
            eData.init(10,fcount,gcount);
            eData.save();
        }
        //eData.print();
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
