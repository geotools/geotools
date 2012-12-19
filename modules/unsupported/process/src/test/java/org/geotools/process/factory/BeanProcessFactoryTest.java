package org.geotools.process.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.geotools.data.Parameter;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.FactoryIteratorProvider;
import org.geotools.factory.GeoTools;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.RenderingProcess;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.util.SimpleInternationalString;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Tests some processes that do not require integration with the application context
 * 
 * @author Andrea Aime - OpenGeo
 * @author Martin Davis - OpenGeo
 * 
 *
 * @source $URL$
 */
public class BeanProcessFactoryTest {
    
    /**
     * Constant used for absolute reference tests
     */
    public static final Rectangle DEFAULT_RECTANGLE = new Rectangle(0, 0, 10, 10);

    public class BeanProcessFactory extends AnnotatedBeanProcessFactory {

        public BeanProcessFactory() {
            super(new SimpleInternationalString("Some bean based processes custom processes"),
                    "bean", 
                    IdentityProcess.class,
                    DefaultsProcess.class,
                    VectorIdentityRTProcess.class);
        }

    }

    BeanProcessFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new BeanProcessFactory();

        // check SPI will see the factory if we register it using an iterator
        // provider
        GeoTools.addFactoryIteratorProvider(new FactoryIteratorProvider() {

            public <T> Iterator<T> iterator(Class<T> category) {
                if (ProcessFactory.class.isAssignableFrom(category)) {
                    return (Iterator<T>) Collections.singletonList(factory).iterator();
                } else {
                    return null;
                }
            }
        });
    }

    @Test
    public void testNames() {
        Set<Name> names = factory.getNames();
        assertTrue(names.size() > 0);
        // System.out.println(names);
        // Identity
        assertTrue(names.contains(new NameImpl("bean", "Identity")));
    }
    
    @Test
    public void testDescribeIdentity() {
        NameImpl name = new NameImpl("bean", "Identity");
        DescribeProcess describeProcessAnno = IdentityProcess.class.getAnnotation(DescribeProcess.class);

        InternationalString desc = factory.getDescription(name);
        assertTrue(desc.toString().equals(describeProcessAnno.description()));
        InternationalString title = factory.getTitle(name);
        assertTrue(title.toString().equals(describeProcessAnno.title()));

        Map<String, Parameter<?>> params = factory.getParameterInfo(name);
        assertEquals(1, params.size());

        Parameter<?> input = params.get("input");
        assertEquals(Object.class, input.type);
        assertTrue(input.required);

        Map<String, Parameter<?>> result = factory.getResultInfo(name, null);
        assertEquals(1, result.size());
        Parameter<?> identity = result.get("value");
        assertEquals(Object.class, identity.type);
    }

    @Test
    public void testExecuteIdentity() throws ProcessException {
        // prepare a mock feature collection
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        final ReferencedEnvelope re = new ReferencedEnvelope(-10, 10, -10, 10, null);

        org.geotools.process.Process p = factory.create(new NameImpl("bean", "Identity"));
        Map<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("input", re);
        Map<String, Object> result = p.execute(inputs, null);

        assertEquals(1, result.size());
        ReferencedEnvelope computed = (ReferencedEnvelope) result.get("value");
        assertEquals(re, computed);
        assertSame(re, computed);
    }
    
    @Test
    public void testSPI() throws Exception {
        NameImpl boundsName = new NameImpl("bean", "Identity");
        ProcessFactory factory = Processors.createProcessFactory(boundsName);
        assertNotNull(factory);
        assertTrue(factory instanceof BeanProcessFactory);

        org.geotools.process.Process buffer = Processors.createProcess(boundsName);
        assertNotNull(buffer);
    }

    @Test
    public void testInvertQuery() throws ProcessException {
        // prepare a mock feature collection
        SimpleFeatureCollection data = buildTestFeatures();
        
        org.geotools.process.Process transformation = factory.create(new NameImpl("bean", "VectorIdentityRT"));
        Map<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("data", data);
        inputs.put("value", 10);
        
        RenderingProcess tx = (RenderingProcess) transformation;
        Query dummyQuery = tx.invertQuery(inputs, null, null);
        
        Map<String, Object> result = transformation.execute(inputs, null);

        assertEquals(1, result.size());
        
        SimpleFeatureCollection computed = (SimpleFeatureCollection) result.get("result");
        
        assertEquals(data, computed);
        assertEquals(data, computed);
        assertSame(data, computed);
    }
    
    @Test
    public void testDefaultValues() throws Exception {
        Process defaults = factory.create(new NameImpl("bean", "Defaults"));
        Map<String, Object> results = defaults.execute(Collections.EMPTY_MAP, null);
        
        // double check all defaults have been applied
        assertEquals("default string", results.get("string"));
        assertEquals(new WKTReader().read("POINT(0 0)"), results.get("geometry"));
        assertEquals(1, results.get("int"));
        assertEquals(0.65e-10, results.get("double"));
        assertEquals(AxisOrder.EAST_NORTH, results.get("axisOrder"));
        assertEquals(Short.MAX_VALUE, results.get("short"));
        assertEquals(DefaultsProcess.GREET_DEFAULT, results.get("greet"));
        assertEquals(DEFAULT_RECTANGLE, results.get("rect"));
    }


    private SimpleFeatureCollection buildTestFeatures()
    {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        // this should be populated correctly
        CoordinateReferenceSystem crs = null;
		tb.add("geom", Geometry.class, crs );
        tb.add("count", Integer.class);
        SimpleFeatureType schema = tb.buildFeatureType();

        ListFeatureCollection fc = new ListFeatureCollection(schema);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);

        GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());

        Geometry point = factory.createPoint(new Coordinate(10, 10));
        fb.add(point);
        fb.add(5);
        
        fc.add(fb.buildFeature(null));

        return fc;
    }
}
