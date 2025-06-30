/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.process.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
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
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.locationtech.jts.io.WKTReader;

/**
 * Tests some processes that do not require integration with the application context
 *
 * @author Andrea Aime - OpenGeo
 * @author Martin Davis - OpenGeo
 */
public class BeanProcessFactoryTest {

    /** Constant used for absolute reference tests */
    public static final Rectangle DEFAULT_RECTANGLE = new Rectangle(0, 0, 10, 10);

    public static class BeanProcessFactory extends AnnotatedBeanProcessFactory {

        public BeanProcessFactory() {
            super(
                    new SimpleInternationalString("Some bean based processes custom processes"),
                    "bean",
                    IdentityProcess.class,
                    DefaultsProcess.class,
                    VectorIdentityRTProcess.class,
                    MetaProcess.class);
        }
    }

    BeanProcessFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new BeanProcessFactory();

        // check SPI will see the factory if we register it using an iterator
        // provider
        GeoTools.addFactoryIteratorProvider(new FactoryIteratorProvider() {

            @Override
            public <T> Iterator<T> iterator(Class<T> category) {
                if (ProcessFactory.class.isAssignableFrom(category)) {
                    @SuppressWarnings("unchecked")
                    Iterator<T> result =
                            (Iterator<T>) Collections.singletonList(factory).iterator();
                    return result;
                } else {
                    return null;
                }
            }
        });
    }

    @Test
    public void testNames() {
        Set<Name> names = factory.getNames();
        assertFalse(names.isEmpty());
        // System.out.println(names);
        // Identity
        assertTrue(names.contains(new NameImpl("bean", "Identity")));
    }

    @Test
    public void testDescribeIdentity() {
        NameImpl name = new NameImpl("bean", "Identity");
        DescribeProcess describeProcessAnno = IdentityProcess.class.getAnnotation(DescribeProcess.class);

        InternationalString desc = factory.getDescription(name);
        assertEquals(desc.toString(), describeProcessAnno.description());
        InternationalString title = factory.getTitle(name);
        assertEquals(title.toString(), describeProcessAnno.title());

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
        Map<String, Object> inputs = new HashMap<>();
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
    public void testRenderingTransformation() throws ProcessException {
        // prepare a mock feature collection
        SimpleFeatureCollection data = buildTestFeatures();

        org.geotools.process.Process transformation = factory.create(new NameImpl("bean", "VectorIdentityRT"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("data", data);
        inputs.put("value", 10);

        RenderingProcess tx = (RenderingProcess) transformation;
        // just making sure it does not explode?
        tx.invertQuery(inputs, null, null);
        assertTrue(tx.clipOnRenderingArea(inputs));

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
        Map<String, Object> results = defaults.execute(Collections.emptyMap(), null);

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

    @Test
    public void testMinMaxAcceptedValues() throws Exception {
        // test that the annotation is correctly generating the parameter metadata
        Map<String, Parameter<?>> params = factory.getParameterInfo(new NameImpl("bean", "Defaults"));
        assertEquals(2.0, params.get("int").metadata.get(Parameter.MAX));
        assertEquals(-1.0, params.get("int").metadata.get(Parameter.MIN));
        assertEquals(2.5, params.get("double").metadata.get(Parameter.MAX));
        assertEquals(-1.5, params.get("double").metadata.get(Parameter.MIN));
        // check the null values with a  parameter that does not have that annotation parameter
        // filled
        assertNull(params.get("short").metadata.get(Parameter.MAX));
        assertNull(params.get("short").metadata.get(Parameter.MIN));
    }

    @Test
    public void testMetadata() throws Exception {
        // check input metadata
        NameImpl name = new NameImpl("bean", "Meta");
        Map<String, Parameter<?>> params = factory.getParameterInfo(name);
        assertEquals(2, params.size());
        Parameter<?> ext = params.get("extension");
        assertEquals("shp", ext.metadata.get(Parameter.EXT));
        Parameter<?> pwd = params.get("password");
        assertEquals("true", pwd.metadata.get(Parameter.IS_PASSWORD));

        // check output metadata
        Map<String, Parameter<?>> results = factory.getResultInfo(name, null);
        assertEquals(1, results.size());
        Parameter<?> result = results.get("value");
        assertEquals("application/shapefile,application/json", result.metadata.get("mimeTypes"));
    }

    private SimpleFeatureCollection buildTestFeatures() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        // this should be populated correctly
        CoordinateReferenceSystem crs = null;
        tb.add("geom", Geometry.class, crs);
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
