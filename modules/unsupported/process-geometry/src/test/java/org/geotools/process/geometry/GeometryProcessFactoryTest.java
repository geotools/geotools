package org.geotools.process.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class GeometryProcessFactoryTest {

    GeometryProcessFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new GeometryProcessFactory();
    }

    @Test
    public void testNames() {
        Set<Name> names = factory.getNames();
        Assert.assertFalse(names.isEmpty());
        // System.out.println(names);
        Assert.assertTrue(names.contains(new NameImpl("geo", "buffer")));
        Assert.assertTrue(names.contains(new NameImpl("geo", "union")));
    }

    @Test
    public void testDescribeBuffer() {
        NameImpl bufferName = new NameImpl("geo", "buffer");
        InternationalString desc = factory.getDescription(bufferName);
        Assert.assertNotNull(desc);

        Map<String, Parameter<?>> params = factory.getParameterInfo(bufferName);
        Assert.assertEquals(4, params.size());

        Parameter<?> geom = params.get("geom");
        Assert.assertEquals(Geometry.class, geom.type);
        Assert.assertTrue(geom.required);

        Parameter<?> distance = params.get("distance");
        Assert.assertEquals(Double.class, distance.type);
        Assert.assertTrue(distance.required);

        Parameter<?> quadrants = params.get("quadrantSegments");
        Assert.assertEquals(Integer.class, quadrants.type);
        Assert.assertFalse(quadrants.required);
        Assert.assertEquals(0, quadrants.minOccurs);
        Assert.assertEquals(1, quadrants.maxOccurs);

        Parameter<?> capStyle = params.get("capStyle");
        Assert.assertEquals(GeometryFunctions.BufferCapStyle.class, capStyle.type);
        Assert.assertFalse(capStyle.required);
        Assert.assertEquals(0, capStyle.minOccurs);
        Assert.assertEquals(1, capStyle.maxOccurs);
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testExecuteBuffer() throws Exception {
        org.geotools.process.Process buffer = factory.create(new NameImpl("geo", "Buffer"));

        // try less than the required params
        Map<String, Object> inputs = new HashMap<>();
        try {
            buffer.execute(inputs, null);
            Assert.fail("What!!! Should have failed big time!");
        } catch (ProcessException e) {
            // fine
        }

        // try out only the required params
        Geometry geom = new WKTReader().read("POINT(0 0)");
        inputs.put("geom", geom);
        inputs.put("distance", 1d);
        Map<String, Object> result = buffer.execute(inputs, null);

        Assert.assertEquals(1, result.size());
        Geometry buffered = (Geometry) result.get("result");
        Assert.assertNotNull(buffered);
        Assert.assertTrue(buffered.equals(geom.buffer(1d)));

        // pass in all params
        inputs.put("quadrantSegments", 12);
        inputs.put("capStyle", GeometryFunctions.BufferCapStyle.Square);
        result = buffer.execute(inputs, null);

        Assert.assertEquals(1, result.size());
        buffered = (Geometry) result.get("result");
        Assert.assertNotNull(buffered);
        Assert.assertTrue(buffered.equals(geom.buffer(1d, 12, BufferParameters.CAP_SQUARE)));
    }

    @Test
    public void testSPI() throws Exception {
        NameImpl bufferName = new NameImpl("geo", "buffer");
        ProcessFactory factory = Processors.createProcessFactory(bufferName);
        Assert.assertNotNull(factory);
        Assert.assertTrue(factory instanceof GeometryProcessFactory);

        org.geotools.process.Process buffer = Processors.createProcess(bufferName);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testDescribeUnion() {
        NameImpl unionName = new NameImpl("geo", "union");
        InternationalString desc = factory.getDescription(unionName);
        Assert.assertNotNull(desc);

        Map<String, Parameter<?>> params = factory.getParameterInfo(unionName);
        Assert.assertEquals(1, params.size());

        Parameter<?> geom = params.get("geom");
        Assert.assertEquals(Geometry.class, geom.type);
        Assert.assertTrue(geom.required);
        Assert.assertEquals(2, geom.minOccurs);
        Assert.assertEquals(Integer.MAX_VALUE, geom.maxOccurs);
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testExecuteUnion() throws Exception {
        org.geotools.process.Process union = factory.create(new NameImpl("geo", "union"));

        // try less than the required params
        Map<String, Object> inputs = new HashMap<>();
        try {
            union.execute(inputs, null);
            Assert.fail("What!!! Should have failed big time!");
        } catch (ProcessException e) {
            // fine
        }

        // try again with less
        Geometry geom1 = new WKTReader().read("POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))");
        Geometry geom2 = new WKTReader().read("POLYGON((0 1, 0 2, 1 2, 1 1, 0 1))");
        List<Geometry> geometries = new ArrayList<>();
        geometries.add(geom1);
        inputs.put("geom", geometries);
        try {
            union.execute(inputs, null);
            Assert.fail("What!!! Should have failed big time!");
        } catch (ProcessException e) {
            // fine
        }

        // now with just enough
        geometries.add(geom2);
        Map<String, Object> result = union.execute(inputs, null);

        Assert.assertEquals(1, result.size());
        Geometry united = (Geometry) result.get("result");
        Assert.assertNotNull(united);
        Assert.assertTrue(united.equals(geom1.union(geom2)));
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testExecuteHull() throws Exception {
        NameImpl hullName = new NameImpl("geo", "convexHull");
        org.geotools.process.Process hull = factory.create(hullName);

        Map<String, Object> inputs = new HashMap<>();
        Geometry geom = new WKTReader().read("LINESTRING(0 0, 0 1, 1 1)");
        inputs.put("geom", geom);
        Map<String, Object> output = hull.execute(inputs, null);

        Assert.assertEquals(1, output.size());
        // there is no output annotation, check there is consistency between what is declared
        // and what is returned
        Geometry result =
                (Geometry)
                        output.get(
                                factory.getResultInfo(hullName, null).keySet().iterator().next());
        Assert.assertTrue(result.equals(geom.convexHull()));
    }
}
