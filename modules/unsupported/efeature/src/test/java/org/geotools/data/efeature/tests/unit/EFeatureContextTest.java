package org.geotools.data.efeature.tests.unit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureContextFactory;
import org.geotools.data.efeature.EFeatureContextInfo;
import org.geotools.data.efeature.EFeatureDataStoreInfo;
import org.geotools.data.efeature.EFeatureDomainInfo;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureFolderInfo;
import org.geotools.data.efeature.EFeatureGeometryInfo;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.EStructureInfo;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;

/**
 * This class implements unit tests for {@link EFeatureContext}
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureContextTest extends AbstractStandaloneTest {

    private EFeatureTestsContextHelper eContextHelper;

    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    /** Test constructor */
    @org.junit.Test
    public void testConstructor() {
        //
        // Create private context helper with private context factory.
        //
        EFeatureTestsContextHelper eHelper = 
            new EFeatureTestsContextHelper(new EFeatureContextFactory(),false, false);
        EFeatureContext eContext = eHelper.eContext();
        EFeatureContextInfo eStructure = eContext.eStructure();
        assertState(eStructure,false,false,false);
        assertStatus(eStructure.validate(),EFeatureStatus.SUCCESS);
        assertState(eStructure,true,true,false);
        assertStructure("EFeatureContext structure mismatch",eStructure);
    }    

    /** Test SPI. */
    @org.junit.Test
    public void testSPI() {
        EFeatureContextFactory eFactory = EFeatureFactoryFinder.getContextFactory();
        assertNotNull("EFeatureFactoryFinder failure, " + 
                "default EFeatureContextFactory instance not found", eFactory);
        assertNotNull("SPI failure, EFeatureContextFactory instance not found", eFactory);
        eFactory = eContextHelper.eContextFactory();
        assertNotNull("SPI failure, EFeatureContextFactory instance not found", eFactory);
        EFeatureContext eContext = eContextHelper.eContext();        
        EFeatureContextInfo eInfo = eContext.eStructure();
        assertState(eInfo,false,false,false);
        assertStatus(eInfo.validate(),EFeatureStatus.SUCCESS);
        assertState(eInfo,true,true,false);
        assertStructure("EFeatureContext structure mismatch",eInfo);
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
    public EFeatureContextTest(String name) {
        super(name, "xmi", true, false);
    }

    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureContextTest.class);
    }    

    @Override
    protected void doSetUp() throws Exception {
        //
        // Create default context helper
        //
        eContextHelper = new EFeatureTestsContextHelper(false, false);
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

    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------

    private static void assertStatus(EFeatureStatus eStatus, int type)
    {        
        if(eStatus.getType() != type) {
            if(eStatus.getCause()!=null) {
                eStatus.getCause().printStackTrace();
            }
            fail(eStatus.getMessage());
        }
    }

    private static void assertState(EStructureInfo<?> eInfo, boolean isAvailable, boolean isValid, boolean isDisposed)
    {
        String eName = eInfo.getClass().getSimpleName();
        assertTrue(eName + "#isAvailable mismatch: " +
                "expected " + isAvailable + ", found " + eInfo.isAvailable(),
                eInfo.isAvailable() == isAvailable);
        assertTrue(eName + "#isValid mismatch: " +
                "expected " + isValid + ", found " + eInfo.isAvailable(),
                eInfo.isValid() == isValid);
        assertTrue(eName + "isDisposed mismatch: " +
        	"expected " + isDisposed + ", found " + eInfo.isDisposed(),
                eInfo.isDisposed() == isDisposed);
    }

    private static void assertStructure(String msg,EFeatureContextInfo eInfo)
    {
        assertNotNull(msg+". EFeatureContextFactory instance not set", eInfo.eContext());
        assertTrue(msg+". Unexpected EFeatureDomainInfo count", eInfo.eDomainInfoMap().size()==1);
        assertStructure(msg, eInfo.eDomainInfoMap().values().iterator().next());
    }

    private static void assertStructure(String msg,EFeatureDomainInfo eInfo)
    {
        assertTrue(msg+". Unexpected EFeatureDataStoreInfo count", eInfo.eDataStoreInfoMap().size()==1);
        assertStructure(msg, eInfo.eDataStoreInfoMap().values().iterator().next());
    }    

    private static void assertStructure(String msg,EFeatureDataStoreInfo eInfo)
    {
        assertTrue(msg+". Unexpected EFeatureFolderInfo count", eInfo.eFolderInfoMap().size()==1);
        assertStructure(msg, eInfo.eFolderInfoMap().values().iterator().next());
    }

    private static void assertStructure(String msg,EFeatureFolderInfo eInfo)
    {
        assertTrue(msg+". Unexpected EFeatureInfo count", eInfo.eFeatureInfoMap().size()==2);
        assertStructure(msg, eInfo.eFeatureInfoMap().values().iterator().next());
    }    

    private static void assertStructure(String msg,EFeatureInfo eInfo)
    {
        int pcount = 0;
        int acount = 0;
        int gcount = 0;
        if(EFeatureData.class.getSimpleName().equals(eInfo.eName())) {
            pcount = 8;
            acount = 7;
            gcount = 1;
        } 
        else if(EFeatureCompatibleData.class.getSimpleName().equals(eInfo.eName())) {
            pcount = 5;
            acount = 4;
            gcount = 1;            
        }
        assertEquals(msg+". Unexpected EFeature["+eInfo.eName()+"] property count", pcount, eInfo.eGetAttributeInfoMap(true).size());
        assertEquals(msg+". Unexpected EFeatureAttributeInfo["+eInfo.eName()+"] count", acount, eInfo.eGetAttributeInfoMap(false).size());
        assertStructure(msg, eInfo.eName(), eInfo.eGetAttributeInfoMap(false).values());
        assertEquals(msg+". Unexpected EFeatureGeometryInfo["+eInfo.eName()+"] count", gcount, eInfo.eGetGeometryInfoMap().size());
        assertStructure(msg, eInfo.eGetGeometryInfoMap().values().iterator().next());

    }    

    private static void assertStructure(String msg,String eName, Collection<EFeatureAttributeInfo> eInfos)
    {
        Map<String, Boolean> exists = new HashMap<String, Boolean>();
        if(EFeatureData.class.getSimpleName().equals(eName)) {
            exists.put("SRID", false);
            exists.put("data", false);
            exists.put("simple", false);
            exists.put("default", false);
            exists.put("structure", false);
            exists.put("attribute", false);
        } 
        else if(EFeatureCompatibleData.class.getSimpleName().equals(eName)) {
            exists.put("ID", false);
            exists.put("attribute", false);
        }        
        for(EFeatureAttributeInfo it : eInfos)
        {
            exists.put(it.eName(),true);
        }
        StringBuffer s = new StringBuffer();
        for(String name : exists.keySet())
        {
            if(!exists.get(name)) {
                if(s.length()>0) s.append(",");
                s.append(name);
            }
        }    
        assertTrue(msg+". Missing attributes: " + s.toString(),s.length()==0);
    }

    private static void assertStructure(String msg,EFeatureGeometryInfo eInfo)
    {
        assertEquals(msg,"geometry",eInfo.eName());
    }    

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

}
