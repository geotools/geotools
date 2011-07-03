package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.tests.unit.EFeatureTestData.newIsEqual;

import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
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
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 */
public class EFeatureQueryTest extends AbstractResourceTest {
    
    private static final String EFEATURE_QUERY_TEST_RANDOM = "EFeatureQueryTest_random";
    
    private static final String EFEATURE_QUERY_TEST_GEOMETRY = "EFeatureQueryTest_geometry";
    
    private static WKTReader READER = new WKTReader();
    
    private static final String BBOX = "POLYGON ((0 0, 100 0, 100 100, 0 100, 00 00))";
    
    private static final Point[] POINTS = new Point[]{
        create(Point.class,"POINT (30 30)"),
        create(Point.class,"POINT (40 40)")
    };

    private static final LineString[] LINESTRINGS = new LineString[]{
        create(LineString.class,"LINESTRING (20 20, 40 20)")
    };
    
    private static final Polygon[] POLYGONS = new Polygon[]{
        //
        // P0 BBOX | INTERSECTS {P1}, TOUCHES {P2}, CONTAINS {P3}
        //
        create(Polygon.class,"POLYGON ((10 10, 30 10, 30 30, 10 30, 10 10))"),
        create(Polygon.class,"POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20))"),
        create(Polygon.class,"POLYGON ((00 30, 20 30, 20 50, 00 50, 00 30))"),
        create(Polygon.class,"POLYGON ((15 15, 25 15, 25 25, 15 25, 15 15))"),
        create(Polygon.class,"POLYGON ((50 10, 70 10, 70 30, 50 30, 50 10))")
    };    
    
    private static final Geometry[] GEOMETRIES = EFeatureUtils.concat(
            Geometry.class, POINTS,LINESTRINGS,POLYGONS);
    
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
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(where);
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
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(where);
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
            EAttribute eAttribute = EFeaturePackage.eINSTANCE.getEFeature_ID();
            EObjectCondition eCondition = newIsEqual(eAttribute, "F1");
            eCondition = eCondition.OR(newIsEqual(eAttribute, "F22"));
            eCondition = eCondition.OR(newIsEqual(eAttribute, "F73"));
            eCondition = eCondition.OR(newIsEqual(eAttribute, "F100"));
            eCondition = eCondition.OR(newIsEqual(eAttribute, "F101", eFeatureCompatibleDataInfo));
            WHERE where = new WHERE(eCondition);
            
            dTime();
            
