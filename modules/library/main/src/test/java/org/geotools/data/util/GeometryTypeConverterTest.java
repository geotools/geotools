package org.geotools.data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.geometry.jts.MultiCurvedGeometry;
import org.geotools.test.TestData;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;

/**
 * Test suite for the GeometryTypeConverterFactory Converters.
 *
 * @author Mauro Bartolomeoli
 */
public class GeometryTypeConverterTest {
    Set<ConverterFactory> factories = null;
    List<String> tests = new ArrayList<>();

    WKTReader wktReader = new WKTReader();

    protected Converter getConverter(Geometry source, Class<?> target) {
        Assert.assertNotNull("Cannot get ConverterFactory for Geometry -> Geometry conversion", factories);
        if (factories == null) return null;
        for (ConverterFactory factory : factories) {
            Converter candidate = factory.createConverter(source.getClass(), target, null);
            if (candidate != null) return candidate;
        }
        Assert.fail("Cannot get ConverterFactory for "
                + source.getClass().getName()
                + " -> "
                + target.getName()
                + " conversion");
        return null;
    }

    @Before
    public void setUp() throws Exception {
        factories = Converters.getConverterFactories(Geometry.class, Geometry.class);
        File testData = TestData.file(this, "converter/tests.txt");
        Assert.assertNotNull("Cannot find test file (converter.txt)", testData);
        try (BufferedReader reader = new BufferedReader(new FileReader(testData, StandardCharsets.UTF_8))) {
            String line = null;

            while ((line = reader.readLine()) != null) tests.add(line);
        }
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testData() throws Exception {
        for (String test : tests) {
            String[] parts = test.split(":");
            Assert.assertEquals(
                    "Test rows should have the form \"source:target:expected:description\"", 4, parts.length);

            Geometry source = wktReader.read(parts[0]);
            source.setSRID(4326);
            Class<?> target = Class.forName("org.locationtech.jts.geom." + parts[1]);
            String expected = parts[2];
            Geometry converted = convert(source, target);
            Assert.assertEquals(expected, converted.toText());
            Assert.assertEquals(4326, converted.getSRID());
            //			System.out.println(parts[3]+": OK");
        }
    }

    private Geometry convert(Geometry source, Class<?> target) throws Exception {
        Converter converter = getConverter(source, target);
        Object dest = converter.convert(source, target);
        Assert.assertNotNull("Cannot convert " + source.toText() + " to " + target.getName(), dest);
        Assert.assertTrue("Converted object is not a Geometry", dest instanceof Geometry);
        return (Geometry) dest;
    }

    @Test
    public void testLineStringToCurve() throws Exception {
        Geometry ls = new WKTReader().read("LINESTRING(0 0, 10 10)");
        Converter converter = getConverter(ls, CurvedGeometry.class);
        CurvedGeometry curve = converter.convert(ls, CurvedGeometry.class);
        Assert.assertTrue(curve instanceof CompoundCurve);
        CompoundCurve cc = (CompoundCurve) curve;
        Assert.assertEquals(1, cc.getComponents().size());
        Assert.assertEquals(ls, cc.getComponents().get(0));
    }

    @Test
    public void testLineStringToMultiCurve() throws Exception {
        Geometry ls = new WKTReader().read("LINESTRING(0 0, 10 10)");
        Converter converter = getConverter(ls, CurvedGeometry.class);
        MultiCurvedGeometry curve = converter.convert(ls, MultiCurvedGeometry.class);
        Assert.assertTrue(curve instanceof MultiCurve);
        MultiCurve mc = (MultiCurve) curve;
        Assert.assertEquals(1, mc.getNumGeometries());
        Assert.assertEquals(ls, mc.getGeometryN(0));
    }

    @Test
    public void testLinearRingToCurve() throws Exception {
        Geometry ls = new WKTReader().read("LINEARRING(0 0, 10 10, 10 0, 0 0)");
        Map<String, String> userData = Collections.singletonMap("test", "value");
        ls.setUserData(userData);
        Converter converter = getConverter(ls, CurvedGeometry.class);
        CurvedGeometry curve = converter.convert(ls, CurvedGeometry.class);
        Assert.assertTrue(curve instanceof CompoundRing);
        CompoundRing cr = (CompoundRing) curve;
        Assert.assertEquals(1, cr.getComponents().size());
        Assert.assertEquals(ls, cr.getComponents().get(0));
        Assert.assertEquals(userData, cr.getUserData());
    }

    @Test
    public void testPolygonToMultiPolygon() throws Exception {
        Geometry pol = new WKTReader().read("POLYGON((0 0, 10 10, 10 0, 0 0))");
        Map<String, String> userData = Collections.singletonMap("test", "value");
        pol.setUserData(userData);
        Converter converter = getConverter(pol, MultiPolygon.class);
        MultiPolygon mp = converter.convert(pol, MultiPolygon.class);
        Assert.assertEquals(1, mp.getNumGeometries());
        Assert.assertEquals(pol, mp.getGeometryN(0));
        Assert.assertNull(mp.getGeometryN(0).getUserData());
        Assert.assertEquals(userData, mp.getUserData());
    }
}
