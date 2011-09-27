package org.geotools.data.efeature.tests.unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.FeatureWriter;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.EFeatureDataStore;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.EFeatureWriter;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 *
 * @source $URL$
 */
public class EFeatureWriterTest extends AbstractResourceTest {
    
    private static boolean binary = false;
    
    private int eFeatureCount = 8;
    private EFeatureTestData eData;
    private Object[][] eTypeData = new Object[][]{
        {"efeature.EFeatureCompatibleData",EFeatureCompatibleData.class,0},
        {"efeature.EFeatureData",EFeatureData.class,0}};
    
    private ParameterInfoTestData eParams;
    private EFeatureDataStoreFactory eStoreFactory;
    private EFeatureTestsContextHelper eContextHelper;
    private Filter filter;
   
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testFeatureWriter() {
        EFeatureDataStore eStore;
        Map<String,Serializable> params;
        try {
            //
            // Get a EFeatureDataStore instance
            //
            params = eParams.createParams(eResourceURI.toString(), null);
            eStore = eStoreFactory.createDataStore(params);
            //
            // Looping over all EFeature types
            //            
            for(Object[] it : eTypeData) {
                //
                // Reset difference time stamp
                //
                dTime();
//                //
//                // Collect type information
//                //
//                int count = 0;
//                String eType = it[0].toString();
//                //
//                // Get writer for given type
//                //
//                FeatureWriter<SimpleFeatureType, SimpleFeature> eWriter = eStore.getEFeatureWriterUpdate(eType, filter, null);
//                //
//                // Update all items
//                //
//                while(eWriter.hasNext()) {
//                    //
//                    // Get next SimpleFeature and EFeature
//                    //
//                    SimpleFeature feature = eWriter.next();
//                    //
//                    // Modify all attributes
//                    //
//                    List<Object> eNewValues = modify(feature.getAttributes());
//                    feature.setAttributes(eNewValues);
//                }
//                //
//                // Notify progress
//                //
//                trace("Count["+eType+"]: " + count, TIME_DELTA);
                 
            }
            //
            // Finished
            //
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
    public EFeatureWriterTest(String name) {
        super(name, binary ? "bin" : "xmi", false, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureWriterTest.class);
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
        //
        // Create ID filter
        //
        StringBuffer eIDs = new StringBuffer();
        int count = eFeatureCount/2;
        for(int i=1;i<=count;i++) {
            if(eIDs.length()>0) eIDs.append(" OR ");
            eIDs.append("ID='F"+i+"'");
        }
        filter = CQL.toFilter(eIDs.toString());
        
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected List<Object> modify(List<Object> values) {
        List<Object> modified = new ArrayList<Object>(values.size());
        for(Object it : values) {
            //
            // Make value the same (change can then be detected) 
            //
            Object v = it;
            //
            // Check data type and do a random change.
            //
            if(DataTypes.isNumeric(it)) {
                if(it instanceof Integer) {
                    
                } else if(it instanceof Double) {
                    v = ((Double)it).doubleValue()*Math.random();                    
                } else if(it instanceof Float) {
                    v = ((Float)it).floatValue()*Math.random();                   
                } else if(it instanceof Byte) {
                    v = ((Byte)it).byteValue()*Math.random();                                       
                } else if(it instanceof Integer) {
                    v = ((Integer)it).intValue()*Math.random();                                       
                } else if(it instanceof Short) {
                    v = ((Short)it).shortValue()*Math.random();
                } else if(it instanceof Long) {
                    v = ((Long)it).longValue()*Math.random();
                } else if(it instanceof Character) {
                    v = ((Character)it).charValue()*Math.random();
                }                
            } else if(DataTypes.isBoolean(it,true)) {
                modified.add(!Boolean.valueOf(it.toString()));
            } else if(DataTypes.isGeometry(it)) {
                Geometry g = (Geometry)it;
                g = g.getFactory().createGeometry(g);
                Coordinate[] c = g.getCoordinates();
                c[0].x = c[0].x*c[0].x*Math.random()*100;
                c[0].y = c[0].y*c[0].y*Math.random()*100;
                modified.add(g);
            } else if(DataTypes.isDate(it)) {
                modified.add(Calendar.getInstance().getTime());
            } else if(DataTypes.isString(it)) {
                modified.add(((String)it)+((String)it));
            } 
            //
            // Verify that data has changed
            //
            assertNotSame("Value not modified", it, v);
            //
            // Add to modified 
            //
            modified.add(v);
        }
        //
        // Finished
        //
        return modified;
    }
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