            for(Object[] type : eTypeData) {
                String eType = type[0].toString();                
                int count = 0;
                TreeIterator<EObject> eObjects = eResource.getAllContents();                
                EFeatureFilter eFilter = new EFeatureFilter(where);
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
    public void testFeatureLiteralCQL() {
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
    
    @org.junit.Test
    public void testFeatureGeometryMatch() {
        try {
            // 
            // ---------------------------------------------------
            //  This test assumes that eight EFeatureData 
            //  instances are added to the resource. The test 
            //  uses this information to check the matched 
            //  geometries. 
            // ---------------------------------------------------
            //
            EAttribute eAttribute = EFeatureTestsPackage.eINSTANCE.getEFeatureData_Geometry();
            EObjectCondition eCondition = newIsEqual(eAttribute, GEOMETRIES[0]);
            for(int i=1;i<GEOMETRIES.length;i++) {
                eCondition = eCondition.OR(newIsEqual(eAttribute, GEOMETRIES[i]));
            }
            WHERE where = new WHERE(eCondition);
            
            dTime();
            int count = 0;
            TreeIterator<EObject> eObjects = eResource.getAllContents();                
            EFeatureFilter eFilter = new EFeatureFilter(where);
            EFeatureQuery eQuery = new EFeatureQuery(eObjects, eFilter);
            EFeatureIterator it = eQuery.iterator();
            while(it.hasNext()) {
                EObject eObject = it.next();
                assertTrue("EObject " + eObject + " is not an " +
                        "instance of EFeatureData", eObject instanceof EFeatureData);
                EFeatureData<?,?> eFeatureData = (EFeatureData<?,?>)eObject;
                Object a = eFeatureData.getAttribute();
                Object g = eFeatureData.getGeometry();
                assertEquals("Unexpected EFeatureData geometry value found",
                        GEOMETRIES[(Integer)a].toString(),g.toString());
                count++;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }
    
    @org.junit.Test
    public void testFeatureGeometryCQL() {
        try {
         // -----------------------------------------------
            //  Test OpenGIS Filter -> EMF Where conversion
            // -----------------------------------------------
            
            Filter filter = CQL.toFilter("WITHIN(geometry,"+BBOX+")");
            
            dTime();
            
            int count = 0;
            TreeIterator<EObject> eObjects = eResource.getAllContents();                
            EFeatureQuery eQuery = EFeatureUtils.toEFeatureQuery(eFeatureDataInfo, eObjects, filter);
            EFeatureIterator it = eQuery.iterator();
            while(it.hasNext()) {
                EObject eObject = it.next();
                assertTrue("EObject " + eObject + " is not an " +
                        "instance of EFeatureData", eObject instanceof EFeatureData);
                EFeatureData<?,?> eFeatureData = (EFeatureData<?,?>)eObject;
                Object a = eFeatureData.getAttribute();
                Object g = eFeatureData.getGeometry();
                assertEquals("Unexpected EFeatureData geometry value found",
                        GEOMETRIES[(Integer)a].toString(),g.toString());
                count++;
            }
            assertEquals("Unexpected number of EFeatureData instances found",GEOMETRIES.length,count);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail(e);
        }
    }    
    
    @org.junit.Test
    public void testFeatureGeometryECQL() {
        try {
         // -----------------------------------------------
            //  Test OpenGIS Filter -> EMF Where conversion
            // -----------------------------------------------
            
            Filter filter = ECQL.toFilter("CONTAINS("+BBOX+",geometry)");
            
            dTime();
            
            int count = 0;
            TreeIterator<EObject> eObjects = eResource.getAllContents();                
            EFeatureQuery eQuery = EFeatureUtils.toEFeatureQuery(eFeatureDataInfo, eObjects, filter);
            EFeatureIterator it = eQuery.iterator();
            while(it.hasNext()) {
                EObject eObject = it.next();
                assertTrue("EObject " + eObject + " is not an " +
                        "instance of EFeatureData", eObject instanceof EFeatureData);
                EFeatureData<?,?> eFeatureData = (EFeatureData<?,?>)eObject;
                Object a = eFeatureData.getAttribute();
                Object g = eFeatureData.getGeometry();
                assertEquals("Unexpected EFeatureData geometry value found",
                        GEOMETRIES[(Integer)a].toString(),g.toString());
                count++;
            }
            assertEquals("Unexpected number of EFeatureData instances found",GEOMETRIES.length,count);
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
        //
        // Only initialize if the test expects it to be
        //
        if(!getName().startsWith("testFeatureGeometry")) {
            int fcount = eFeatureCount/2;
            int gcount = eFeatureCount - fcount;
            eTypeData[0][2] = eFeatureDataInfo;
            eTypeData[0][3] = fcount;
            eTypeData[1][2] = eFeatureCompatibleDataInfo;
            eTypeData[1][3] = gcount;
        }
        
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
        } else if(name.startsWith("testFeatureGeometry")) {
            return EFEATURE_QUERY_TEST_GEOMETRY;
        }
        // 
        // All other use random data
        //
        return EFEATURE_QUERY_TEST_RANDOM;
    }

    @Override
    protected void createTestData(String name, Resource eResource) throws Exception {
        //
        // ------------------------------------------------
        //  Create data used by all tests expecting data
        // ------------------------------------------------
        //
        //  Is test expecting geometry data?
        //
        if(name.startsWith("testFeatureGeometry")) {
            eData = new EFeatureTestData(eResource);
            eData.addNonGeoEObjects(eFeatureCount);
            int count = GEOMETRIES.length;
            Integer[] a = new Integer[count];
            for(int i=0;i<count;i++) {
                a[i] = i;
            }
            eData.addFeatureData(0,count,a,GEOMETRIES);
        }
        //
        //  Is test expecting an empty resource?
        //
        else if(!"testFeatureQueryEmpty".equals(name)) {
            eData = new EFeatureTestData(eResource);
            eData.random(10,(Integer)eTypeData[0][3],(Integer)eTypeData[1][3]);
            eData.save();
        }
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    protected static final <T extends Geometry> T create(Class<T> type, String wkt) {
        try {
            return type.cast(READER.read(wkt));
        } catch (ParseException e) {
            fail(e.getMessage());
        }        
        return null;
    }
    
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
}
