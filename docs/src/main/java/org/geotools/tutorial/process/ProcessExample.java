package org.geotools.tutorial.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.feature.BufferFeatureCollectionFactory;
import org.geotools.process.feature.BufferFeatureCollectionProcess;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class ProcessExample {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        example1();
        example2();
    }

    public static void example1() throws Exception {

        WKTReader reader = new WKTReader(new GeometryFactory());

        Geometry geom1 = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Double buffer = new Double(213.78);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(BufferFactory.GEOM1.key, geom1);
        map.put(BufferFactory.BUFFER.key, buffer);

        BufferProcess process = new BufferProcess(null);
        Map<String, Object> resultMap = process.execute(map, null);

        Object result = resultMap.get(BufferFactory.RESULT.key);
        Geometry bufferedGeom = geom1.buffer(buffer);
    }

    public static void example2() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Point.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        for (int i = 0; i < 2; i++) {
            b.add(gf.createPoint(new Coordinate(i, i)));
            b.add(i);
            features.add(b.buildFeature(i + ""));
        }

        Map<String, Object> input = new HashMap();
        input.put(BufferFeatureCollectionFactory.FEATURES.key, features);
        input.put(BufferFeatureCollectionFactory.BUFFER.key, 10d);

        BufferFeatureCollectionFactory factory = new BufferFeatureCollectionFactory();
        BufferFeatureCollectionProcess process = factory.create();
        Map<String, Object> output = process.execute(input, null);

        FeatureCollection buffered = (FeatureCollection) output
                .get(BufferFeatureCollectionFactory.RESULT.key);

        assertEquals(2, buffered.size());
        for (int i = 0; i < 2; i++) {
            Geometry expected = gf.createPoint(new Coordinate(i, i)).buffer(10d);
            FeatureCollection sub = buffered.subCollection(ff.equals(ff.property("integer"),
                    ff.literal(i)));
            assertEquals(1, sub.size());
            FeatureIterator iterator = sub.features();
            SimpleFeature sf = (SimpleFeature) iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
            iterator.close();
        }
    }

}
